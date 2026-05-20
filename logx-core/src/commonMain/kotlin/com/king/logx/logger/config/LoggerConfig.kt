package com.king.logx.logger.config

import com.king.logx.logger.LogFormat

/**
 * 日志记录器配置
 *
 * Logger configuration
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
     * [LoggerConfig] 构建器基类，支持泛型自引用以保证子类链式调用返回正确类型
     *
     * Base builder for [LoggerConfig] with self-referential generic for fluent subclass chaining.
     */
    open class BaseBuilder<SELF : BaseBuilder<SELF>> {

        /**
         * 日志显示格式，默认：[LogFormat.PRETTY]
         *
         * Log display format, defaults to [LogFormat.PRETTY] for better readability.
         */
        var logFormat: LogFormat = LogFormat.PRETTY

        /**
         * 堆栈跟踪偏移量
         *
         * Stack trace offset (hides internal method calls)
         */
        var methodOffset: Int = 0

        @Suppress("UNCHECKED_CAST")
        protected fun self(): SELF = this as SELF

        /**
         * 设置日志显示格式
         *
         * Set log display format.
         *
         * @param logFormat Log display format, defaults to [LogFormat.PRETTY] for better readability
         */
        fun setLogFormat(logFormat: LogFormat): SELF {
            this.logFormat = logFormat
            return self()
        }

        /**
         * 设置堆栈跟踪偏移量
         *
         * Set stack trace offset.
         *
         * @param methodOffset Stack trace offset (hides internal method calls)
         */
        fun setMethodOffset(methodOffset: Int): SELF {
            this.methodOffset = methodOffset
            return self()
        }

        /**
         * 构建 [LoggerConfig]
         */
        open fun build(): LoggerConfig {
            return LoggerConfig(logFormat, methodOffset)
        }
    }

    /**
     * [LoggerConfig] 构建器
     *
     * Builder for [LoggerConfig]
     */
    open class Builder : BaseBuilder<Builder>()

    companion object {

        /**
         * DSL风格构建器
         *
         * DSL style builder
         *
         * @return [LoggerConfig]
         */
        inline fun build(block: Builder.() -> Unit = {}): LoggerConfig {
            return Builder().apply(block).build()
        }
    }
}

/**
 * DSL风格顶级函数，用于构建 [LoggerConfig]
 *
 * Top-level DSL function for building a [LoggerConfig].
 *
 * @return [LoggerConfig]
 */
inline fun loggerConfig(block: LoggerConfig.Builder.() -> Unit = {}): LoggerConfig {
    return LoggerConfig.Builder().apply(block).build()
}
