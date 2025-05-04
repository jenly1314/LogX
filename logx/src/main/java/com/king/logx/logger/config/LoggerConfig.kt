package com.king.logx.logger.config

import com.king.logx.logger.LogFormat
import com.king.logx.logger.Logger

/**
 * 日志记录器配置；适用于 [Logger]
 *
 * Logger configuration for [Logger]
 *
 * @param logFormat Log display format.
 * @param methodOffset Stack trace offset (hides internal method calls)
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class LoggerConfig protected constructor(
    val logFormat: LogFormat,
    val methodOffset: Int,
) {

    /**
     * [LoggerConfig] 构建器
     *
     * Builder for [LoggerConfig]
     */
    open class Builder {

        /**
         * 日志显示格式，默认：[LogFormat.PRETTY]
         *
         * Log display format, defaults to [LogFormat.PRETTY] for better readability.
         */
        @set:JvmSynthetic
        var logFormat: LogFormat = LogFormat.PRETTY

        /**
         * 堆栈跟踪偏移量
         *
         * Stack trace offset (hides internal method calls)
         */
        @set:JvmSynthetic
        var methodOffset: Int = 0

        /**
         * 设置日志显示格式
         *
         * Set log display format.
         *
         * @param logFormat Log display format, defaults to [LogFormat.PRETTY] for better readability
         */
        open fun setLogFormat(logFormat: LogFormat): Builder {
            this.logFormat = logFormat
            return this
        }

        /**
         * 设置堆栈跟踪偏移量
         *
         * Set stack trace offset.
         *
         * @param methodOffset Stack trace offset (hides internal method calls)
         */
        open fun setMethodOffset(methodOffset: Int): Builder {
            this.methodOffset = methodOffset
            return this
        }

        /**
         * 构建 [LoggerConfig]
         */
        open fun build(): LoggerConfig {
            return LoggerConfig(logFormat, methodOffset)
        }
    }

    companion object {

        /**
         * DSL风格构建器
         *
         * DSL style builder
         *
         * @return [LoggerConfig]
         */
        @JvmSynthetic
        inline fun build(block: Builder.() -> Unit = {}): LoggerConfig {
            return Builder().apply(block).build()
        }
    }
}

