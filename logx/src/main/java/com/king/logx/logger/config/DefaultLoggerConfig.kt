package com.king.logx.logger.config

import com.king.logx.logger.DefaultLogger
import com.king.logx.logger.LogFormat

/**
 * 日志记录器配置；适用于 [DefaultLogger]
 *
 * Logger configuration for [DefaultLogger]
 *
 * @param logFormat Log display format.
 * @param showThreadInfo Whether to show thread info.
 * @param methodCount How many method line to show.
 * @param methodOffset Stack trace offset (hides internal method calls)
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class DefaultLoggerConfig protected constructor(
    logFormat: LogFormat,
    val showThreadInfo: Boolean,
    val methodCount: Int,
    methodOffset: Int,
) : LoggerConfig(logFormat, methodOffset) {

    /**
     * [DefaultLoggerConfig] 构建器
     *
     * Builder for [DefaultLoggerConfig]
     */
    open class Builder : LoggerConfig.Builder() {

        /**
         * 是否显示线程信息，默认：true
         *
         * Whether to show thread info. Default true
         */
        @set:JvmSynthetic
        var showThreadInfo: Boolean = true

        /**
         * 要显示的调用栈方法行数，默认：2
         *
         * How many method line to show. Default 2
         */
        @set:JvmSynthetic
        var methodCount: Int = 2

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
        open fun setShowThreadInfo(showThreadInfo: Boolean): Builder {
            this.showThreadInfo = showThreadInfo
            return this
        }

        /**
         * 设置要显示的调用栈方法行数
         *
         * Sets the number of method lines to show.
         *
         * @param methodCount How many method line to show. Default 2
         */
        open fun setMethodCount(methodCount: Int): Builder {
            this.methodCount = methodCount
            return this
        }

        /**
         * 构建 [DefaultLoggerConfig]
         */
        override fun build(): DefaultLoggerConfig {
            return DefaultLoggerConfig(logFormat, showThreadInfo, methodCount, methodOffset)
        }
    }

    companion object {

        /**
         * DSL风格构建器
         *
         * DSL style builder
         *
         * @return [DefaultLoggerConfig]
         */
        @JvmSynthetic
        inline fun build(block: Builder.() -> Unit = {}): DefaultLoggerConfig {
            return Builder().apply(block).build()
        }
    }
}
