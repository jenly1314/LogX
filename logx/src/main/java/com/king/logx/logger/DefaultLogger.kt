package com.king.logx.logger

import android.util.Log
import com.king.logx.LogX
import com.king.logx.logger.config.DefaultLoggerConfig
import com.king.logx.util.Utils
import com.king.logx.util.Utils.Companion.utf8ByteSize

/**
 * 默认实现的[Logger]，既美观又实用。
 *
 * Default [Logger] implementation with beautiful & practical design.
 *
 * @param config Logger configuration.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class DefaultLogger @JvmOverloads constructor(
    config: DefaultLoggerConfig = DefaultLoggerConfig.build(),
) : Logger(config) {

    private val showThreadInfo = config.showThreadInfo
    private val methodCount = config.methodCount

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        val logMessage = when {
            message.isNullOrEmpty() && t != null -> Utils.getStackTraceString(t)
            t != null -> "$message\n${Utils.getStackTraceString(t)}"
            else -> message.toString()
        }

        when (lastLogFormat) {
            LogFormat.PRETTY -> {
                logTopBorder(priority, tag)
                logStackTrace(priority, tag)
                logContent(priority, tag, logMessage) {
                    "$HORIZONTAL_LINE $it"
                }
                logBottomBorder(priority, tag)
            }

            LogFormat.PLAIN -> {
                logContent(priority, tag, logMessage)
            }
        }
    }

    private fun logTopBorder(priority: Int, tag: String?) {
        println(priority, tag, TOP_BORDER)
    }

    private fun logStackTrace(priority: Int, tag: String?) {
        if (showThreadInfo) {
            println(priority, tag, "$HORIZONTAL_LINE Thread: ${Thread.currentThread().name}")
            logDivider(priority, tag)
        }

        if (methodCount <= 0) return

        val stackTrace = getStackTrace()
        val baseTraceIndex = getStackOffset(stackTrace) + lastOffset
        val traceDepth = minOf(methodCount, stackTrace.size - baseTraceIndex)

        if (traceDepth <= 0) return

        val deepestTraceIndex = baseTraceIndex + traceDepth - 1
        var indentLevel = ""
        val messageBuffer = StringBuilder(TRACE_LINE_CAPACITY * traceDepth)

        // 遍历打印调用栈信息
        for (i in deepestTraceIndex downTo baseTraceIndex) {
            val stackElement = stackTrace[i]
            messageBuffer.run {
                append(HORIZONTAL_LINE)
                append(' ')
                append(indentLevel)
                append(stackElement.className.substringAfterLast('.'))
                append('.')
                append(stackElement.methodName)
                append('(')
                append(stackElement.fileName)
                append(':')
                append(stackElement.lineNumber)
                append(')')
                toString()
            }.also { println(priority, tag, it) }

            indentLevel += INDENT
            messageBuffer.clear()
        }

        logDivider(priority, tag)
    }

    private fun logDivider(priority: Int, tag: String?) {
        println(priority, tag, MIDDLE_BORDER)
    }

    private fun logBottomBorder(priority: Int, tag: String?) {
        println(priority, tag, BOTTOM_BORDER)
    }

    private inline fun logContent(
        priority: Int,
        tag: String?,
        message: String,
        crossinline transform: (String) -> String = { it }
    ) {
        message.lineSequence().forEach {
            if (shouldSplitChunks(it)) {
                splitLogChunks(it) { chunk ->
                    println(priority, tag, transform(chunk))
                }
            } else {
                println(priority, tag, transform(it))
            }
        }
    }

    private fun shouldSplitChunks(message: String): Boolean {
        // 短日志快速跳过（保守估计：3字节/字符）
        if (message.length <= SIMPLE_LOG_MAX_CHARS) return false

        // 超长日志直接拆分（乐观估计：1字节/字符）
        if (message.length > MAX_LOG_BYTES) return true

        // 精确计算日志的UTF-8字节长度
        var bytes = 0
        for (char in message) {
            bytes += char.utf8ByteSize()
            if (bytes > MAX_LOG_BYTES) return true
        }
        return false
    }

    private inline fun splitLogChunks(message: String, crossinline onChunk: (String) -> Unit) {
        var chunkStart = 0
        var byteCount = 0
        val length = message.length
        var i = 0

        while (i < length) {
            val char = message[i]
            val charByteSize = char.utf8ByteSize()

            // 累计字节数超过限制时，拆分当前块
            if (byteCount + charByteSize > MAX_LOG_BYTES) {
                onChunk(message.substring(chunkStart, i))
                chunkStart = i
                byteCount = charByteSize
            } else {
                byteCount += charByteSize
            }
            i++
        }

        // 处理最后剩余的字符块
        if (chunkStart < length) {
            onChunk(message.substring(chunkStart))
        }
    }

    /**
     * 将日志消息写入目标输出
     *
     * Write a log message to its destination.
     *
     * @param priority Log level. See [LogX] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message log message.
     */
    protected open fun println(priority: Int, tag: String?, message: String) {
        Log.println(priority, tag, message)
    }

}
