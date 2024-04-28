package com.king.logx.logger

import com.king.logx.LogX
import com.king.logx.util.Utils

/**
 * 日志记录器基类；封装好一些通用的逻辑，从而简化其子类的实现。
 *
 * @param methodOffset Hides internal method calls up to offset.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
abstract class Logger @JvmOverloads constructor(private val methodOffset: Int = 0) : ILogger {

    @get:JvmSynthetic // Hide from public API.
    private val explicitOffset = ThreadLocal<Int>()

    @get:JvmSynthetic // Hide from public API.
    private val offset: Int
        get() {
            val onceOnlyOffset = explicitOffset.get()
            if (onceOnlyOffset != null) {
                explicitOffset.remove()
                return onceOnlyOffset
            }
            return methodOffset
        }

    @Volatile
    internal var lastOffset = methodOffset

    @get:JvmSynthetic // Hide from public API.
    private val explicitTag = ThreadLocal<String>()

    @get:JvmSynthetic
    private val tag: String?
        get() {
            val onceOnlyTag = explicitTag.get()
            if (onceOnlyTag != null) {
                explicitTag.remove()
                return onceOnlyTag
            }
            return getStackTrace().let {
                it.getOrNull(getStackOffset(it) + 1 + lastOffset)
            }?.let { Utils.createStackElementTag(it) }
        }

    /** Set a one-time method trace offset for use on the next logging call. */
    override fun offset(methodOffset: Int): ILogger {
        explicitOffset.set(methodOffset)
        return this
    }

    /** Set a one-time tag for use on the next logging call. */
    override fun tag(tag: String): ILogger {
        explicitTag.set(tag)
        return this
    }

    /** Log a verbose message with optional format args. */
    override fun v(message: String?, vararg args: Any?) {
        prepareLog(LogX.VERBOSE, null, message, *args)
    }

    /** Log a verbose exception and a message with optional format args. */
    override fun v(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.VERBOSE, t, message, *args)
    }

    /** Log a verbose exception. */
    override fun v(t: Throwable?) {
        prepareLog(LogX.VERBOSE, t, null)
    }

    /** Log a debug message with optional format args. */
    override fun d(message: String?, vararg args: Any?) {
        prepareLog(LogX.DEBUG, null, message, *args)
    }

    /** Log a debug exception and a message with optional format args. */
    override fun d(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.DEBUG, t, message, *args)
    }

    /** Log a debug exception. */
    override fun d(t: Throwable?) {
        prepareLog(LogX.DEBUG, t, null)
    }

    /** Log an info message with optional format args. */
    override fun i(message: String?, vararg args: Any?) {
        prepareLog(LogX.INFO, null, message, *args)
    }

    /** Log an info exception and a message with optional format args. */
    override fun i(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.INFO, t, message, *args)
    }

    /** Log an info exception. */
    override fun i(t: Throwable?) {
        prepareLog(LogX.INFO, t, null)
    }

    /** Log a warning message with optional format args. */
    override fun w(message: String?, vararg args: Any?) {
        prepareLog(LogX.WARN, null, message, *args)
    }

    /** Log a warning exception and a message with optional format args. */
    override fun w(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.WARN, t, message, *args)
    }

    /** Log a warning exception. */
    override fun w(t: Throwable?) {
        prepareLog(LogX.WARN, t, null)
    }

    /** Log an error message with optional format args. */
    override fun e(message: String?, vararg args: Any?) {
        prepareLog(LogX.ERROR, null, message, *args)
    }

    /** Log an error exception and a message with optional format args. */
    override fun e(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.ERROR, t, message, *args)
    }

    /** Log an error exception. */
    override fun e(t: Throwable?) {
        prepareLog(LogX.ERROR, t, null)
    }

    /** Log an assert message with optional format args. */
    override fun wtf(message: String?, vararg args: Any?) {
        prepareLog(LogX.ASSERT, null, message, *args)
    }

    /** Log an assert exception and a message with optional format args. */
    override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.ASSERT, t, message, *args)
    }

    /** Log an assert exception. */
    override fun wtf(t: Throwable?) {
        prepareLog(LogX.ASSERT, t, null)
    }

    /** Log at `priority` an message. */
    override fun log(priority: Int, message: String?) {
        prepareLog(priority, null, message)
    }

    /** Log at `priority` an exception and a message. */
    override fun log(priority: Int, t: Throwable?, message: String?) {
        prepareLog(priority, t, message)
    }

    /** Log at `priority` an exception. */
    override fun log(priority: Int, t: Throwable?) {
        prepareLog(priority, t, null)
    }

    /** Return whether a message at `priority` or `tag` should be logged. */
    protected open fun isLoggable(priority: Int, tag: String?) = true

    @Synchronized
    private fun prepareLog(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        lastOffset = offset
        val onceOnlyTag = tag
        if (!isLoggable(priority, onceOnlyTag)) {
            return
        }
        log(priority, onceOnlyTag, formatMessage(message, args), t)
    }

    /** Formats a log message with optional arguments. */
    protected open fun formatMessage(message: String?, args: Array<out Any?>): String? {
        return message?.format(*args)
    }

    /**
     * Provides programmatic access to the stack trace information printed
     */
    protected open fun getStackTrace(): Array<StackTraceElement> = Throwable().stackTrace

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    protected fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val className = trace[i].className
            if (className !in LogX.internalIgnore) {
                return --i
            }
            i++
        }
        return -1
    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [LogX] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message.
     * @param t Accompanying exceptions. May be `null`.
     */
    abstract fun log(priority: Int, tag: String?, message: String?, t: Throwable?)

    internal companion object {
        private const val TOP_LEFT_CORNER = '┌'
        private const val BOTTOM_LEFT_CORNER = '└'
        private const val MIDDLE_CORNER = '├'
        const val HORIZONTAL_LINE = '│'
        private const val DOUBLE_DIVIDER = "─────────────────────────────────────────────────"
        private const val SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
        const val TOP_BORDER = "$TOP_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
        const val BOTTOM_BORDER = "$BOTTOM_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
        const val MIDDLE_BORDER = "$MIDDLE_CORNER$SINGLE_DIVIDER$SINGLE_DIVIDER"
        const val MAX_LOG_LENGTH = 4000
        const val MIN_STACK_OFFSET = 5
        const val INDENT = "    "
    }
}