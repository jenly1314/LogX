package com.king.logx.logger

import com.king.logx.LogX
import com.king.logx.logger.config.LoggerConfig
import com.king.logx.util.Utils

/**
 * 日志记录器抽象基类，封装通用逻辑以简化子类实现
 *
 * Abstract logger base class encapsulating common logic to simplify subclass implementations.
 *
 * @param config Logger configuration.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
abstract class Logger(config: LoggerConfig) : ILogger {

    /**
     * 构造
     *
     * @param logFormat Log display format, defaults to [LogFormat.PRETTY] for better readability
     * @param methodOffset Stack trace offset (hides internal method calls)
     */
    @JvmOverloads
    constructor(logFormat: LogFormat = LogFormat.PRETTY, methodOffset: Int = 0) : this(
        config = LoggerConfig.build {
            this.logFormat = logFormat
            this.methodOffset = methodOffset
        }
    )

    private val logFormat = config.logFormat
    private val methodOffset = config.methodOffset

    @get:JvmSynthetic // Hide from public API.
    private val explicitLogFormat = ThreadLocal<LogFormat>()

    @get:JvmSynthetic // Hide from public API.
    private val format: LogFormat
        get() {
            val onceOnlyLogMode = explicitLogFormat.get()
            if (onceOnlyLogMode != null) {
                explicitLogFormat.remove()
                return onceOnlyLogMode
            }
            return logFormat
        }

    @Volatile
    var lastLogFormat = logFormat
        internal set

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
    var lastOffset = methodOffset
        internal set

    @get:JvmSynthetic // Hide from public API.
    private val explicitTag = ThreadLocal<String>()

    @get:JvmSynthetic // Hide from public API.
    private val tag: String?
        get() {
            val onceOnlyTag = explicitTag.get()
            if (onceOnlyTag != null) {
                explicitTag.remove()
                return onceOnlyTag
            }
            return getStackTrace().let {
                it.getOrNull(getStackOffset(it) + lastOffset)
            }?.let { Utils.createStackElementTag(it) }
        }

    override fun format(logFormat: LogFormat): ILogger {
        explicitLogFormat.set(logFormat)
        return this
    }

    override fun offset(methodOffset: Int): ILogger {
        explicitOffset.set(methodOffset)
        return this
    }

    override fun tag(tag: String): ILogger {
        explicitTag.set(tag)
        return this
    }

    override fun v(message: String?, vararg args: Any?) {
        prepareLog(LogX.VERBOSE, null, message, *args)
    }

    override fun v(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.VERBOSE, t, message, *args)
    }

    override fun v(t: Throwable?) {
        prepareLog(LogX.VERBOSE, t, null)
    }

    override fun d(message: String?, vararg args: Any?) {
        prepareLog(LogX.DEBUG, null, message, *args)
    }

    override fun d(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.DEBUG, t, message, *args)
    }

    override fun d(t: Throwable?) {
        prepareLog(LogX.DEBUG, t, null)
    }

    override fun i(message: String?, vararg args: Any?) {
        prepareLog(LogX.INFO, null, message, *args)
    }

    override fun i(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.INFO, t, message, *args)
    }

    override fun i(t: Throwable?) {
        prepareLog(LogX.INFO, t, null)
    }

    override fun w(message: String?, vararg args: Any?) {
        prepareLog(LogX.WARN, null, message, *args)
    }

    override fun w(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.WARN, t, message, *args)
    }

    override fun w(t: Throwable?) {
        prepareLog(LogX.WARN, t, null)
    }

    override fun e(message: String?, vararg args: Any?) {
        prepareLog(LogX.ERROR, null, message, *args)
    }

    override fun e(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.ERROR, t, message, *args)
    }

    override fun e(t: Throwable?) {
        prepareLog(LogX.ERROR, t, null)
    }

    override fun wtf(message: String?, vararg args: Any?) {
        prepareLog(LogX.ASSERT, null, message, *args)
    }

    override fun wtf(t: Throwable?, message: String?, vararg args: Any?) {
        prepareLog(LogX.ASSERT, t, message, *args)
    }

    override fun wtf(t: Throwable?) {
        prepareLog(LogX.ASSERT, t, null)
    }

    override fun log(priority: Int, message: String?) {
        prepareLog(priority, null, message)
    }

    override fun log(priority: Int, t: Throwable?, message: String?) {
        prepareLog(priority, t, message)
    }

    override fun log(priority: Int, t: Throwable?) {
        prepareLog(priority, t, null)
    }

    /**
     * 判断指定`priority`或`tag`的日志是否应该被记录
     *
     * Return whether a message at `priority` or `tag` should be logged.
     */
    protected open fun isLoggable(priority: Int, tag: String?) = LogX.isDebug

    @Synchronized
    private fun prepareLog(priority: Int, t: Throwable?, message: String?, vararg args: Any?) {
        lastOffset = offset
        lastLogFormat = format
        val onceOnlyTag = tag
        if (!isLoggable(priority, onceOnlyTag)) {
            return
        }
        val logMessage = if (args.isNotEmpty()) formatMessage(message, args) else message
        log(priority, onceOnlyTag, logMessage, t)
    }

    /**
     * 格式化带参数的日志消息
     *
     * Formats a log message with optional arguments.
     */
    protected open fun formatMessage(message: String?, args: Array<out Any?>): String? {
        return message?.format(*args)
    }

    /**
     * 获取当前调用堆栈信息（从调用点开始）
     *
     * Provides programmatic access to the stack trace information printed
     */
    protected open fun getStackTrace(): Array<StackTraceElement> = Throwable().stackTrace

    /**
     * 确定堆栈跟踪的起始索引（跳过本类内部方法调用）
     *
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    protected fun getStackOffset(trace: Array<StackTraceElement>): Int {
        for (i in MIN_STACK_OFFSET until trace.size) {
            if (!LogX.internalIgnore.contains(trace[i].className)) return i
        }
        return 0
    }

    /**
     * 将日志消息写入目标输出（默认由所有级别特定的方法调用）
     *
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [LogX] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message.
     * @param t Accompanying exceptions. May be `null`.
     */
    protected abstract fun log(priority: Int, tag: String?, message: String?, t: Throwable?)

    internal companion object {
        private const val TOP_LEFT_CORNER = '┌'
        private const val BOTTOM_LEFT_CORNER = '└'
        private const val MIDDLE_CORNER = '├'
        const val HORIZONTAL_LINE = '│'
        private const val DOUBLE_DIVIDER = "──────────────────────────────────────────────────"
        private const val SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
        const val TOP_BORDER = "$TOP_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
        const val BOTTOM_BORDER = "$BOTTOM_LEFT_CORNER$DOUBLE_DIVIDER$DOUBLE_DIVIDER"
        const val MIDDLE_BORDER = "$MIDDLE_CORNER$SINGLE_DIVIDER$SINGLE_DIVIDER"
        const val MAX_LOG_BYTES = 4000
        const val SIMPLE_LOG_MAX_CHARS = MAX_LOG_BYTES / 3
        const val MIN_STACK_OFFSET = 5
        const val TRACE_LINE_CAPACITY = 128
        const val INDENT = "    "
    }
}
