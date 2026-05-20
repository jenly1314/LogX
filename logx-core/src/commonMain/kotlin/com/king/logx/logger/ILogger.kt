package com.king.logx.logger

/**
 * 日志记录器的基本定义
 *
 * Basic definition of a logger.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
interface ILogger {

    /**
     * 为下次日志调用设置一次性日志显示格式
     *
     * Set a one-time log display format for use on the next logging call.
     */
    fun format(logFormat: LogFormat): ILogger

    /**
     * 为下次日志调用设置一次性方法跟踪偏移量
     *
     * Set a one-time method trace offset for use on the next logging call.
     */
    fun offset(methodOffset: Int): ILogger

    /**
     * 为下次日志调用设置一次性标签
     *
     * Set a one-time tag for use on the next logging call.
     */
    fun tag(tag: String): ILogger

    /**
     * 记录详细级别日志消息（可包含格式参数）
     *
     * Log a verbose message with optional format args.
     */
    fun v(message: String?, vararg args: Any?)

    /**
     * 记录详细级别异常和消息（可包含格式参数）
     *
     * Log a verbose exception and a message with optional format args.
     */
    fun v(t: Throwable?, message: String?, vararg args: Any?)

    /**
     * 记录详细级别异常
     *
     * Log a verbose exception.
     */
    fun v(t: Throwable?)

    /**
     * 记录调试级别日志消息（可包含格式参数）
     *
     * Log a debug message with optional format args.
     */
    fun d(message: String?, vararg args: Any?)

    /**
     * 记录调试级别异常和消息（可包含格式参数）
     *
     * Log a debug exception and a message with optional format args.
     */
    fun d(t: Throwable?, message: String?, vararg args: Any?)

    /**
     * 记录调试级别异常
     *
     * Log a debug exception.
     */
    fun d(t: Throwable?)

    /**
     * 记录信息级别日志消息（可包含格式参数）
     *
     * Log an info message with optional format args.
     */
    fun i(message: String?, vararg args: Any?)

    /**
     * 记录信息级别异常和消息（可包含格式参数）
     *
     * Log an info exception and a message with optional format args.
     */
    fun i(t: Throwable?, message: String?, vararg args: Any?)

    /**
     * 记录信息级别异常
     *
     * Log an info exception.
     */
    fun i(t: Throwable?)

    /**
     * 记录警告级别日志消息（可包含格式参数）
     *
     * Log a warning message with optional format args.
     */
    fun w(message: String?, vararg args: Any?)

    /**
     * 记录警告级别异常和消息（可包含格式参数）
     *
     * Log a warning exception and a message with optional format args.
     */
    fun w(t: Throwable?, message: String?, vararg args: Any?)

    /**
     * 记录警告级别异常
     *
     * Log a warning exception.
     */
    fun w(t: Throwable?)

    /**
     * 记录错误级别日志消息（可包含格式参数）
     *
     * Log an error message with optional format args.
     */
    fun e(message: String?, vararg args: Any?)

    /**
     * 记录错误级别异常和消息（可包含格式参数）
     *
     * Log an error exception and a message with optional format args.
     */
    fun e(t: Throwable?, message: String?, vararg args: Any?)

    /**
     * 记录错误级别异常
     *
     * Log an error exception.
     */
    fun e(t: Throwable?)

    /**
     * 记录断言级别日志消息（可包含格式参数）
     *
     * Log an assert message with optional format args.
     */
    fun wtf(message: String?, vararg args: Any?)

    /**
     * 记录断言级别异常和消息（可包含格式参数）
     *
     * Log an assert exception and a message with optional format args.
     */
    fun wtf(t: Throwable?, message: String?, vararg args: Any?)

    /**
     * 记录断言级别异常
     *
     * Log an assert exception.
     */
    fun wtf(t: Throwable?)

    /**
     * 按指定优先级记录日志消息
     *
     * Log at `priority` a message.
     */
    fun log(priority: Int, message: String?)

    /**
     * 按指定优先级记录异常和消息
     *
     * Log at `priority` an exception and a message.
     */
    fun log(priority: Int, t: Throwable?, message: String?)

    /**
     * 按指定优先级记录异常
     *
     * Log at `priority` an exception.
     */
    fun log(priority: Int, t: Throwable?)
}

/**
 * 日志显示格式
 *
 * Log display format.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
enum class LogFormat {

    /**
     * 美化格式：带结构化分隔线的日志排版，提升可读性，便于开发调试时快速定位问题
     *
     * Pretty: Features structured layout with visual separators for better readability and efficient debugging.
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
     */
    PRETTY,

    /**
     * 普通格式：与Android默认日志格式一致
     *
     * Plain: Matches default Android logging format.
     *
     * Default log output format:
     *
     * ```
     * Original log message
     * ```
     */
    PLAIN;
}
