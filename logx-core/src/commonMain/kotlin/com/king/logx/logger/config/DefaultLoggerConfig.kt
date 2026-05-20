package com.king.logx.logger.config

import com.king.logx.logger.LogFormat

/**
 * 默认日志记录器配置
 *
 * Default logger configuration
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
     * [DefaultLoggerConfig] 构建器基类，支持泛型自引用以保证子类链式调用返回正确类型
     *
     * Base builder for [DefaultLoggerConfig] with self-referential generic for fluent subclass chaining.
     */
    open class BaseBuilder<SELF : BaseBuilder<SELF>> : LoggerConfig.BaseBuilder<SELF>() {

        /**
         * 是否显示线程信息，默认：true
         *
         * Whether to show thread info. Default true
         */
        var showThreadInfo: Boolean = true

        /**
         * 要显示的调用栈方法行数，默认：2
         *
         * How many method line to show. Default 2
         */
        var methodCount: Int = 2

        /**
         * 设置是否显示线程信息
         *
         * Set whether to show thread info.
         *
         * @param showThreadInfo Whether to show thread info or not. Default true
         */
        fun setShowThreadInfo(showThreadInfo: Boolean): SELF {
            this.showThreadInfo = showThreadInfo
            return self()
        }

        /**
         * 设置要显示的调用栈方法行数
         *
         * Sets the number of method lines to show.
         *
         * @param methodCount How many method line to show. Default 2
         */
        fun setMethodCount(methodCount: Int): SELF {
            this.methodCount = methodCount
            return self()
        }

        /**
         * 构建 [DefaultLoggerConfig]
         */
        override fun build(): DefaultLoggerConfig {
            return DefaultLoggerConfig(logFormat, showThreadInfo, methodCount, methodOffset)
        }
    }

    /**
     * [DefaultLoggerConfig] 构建器
     *
     * Builder for [DefaultLoggerConfig]
     */
    open class Builder : BaseBuilder<Builder>()

    companion object {

        /**
         * DSL风格构建器
         *
         * DSL style builder
         *
         * @return [DefaultLoggerConfig]
         */
        inline fun build(block: Builder.() -> Unit = {}): DefaultLoggerConfig {
            return Builder().apply(block).build()
        }
    }
}

/**
 * DSL风格顶级函数，用于构建 [DefaultLoggerConfig]
 *
 * Top-level DSL function for building a [DefaultLoggerConfig].
 *
 * @return [DefaultLoggerConfig]
 */
inline fun defaultLoggerConfig(block: DefaultLoggerConfig.Builder.() -> Unit = {}): DefaultLoggerConfig {
    return DefaultLoggerConfig.Builder().apply(block).build()
}
