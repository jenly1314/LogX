package com.king.logx

import com.king.logx.logger.CompositeLogger
import com.king.logx.logger.DefaultLogger
import com.king.logx.logger.ILogger
import com.king.logx.logger.LogFormat
import com.king.logx.logger.Logger

/**
 * LogX —— 轻量而强大的日志框架。
 *
 * LogX —— A lightweight yet powerful logging framework.
 *
 * - 兼具 **Timber** 的优雅易用与高度扩展性，同时拥有 **Logger** 般精美的日志格式化输出。
 *
 * - Combines **Timber**'s elegance and extensibility with **Logger**-style beautiful formatting.
 *
 * - 直接使用，无需初始化，默认配置即是最推荐的配置。
 *
 * - Ready to use without initialization - default configuration is the recommended setup.
 *
 * Default log output format:
 *
 * ```
 *  ┌──────────────────────────────
 *  │ Thread information
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Method stack history
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Log message
 *  └──────────────────────────────
 * ```
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class LogX private constructor() {

    init {
        throw AssertionError()
    }

    companion object : ILogger {

        /**
         * println方法的优先级常量；使用 Log.v
         *
         * Priority constant for the println method; use Log.v.
         */
        const val VERBOSE = 2

        /**
         * println方法的优先级常量；使用 Log.d
         *
         * Priority constant for the println method; use Log.d.
         */
        const val DEBUG = 3

        /**
         * println方法的优先级常量；使用 Log.i
         *
         * Priority constant for the println method; use Log.i.
         */
        const val INFO = 4

        /**
         * println方法的优先级常量；使用 Log.w
         *
         * Priority constant for the println method; use Log.w.
         */
        const val WARN = 5

        /**
         * println方法的优先级常量；使用 Log.e
         *
         * Priority constant for the println method; use Log.e.
         */
        const val ERROR = 6

        /**
         * println方法的优先级常量；使用 Log.wtf
         *
         * Priority constant for the println method; use Log.wtf.
         */
        const val ASSERT = 7

        /**
         * 默认根据调试模式动态赋值（true = 调试模式，false = 发布模式）
         *
         * Default value is dynamically assigned based on debug mode (true = debug mode, false = release mode)
         */
        @get:JvmSynthetic
        internal var isDebug = true

        @get:JvmSynthetic
        internal val internalIgnore = setOf<String>(
            LogX::class.java.name,
            Companion::class.java.name,
            DefaultLogger::class.java.name,
            CompositeLogger::class.java.name,
            Logger::class.java.name,
            ILogger::class.java.name,
        )

        /**
         * 当前使用的日志记录器（默认：[DefaultLogger]）
         *
         * Currently active logger (default: [DefaultLogger])
         */
        @JvmStatic
        private var logger: Logger = DefaultLogger()

        /**
         * 设置日志记录器
         *
         * Set up a logger.
         */
        @JvmStatic
        fun setLogger(logger: Logger) {
            this.logger = logger
        }

        /**
         * 为下次日志调用设置一次性日志显示格式
         *
         * Set a one-time log display format for use on the next logging call.
         */
        @JvmStatic
        override fun format(logFormat: LogFormat): ILogger {
            logger.format(logFormat)
            return this
        }

        /**
         * 为下次日志调用设置一次性方法跟踪偏移量
         *
         * Set a one-time method trace offset for use on the next logging call.
         */
        @JvmStatic
        override fun offset(methodOffset: Int): ILogger {
            logger.offset(methodOffset)
            return this
        }

        /**
         * 为下次日志调用设置一次性标签
         *
         * Set a one-time tag for use on the next logging call.
         */
        @JvmStatic
        override fun tag(tag: String): ILogger {
            logger.tag(tag)
            return this
        }

        /**
         * 记录详细级别日志消息（可包含格式参数）
         *
         * Log a verbose message with optional format args.
         */
        @JvmStatic
        override fun v(message: String?, vararg args: Any?) {
            logger.v(message, *args)
        }

        /**
         * 记录详细级别异常和消息（可包含格式参数）
         *
         * Log a verbose exception and a message with optional format args.
         */
        @JvmStatic
        override fun v(t: Throwable?, message: String?, vararg args: Any?) {
            logger.v(t, message, *args)
        }

        /**
         * 记录详细级别异常
         *
         * Log a verbose exception.
         */
        @JvmStatic
        override fun v(t: Throwable?) {
            logger.v(t)
        }

        /**
         * 记录调试级别日志消息（可包含格式参数）
         *
         * Log a debug message with optional format args.
         */
        @JvmStatic
        override fun d(message: String?, vararg args: Any?) {
            logger.d(message, *args)
        }

        /**
         * 记录调试级别异常和消息（可包含格式参数）
         *
         * Log a debug exception and a message with optional format args.
         */
        @JvmStatic
        override fun d(t: Throwable?, message: String?, vararg args: Any?) {
            logger.d(t, message, *args)
        }

        /**
         * 记录调试级别异常
         *
         * Log a debug exception.
         */
        @JvmStatic
        override fun d(t: Throwable?) {
            logger.d(t)
        }

        /**
         * 记录信息级别日志消息（可包含格式参数）
         *
         * Log an info message with optional format args.
         */
        @JvmStatic
        override fun i(message: String?, vararg args: Any?) {
            logger.i(message, *args)
        }

        /**
         * 记录信息级别异常和消息（可包含格式参数）
         *
         * Log an info exception and a message with optional format args.
         */
        @JvmStatic
        override fun i(t: Throwable?, message: String?, vararg args: Any?) {
            logger.i(t, message, *args)
        }

        /**
         * 记录信息级别异常
         *
         * Log an info exception.
         */
        @JvmStatic
        override fun i(t: Throwable?) {
            logger.i(t)
        }

        /**
         * 记录警告级别日志消息（可包含格式参数）
         *
         * Log a warning message with optional format args.
         */
        @JvmStatic
        override fun w(message: String?, vararg args: Any?) {
            logger.w(message, *args)
        }

        /**
         * 记录警告级别异常和消息（可包含格式参数）
         *
         * Log a warning exception and a message with optional format args.
         */
        @JvmStatic
        override fun w(t: Throwable?, message: String?, vararg args: Any?) {
            logger.w(t, message, *args)
        }

        /**
         * 记录警告级别异常
         *
         * Log a warning exception.
         */
        @JvmStatic
        override fun w(t: Throwable?) {
            logger.w(t)
        }

        /**
         * 记录错误级别日志消息（可包含格式参数）
         *
         * Log an error message with optional format args.
         */
        @JvmStatic
        override fun e(message: String?, vararg args: Any?) {
            logger.e(message, *args)
        }

        /**
         * 记录错误级别异常和消息（可包含格式参数）
         *
         * Log an error exception and a message with optional format args.
         */
        @JvmStatic
        override fun e(t: Throwable?, message: String?, vararg args: Any?) {
            logger.e(t, message, *args)
        }

        /**
         * 记录错误级别异常
         *
         * Log an error exception.
         */
        @JvmStatic
        override fun e(t: Throwable?) {
            logger.e(t)
        }

        /**
         * 记录断言级别日志消息（可包含格式参数）
         *
         * Log an assert message with optional format args.
         */
        @JvmStatic
        override fun wtf(message: String?, vararg args: Any?) {
            logger.wtf(message, *args)
        }

        /**
         * 记录断言级别异常和消息（可包含格式参数）
         *
         * Log an assert exception and a message with optional format args.
         */
        @JvmStatic
        override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
            logger.wtf(t, message, *args)
        }

        /**
         * 记录断言级别异常
         *
         * Log an assert exception.
         */
        @JvmStatic
        override fun wtf(t: Throwable?) {
            logger.wtf(t)
        }

        /**
         * 按指定优先级记录日志消息
         *
         * Log at `priority` a message.
         */
        @JvmStatic
        override fun log(priority: Int, message: String?) {
            logger.log(priority, message)
        }

        /**
         * 按指定优先级记录异常和消息
         *
         * Log at `priority` an exception and a message.
         */
        @JvmStatic
        override fun log(priority: Int, t: Throwable?, message: String?) {
            logger.log(priority, t, message)
        }

        /**
         * 按指定优先级记录异常
         *
         * Log at `priority` an exception.
         */
        @JvmStatic
        override fun log(priority: Int, t: Throwable?) {
            logger.log(priority, t)
        }
    }
}
