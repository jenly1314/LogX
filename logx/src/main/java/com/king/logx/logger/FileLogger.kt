package com.king.logx.logger

import android.content.Context
import android.util.Log
import com.king.logx.LogX
import com.king.logx.logger.config.FileLoggerConfig
import com.king.logx.util.Utils
import com.king.logx.util.Utils.Companion.utf8ByteSize
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 文件日志记录器
 *
 * File logger implementation.
 *
 * @param context [Context]
 * @param config Logger configuration.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class FileLogger @JvmOverloads constructor(
    context: Context,
    private val config: FileLoggerConfig = FileLoggerConfig.build(),
) : DefaultLogger(config) {

    private val appContext = context.applicationContext

    private val fileNameFormat = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat(config.fileNameFormatPattern, Locale.getDefault())
        }
    }

    private val logDateFormat = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat(config.logDateFormatPattern, Locale.getDefault())
        }
    }

    /**
     * 日志写入器
     */
    private class LogWriter(val file: File, val size: AtomicLong) {

        @Volatile
        private var writer: BufferedWriter? = null

        fun write(message: String) {
            if (writer == null) {
                writer = BufferedWriter(
                    OutputStreamWriter(
                        FileOutputStream(file, true),
                        StandardCharsets.UTF_8
                    )
                )
            }
            writer?.write(message)
        }

        @Synchronized
        fun close() {
            try {
                writer?.flush()
            } finally {
                writer?.close()
                writer = null
            }
        }
    }

    private val coroutineScope = CoroutineScope(
        context = Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, e ->
            Log.w(TAG, Utils.getStackTraceString(e))
        }
    )

    private val logChannel = Channel<String>(Channel.UNLIMITED)

    private var isLogInProgress = AtomicBoolean(false)

    @Volatile
    private var currentWriter: LogWriter? = null

    init {
        startLogProcessor()
    }

    private fun startLogProcessor() = coroutineScope.launch {
        // 处理日志消息
        logChannel.consumeEach {
            processLogMessage(it)
        }
    }

    @Synchronized
    private fun processLogMessage(message: String) {
        try {
            val byteSize = message.utf8ByteSize()

            currentWriter?.also {
                if (it.size.get() + byteSize > config.maxFileSize) rotateLogFile()
            } ?: run {
                currentWriter = createLogWriter(true)
                cleanupOldLogs()
            }

            // 写入日志
            currentWriter?.apply {
                write(message)
                size.addAndGet(byteSize)
                if (!isLogInProgress.get()) {
                    close()
                }
            }
        } catch (e: Exception) {
            if (LogX.isDebug) {
                Log.w(TAG, Utils.getStackTraceString(e))
            }
            try {
                currentWriter?.close()
            } finally {
                currentWriter = null
            }
        }
    }

    private fun createLogWriter(reuseRecentFile: Boolean): LogWriter {
        val logDir = Utils.getCacheFileDir(appContext, config.logDir).apply {
            if (!exists()) mkdirs()
        }

        val logFile = if (reuseRecentFile && config.reuseThresholdMillis > 0) {
            // 优先复用满足条件的旧日志文件
            findLatestUsableLogFile(logDir) ?: createNewLogFile(logDir)
        } else {
            createNewLogFile(logDir)
        }

        if (LogX.isDebug) {
            Log.d(TAG, "Log file: $logFile")
        }
        return LogWriter(file = logFile, size = AtomicLong(logFile.length()))
    }

    private fun createNewLogFile(logDir: File): File {
        return config.run {
            File(logDir, "$filePrefix${fileNameFormat.get()!!.format(Date())}$fileExtension")
        }
    }

    private fun findLatestUsableLogFile(logDir: File): File? {
        val minReusableTime = System.currentTimeMillis() - config.reuseThresholdMillis
        var candidateFile: File? = null
        var latestTime = 0L

        logDir.list()?.let { filenames ->
            for (filename in filenames) {
                if (!filename.startsWith(config.filePrefix) || !filename.endsWith(config.fileExtension)) {
                    continue
                }
                try {
                    val file = File(logDir, filename)

                    val lastModified = file.lastModified()
                    // 小于最小可复用时间的直接跳过
                    if (lastModified < minReusableTime) continue

                    // 只更新候选文件（如果比当前候选文件更新）
                    if (lastModified > latestTime) {
                        candidateFile = file
                        latestTime = lastModified
                    }
                } catch (e: Exception) {
                    if (LogX.isDebug) {
                        Log.w(TAG, Utils.getStackTraceString(e))
                    }
                }
            }
        }

        // 最后检查候选文件是否满足大小和可写条件
        return candidateFile?.takeIf { it.length() < config.maxFileSize && it.canWrite() }
    }

    private fun rotateLogFile() {
        currentWriter?.close()
        currentWriter = createLogWriter(false)
        cleanupOldLogs()
    }

    private fun cleanupOldLogs() {
        try {
            Utils.getCacheFileDir(appContext, config.logDir).listFiles { file ->
                file.name.startsWith(config.filePrefix) && file.name.endsWith(config.fileExtension)
            }?.sortedBy { it.lastModified() }?.also { logFiles ->
                if (logFiles.size > config.maxFileCount) {
                    // 清理旧日志
                    logFiles.take(logFiles.size - config.maxFileCount)
                        .forEach { it.delete() }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, Utils.getStackTraceString(e))
        }
    }

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        isLogInProgress.set(true)
        super.log(priority, tag, message, t)
        isLogInProgress.set(false)
    }

    override fun println(priority: Int, tag: String?, message: String) {
        if (config.logToLogcat) super.println(priority, tag, message)
        when {
            @OptIn(DelicateCoroutinesApi::class)
            !logChannel.isClosedForSend -> {
                logChannel.trySend(buildMessage(priority, tag, message))
            }

            LogX.isDebug -> {
                Log.w(TAG, "Log channel is closed.")
            }
        }
    }

    /**
     * 构建日志消息；默认格式为："$timestamp $level/$tag: $message"
     *
     * Build log message with default format: "$timestamp $level/$tag: $message"
     *
     * @param priority Log level. See [LogX] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message log message.
     */
    protected open fun buildMessage(priority: Int, tag: String?, message: String): String {
        val timestamp = logDateFormat.get()!!.format(Date())
        val level = Utils.getLogLevel(priority)
        return "$timestamp $level/${tag.toString()}: $message\n"
    }

    /**
     * 关闭协程
     *
     * Shutdown coroutine.
     */
    fun shutdown() {
        if (coroutineScope.isActive) {
            coroutineScope.cancel()
            Log.d(TAG, "CoroutineScope was cancelled.")
        } else {
            Log.d(TAG, "CoroutineScope was already inactive.")
        }
    }

    internal companion object {
        private const val TAG = "FileLogger"
    }
}
