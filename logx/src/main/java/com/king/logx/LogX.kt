package com.king.logx

import com.king.logx.logger.DefaultLogger
import com.king.logx.logger.ILogger
import com.king.logx.logger.Logger
import com.king.logx.logger.CompositeLogger

/**
 * LogX 小而美的日志框架。
 *
 * * [LogX] 既有 **Timber** 的易用性与可扩展性，又具备 **Logger** 的日志格式美观性。
 * * 直接使用，无需初始化，默认配置即是最推荐的配置。
 *
 *
 * 日志的默认输出格式如下：
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
         * Priority constant for the println method; use Log.v.
         */
        const val VERBOSE = 2

        /**
         * Priority constant for the println method; use Log.d.
         */
        const val DEBUG = 3

        /**
         * Priority constant for the println method; use Log.i.
         */
        const val INFO = 4

        /**
         * Priority constant for the println method; use Log.w.
         */
        const val WARN = 5

        /**
         * Priority constant for the println method; use Log.e.
         */
        const val ERROR = 6

        /**
         * Priority constant for the println method.use Log.wtf.
         */
        const val ASSERT = 7

        @get:JvmSynthetic
        internal var isDebug = true

        @get:JvmSynthetic
        internal val internalIgnore = listOf<String>(
            LogX::class.java.name,
            LogX.Companion::class.java.name,
            ILogger::class.java.name,
            DefaultLogger::class.java.name,
            CompositeLogger::class.java.name,
            Logger::class.java.name,
        )

        /**
         * logger
         */
        @JvmStatic
        private var logger: Logger = DefaultLogger()

        /** Set up a logger. */
        @JvmStatic
        fun setLogger(logger: Logger) {
            this.logger = logger
        }

        /** Set a one-time method trace offset for use on the next logging call. */
        @JvmStatic
        override fun offset(methodOffset: Int): ILogger {
            logger.offset(methodOffset)
            return this
        }

        /** Set a one-time tag for use on the next logging call. */
        @JvmStatic
        override fun tag(tag: String): ILogger {
            logger.tag(tag)
            return this
        }

        /** Log a verbose message with optional format args. */
        @JvmStatic
        override fun v(message: String?, vararg args: Any?) {
            logger.v(message, *args)
        }

        /** Log a verbose exception and a message with optional format args. */
        @JvmStatic
        override fun v(t: Throwable?, message: String?, vararg args: Any?) {
            logger.v(t, message, *args)
        }

        /** Log a verbose exception. */
        @JvmStatic
        override fun v(t: Throwable?) {
            logger.v(t)
        }

        /** Log a debug message with optional format args. */
        @JvmStatic
        override fun d(message: String?, vararg args: Any?) {
            logger.d(message, *args)
        }

        /** Log a debug exception and a message with optional format args. */
        @JvmStatic
        override fun d(t: Throwable?, message: String?, vararg args: Any?) {
            logger.d(t, message, *args)
        }

        /** Log a debug exception. */
        @JvmStatic
        override fun d(t: Throwable?) {
            logger.d(t)
        }

        /** Log an info message with optional format args. */
        @JvmStatic
        override fun i(message: String?, vararg args: Any?) {
            logger.i(message, *args)
        }

        /** Log an info exception and a message with optional format args. */
        @JvmStatic
        override fun i(t: Throwable?, message: String?, vararg args: Any?) {
            logger.i(t, message, *args)
        }

        /** Log an info exception. */
        @JvmStatic
        override fun i(t: Throwable?) {
            logger.i(t)
        }

        /** Log a warning message with optional format args. */
        @JvmStatic
        override fun w(message: String?, vararg args: Any?) {
            logger.w(message, *args)
        }

        /** Log a warning exception and a message with optional format args. */
        @JvmStatic
        override fun w(t: Throwable?, message: String?, vararg args: Any?) {
            logger.w(t, message, *args)
        }

        /** Log a warning exception. */
        @JvmStatic
        override fun w(t: Throwable?) {
            logger.w(t)
        }

        /** Log an error message with optional format args. */
        @JvmStatic
        override fun e(message: String?, vararg args: Any?) {
            logger.e(message, *args)
        }

        /** Log an error exception and a message with optional format args. */
        @JvmStatic
        override fun e(t: Throwable?, message: String?, vararg args: Any?) {
            logger.e(t, message, *args)
        }

        /** Log an error exception. */
        @JvmStatic
        override fun e(t: Throwable?) {
            logger.e(t)
        }

        /** Log an assert message with optional format args. */
        @JvmStatic
        override fun wtf(message: String?, vararg args: Any?) {
            logger.wtf(message, *args)
        }

        /** Log an assert exception and a message with optional format args. */
        @JvmStatic
        override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
            logger.wtf(t, message, *args)
        }

        /** Log an assert exception. */
        @JvmStatic
        override fun wtf(t: Throwable?) {
            logger.wtf(t)
        }

        /** Log at `priority` an message. */
        @JvmStatic
        override fun log(priority: Int, message: String?) {
            logger.log(priority, message)
        }

        /** Log at `priority` an exception and a message. */
        @JvmStatic
        override fun log(priority: Int, t: Throwable?, message: String?) {
            logger.log(priority, message)
        }

        /** Log at `priority` an exception. */
        @JvmStatic
        override fun log(priority: Int, t: Throwable?) {
            logger.log(priority, t)
        }
    }
}
