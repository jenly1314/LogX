package com.king.logx.logger.config

import com.king.logx.logger.LogFormat

private const val ONE_MINUTE_MILLIS = 60 * 1000L
private const val DEFAULT_REUSE_THRESHOLD_MILLIS = 60 * ONE_MINUTE_MILLIS

/**
 * 文件日志记录器配置
 *
 * File logger configuration
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
     * [FileLoggerConfig] 构建器基类，支持泛型自引用以保证子类链式调用返回正确类型
     *
     * Base builder for [FileLoggerConfig] with self-referential generic for fluent subclass chaining.
     */
    open class BaseBuilder<SELF : BaseBuilder<SELF>> : DefaultLoggerConfig.BaseBuilder<SELF>() {
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
        var reuseThresholdMillis: Long = DEFAULT_REUSE_THRESHOLD_MILLIS

        /**
         * 设置是否同时输出日志到Logcat
         *
         * Set whether to also output logs to Logcat.
         *
         * @param logToLogcat Whether to output to Logcat. Default false
         */
        fun setLogToLogcat(logToLogcat: Boolean): SELF {
            this.logToLogcat = logToLogcat
            return self()
        }

        /**
         * 设置单个日志文件的最大大小
         *
         * Set maximum size for single log file.
         *
         * @param maxFileSize Maximum file size in bytes. Default 2MB
         */
        fun setMaxFileSize(maxFileSize: Long): SELF {
            this.maxFileSize = maxFileSize
            return self()
        }

        /**
         * 设置最大日志文件数量
         *
         * Set maximum number of log files.
         *
         * @param maxFileCount Maximum file count. Default 10
         */
        fun setMaxFileCount(maxFileCount: Int): SELF {
            this.maxFileCount = maxFileCount
            return self()
        }

        /**
         * 设置日志文件前缀
         *
         * Set log file name prefix.
         *
         * @param filePrefix File name prefix. Default "logx_"
         */
        fun setFilePrefix(filePrefix: String): SELF {
            this.filePrefix = filePrefix
            return self()
        }

        /**
         * 设置日志文件扩展名
         *
         * Set log file extension.
         *
         * @param fileExtension File extension. Default ".log"
         */
        fun setFileExtension(fileExtension: String): SELF {
            this.fileExtension = fileExtension
            return self()
        }

        /**
         * 设置日志文件的命名格式模式
         *
         * Sets the pattern for log file name formatting.
         *
         * @param formatPattern The date format pattern for log file names, defaults to "yyyyMMdd_HHmmss"
         */
        fun setFileNameFormatPattern(formatPattern: String): SELF {
            this.fileNameFormatPattern = formatPattern
            return self()
        }

        /**
         * 设置日志内容中的日期时间格式模式
         *
         * Sets the date-time format pattern used in log entries.
         *
         * @param formatPattern The date-time format pattern for log entries, defaults to "yyyy-MM-dd HH:mm:ss.SSS"
         */
        fun setLogDateFormatPattern(formatPattern: String): SELF {
            this.logDateFormatPattern = formatPattern
            return self()
        }

        /**
         * 设置日志目录
         *
         * Set log directory.
         *
         * @param logDir Directory path for storing logs. Default "logs"
         */
        fun setLogDir(logDir: String): SELF {
            this.logDir = logDir
            return self()
        }

        /**
         * 设置日志文件可复用的时间阈值（毫秒），默认：1小时
         *
         * Sets the time threshold (in milliseconds) for reusing log files, default: 1 hour (3600000 ms)
         *
         * @param reuseThresholdMillis Maximum time difference (in milliseconds) allowed for log reuse
         */
        fun setReuseThresholdMillis(reuseThresholdMillis: Long): SELF {
            this.reuseThresholdMillis = reuseThresholdMillis
            return self()
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

    /**
     * [FileLoggerConfig] 构建器
     *
     * Builder for [FileLoggerConfig]
     */
    open class Builder : BaseBuilder<Builder>()

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

/**
 * DSL风格顶级函数，用于构建 [FileLoggerConfig]
 *
 * Top-level DSL function for building a [FileLoggerConfig].
 *
 * @return [FileLoggerConfig]
 */
@JvmSynthetic
inline fun fileLoggerConfig(block: FileLoggerConfig.Builder.() -> Unit = {}): FileLoggerConfig {
    return FileLoggerConfig.Builder().apply(block).build()
}
