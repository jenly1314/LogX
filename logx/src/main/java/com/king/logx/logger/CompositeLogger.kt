package com.king.logx.logger

import java.util.Collections

/**
 * [CompositeLogger]可以同时管理多个[Logger]，提供更多的扩展性。
 *
 * * 通过[addLogger]函数可以添加[Logger]
 * * 通过[removeLogger]函数可以移除已添加过的[Logger]
 * * 通过[removeAllLoggers]函数可以移除所有的[Logger]
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class CompositeLogger : Logger() {

    private val loggers = ArrayList<Logger>()

    @Volatile
    private var loggerArray = emptyArray<Logger>()

    override fun offset(methodOffset: Int): ILogger {
        loggerArray.forEach {
            it.offset(methodOffset)
        }
        return this
    }

    override fun tag(tag: String): ILogger {
        loggerArray.forEach {
            it.tag(tag)
        }
        return this
    }

    override fun v(message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.v(message, *args)
        }
    }

    override fun v(t: Throwable?, message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.v(t, message, *args)
        }
    }

    override fun v(t: Throwable?) {
        loggerArray.forEach {
            it.v(t)
        }
    }

    override fun d(message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.d(message, *args)
        }
    }

    override fun d(t: Throwable?, message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.d(t, message, *args)
        }
    }

    override fun d(t: Throwable?) {
        loggerArray.forEach {
            it.d(t)
        }
    }

    override fun i(message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.i(message, *args)
        }
    }

    override fun i(t: Throwable?, message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.i(t, message, *args)
        }
    }

    override fun i(t: Throwable?) {
        loggerArray.forEach {
            it.i(t)
        }
    }

    override fun w(message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.w(message, *args)
        }
    }

    override fun w(t: Throwable?, message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.w(t, message, *args)
        }
    }

    override fun w(t: Throwable?) {
        loggerArray.forEach {
            it.w(t)
        }
    }

    override fun e(message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.e(message, *args)
        }
    }

    override fun e(t: Throwable?, message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.e(t, message, *args)
        }
    }

    override fun e(t: Throwable?) {
        loggerArray.forEach {
            it.e(t)
        }
    }

    override fun wtf(message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.wtf(message, *args)
        }
    }

    override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
        loggerArray.forEach {
            it.wtf(t, message, *args)
        }
    }

    override fun wtf(t: Throwable?) {
        loggerArray.forEach {
            it.wtf(t)
        }
    }

    override fun log(priority: Int, message: String?) {
        loggerArray.forEach {
            it.log(priority, message)
        }
    }

    override fun log(priority: Int, t: Throwable?, message: String?) {
        loggerArray.forEach {
            it.log(priority, t, message)
        }
    }

    override fun log(priority: Int, t: Throwable?) {
        loggerArray.forEach {
            it.log(priority, t)
        }
    }

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        // Missing override for log method.
        throw AssertionError()
    }

    /**
     * Add new logger.
     */
    fun addLogger(logger: Logger) {
        require(logger !== this) { "Cannot add ${logger.javaClass.simpleName} into itself." }
        synchronized(loggers) {
            loggers.add(logger)
            loggerArray = loggers.toTypedArray()
        }
    }

    /**
     * Adds new loggers.
     */
    fun addLogger(vararg loggers: Logger) {
        if (loggers.isNotEmpty()) {
            for (logger in loggers) {
                require(logger !== this) { "Cannot add ${logger.javaClass.simpleName} into itself." }
            }
            synchronized(this.loggers) {
                Collections.addAll(this.loggers, *loggers)
                loggerArray = this.loggers.toTypedArray()
            }
        }
    }

    /**
     * Remove a added logger.
     */
    fun removeLogger(logger: Logger) {
        synchronized(loggers) {
            require(loggers.remove(logger)) { "Cannot remove logger which is not added: $logger" }
            loggerArray = loggers.toTypedArray()
        }
    }

    /**
     * Remove all added loggers.
     */
    fun removeAllLoggers() {
        synchronized(loggers) {
            loggers.clear()
            loggerArray = emptyArray()
        }
    }

    /**
     * Return the number of loggers
     */
    @get:[JvmName("loggerCount")]
    val loggerCount get() = loggerArray.size
}