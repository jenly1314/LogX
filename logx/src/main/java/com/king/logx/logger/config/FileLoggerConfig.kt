package com.king.logx.logger.config

import com.king.logx.logger.FileLogger
import com.king.logx.logger.LogFormat
import java.util.concurrent.TimeUnit

/**
 * 日志记录器配置；适用于 [FileLogger]
 *
 * Logger configuration for [FileLogger]
 *
 * @param logToLogcat Whether to also output logs to Logcat.
 * @param maxFileSize Maximum size for single log file (in bytes)
 * @param maxFileCount Maximum number of log files.
 * @param filePrefix Log file name prefix.
 * @param fileExtension Log file extension.
 * @param fileNameFormatPattern Log filename format pattern.
 * @param logDateFormatPattern Date-time format pattern in log entries.
 * @param logDir Directory path for storing logs.
 * @param reuseThresholdMillis Time threshold (in milliseconds) for reusing log files.
 * @param logFormat Log display format.
 * @param showThreadInfo Whether to show thread info.
 * @param methodCount How many method line to show.
 * @param methodOffset Stack trace offset (hides internal method calls)
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class FileLoggerConfig protected constructor(
    val logToLogcat: Boolean,
    val maxFileSize: Long,
    val maxFileCount: Int,
    val filePrefix: String,
    val fileExtension: String,
    val fileNameFormatPattern: String,
    val logDateFormatPattern: String,
    val logDir: String,
    val reuseThresholdMillis: Long,
    logFormat: LogFormat,
    showThreadInfo: Boolean,
    methodCount: Int,
    methodOffset: Int,
) : DefaultLoggerConfig(logFormat, showThreadInfo, methodCount, methodOffset) {

    /**
     * [FileLoggerConfig] 构建器
     *
     * Builder for [FileLoggerConfig]
     */
    open class Builder : DefaultLoggerConfig.Builder() {
        /**
         * 设置是否同时输出日志到Logcat，默认：false
         *
         * Whether to output to Logcat. Default false
         */
        @set:JvmSynthetic
        var logToLogcat: Boolean = false

        /**
         * 设置单个日志文件的最大大小，默认：2M
         *
         * Maximum size for single log file (in bytes). Default 2MB
         */
        @set:JvmSynthetic
        var maxFileSize: Long = 2 * 1024 * 1024

        /**
         * 设置最大日志文件数量，默认：10
         *
         * Maximum number of log files. Default 10
         */
        @set:JvmSynthetic
        var maxFileCount: Int = 10

        /**
         * 设置日志文件前缀，默认：logx_
         *
         * Log file name prefix. Default "logx_"
         */
        @set:JvmSynthetic
        var filePrefix: String = "logx_"

        /**
         * 设置日志文件扩展名，默认：.log
         *
         * Log file extension. Default ".log"
         */
        @set:JvmSynthetic
        var fileExtension: String = ".log"

        /**
         * 日志文件名格式模式，用于定义生成的日志文件命名规则，默认：[LOG_FILENAME_FORMAT_PATTERN]
         *
         * Log filename format pattern. Default: "yyyyMMdd_HHmmss"
         */
        @set:JvmSynthetic
        var fileNameFormatPattern: String = LOG_FILENAME_FORMAT_PATTERN

        /**
         * 日志内容中的日期时间格式模式，用于控制每条日志记录的时间戳显示格式，默认：[LOG_DATE_FORMAT_PATTERN]
         *
         * Date-time format pattern in log entries. Default: "yyyy-MM-dd HH:mm:ss.SSS"
         */
        @set:JvmSynthetic
        var logDateFormatPattern: String = LOG_DATE_FORMAT_PATTERN

        /**
         * 设置日志目录，默认：logs
         *
         * Directory path for storing logs. Default "logs"
         */
        @set:JvmSynthetic
        var logDir: String = "logs"

        /**
         * 日志文件可复用的时间阈值（毫秒），默认：1小时
         *
         * Time threshold (in milliseconds) for reusing log files. Default 1 hour
         */
        @set:JvmSynthetic
        var reuseThresholdMillis: Long = TimeUnit.HOURS.toMillis(1)

        /**
         * 设置是否同时输出日志到Logcat
         *
         * Set whether to also output logs to Logcat.
         *
         * @param logToLogcat Whether to output to Logcat. Default false
         */
        open fun setLogToLogcat(logToLogcat: Boolean): Builder {
            this.logToLogcat = logToLogcat
            return this
        }

        /**
         * 设置单个日志文件的最大大小
         *
         * Set maximum size for single log file.
         *
         * @param maxFileSize Maximum file size in bytes. Default 2MB
         */
        open fun setMaxFileSize(maxFileSize: Long): Builder {
            this.maxFileSize = maxFileSize
            return this
        }

        /**
         * 设置最大日志文件数量
         *
         * Set maximum number of log files.
         *
         * @param maxFileCount Maximum file count. Default 10
         */
        open fun setMaxFileCount(maxFileCount: Int): Builder {
            this.maxFileCount = maxFileCount
            return this
        }

        /**
         * 设置日志文件前缀
         *
         * Set log file name prefix.
         *
         * @param filePrefix File name prefix. Default "logx_"
         */
        open fun setFilePrefix(filePrefix: String): Builder {
            this.filePrefix = filePrefix
            return this
        }

        /**
         * 设置日志文件扩展名
         *
         * Set log file extension.
         *
         * @param fileExtension File extension. Default ".log"
         */
        open fun setFileExtension(fileExtension: String): Builder {
            this.fileExtension = fileExtension
            return this
        }

        /**
         * 设置日志文件的命名格式模式
         *
         * Sets the pattern for log file name formatting.
         *
         * @param formatPattern The date format pattern for log file names, defaults to "yyyyMMdd_HHmmss"
         */
        open fun setFileNameFormatPattern(formatPattern: String): Builder {
            this.fileNameFormatPattern = formatPattern
            return this
        }

        /**
         * 设置日志内容中的日期时间格式模式
         *
         * Sets the date-time format pattern used in log entries.
         *
         * @param formatPattern The date-time format pattern for log entries, defaults to "yyyy-MM-dd HH:mm:ss.SSS"
         */
        open fun setLogDateFormatPattern(formatPattern: String): Builder {
            this.logDateFormatPattern = formatPattern
            return this
        }

        /**
         * 设置日志目录
         *
         * Set log directory.
         *
         * @param logDir Directory path for storing logs. Default "logs"
         */
        open fun setLogDir(logDir: String): Builder {
            this.logDir = logDir
            return this
        }

        /**
         * 设置日志文件可复用的时间阈值（毫秒），默认：1小时
         *
         * Sets the time threshold (in milliseconds) for reusing log files, default: 1 hour (3600000 ms)
         *
         * @param reuseThresholdMillis Maximum time difference (in milliseconds) allowed for log reuse
         */
        open fun setReuseThresholdMillis(reuseThresholdMillis: Long): Builder {
            this.reuseThresholdMillis = reuseThresholdMillis
            return this
        }

        /**
         * 设置日志显示格式
         *
         * Set log display format.
         *
         * @param logFormat Log display format, defaults to [LogFormat.PRETTY] for better readability
         */
        override fun setLogFormat(logFormat: LogFormat): Builder {
            super.setLogFormat(logFormat)
            return this
        }

        /**
         * 设置堆栈跟踪偏移量
         *
         * Set stack trace offset.
         *
         * @param methodOffset Stack trace offset (hides internal method calls)
         */
        override fun setMethodOffset(methodOffset: Int): Builder {
            super.setMethodOffset(methodOffset)
            return this
        }

        /**
         * 设置是否显示线程信息
         *
         * Set whether to show thread info.
         *
         * @param showThreadInfo Whether to show thread info or not. Default true
         */
        override fun setShowThreadInfo(showThreadInfo: Boolean): Builder {
            super.setShowThreadInfo(showThreadInfo)
            return this
        }

        /**
         * 设置要显示的调用栈方法行数
         *
         * Sets the number of method lines to show.
         *
         * @param methodCount Number of lines to show (default: 2)
         */
        override fun setMethodCount(methodCount: Int): Builder {
            super.setMethodCount(methodCount)
            return this
        }

        override fun build(): FileLoggerConfig {
            return FileLoggerConfig(
                logToLogcat,
                maxFileSize,
                maxFileCount,
                filePrefix,
                fileExtension,
                fileNameFormatPattern,
                logDateFormatPattern,
                logDir,
                reuseThresholdMillis,
                logFormat,
                showThreadInfo,
                methodCount,
                methodOffset
            )
        }
    }

    companion object {
        private const val LOG_FILENAME_FORMAT_PATTERN = "yyyyMMdd_HHmmss"
        private const val LOG_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"

        /**
         * DSL风格构建器
         *
         * DSL style builder
         *
         * @return [FileLoggerConfig]
         */
        @JvmSynthetic
        inline fun build(block: Builder.() -> Unit = {}): FileLoggerConfig {
            return Builder().apply(block).build()
        }
    }
}
