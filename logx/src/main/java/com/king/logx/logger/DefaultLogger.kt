package com.king.logx.logger

import android.util.Log
import com.king.logx.LogX
import com.king.logx.util.Utils

/**
 * 默认实现的[Logger]，既美观又实用。
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
 * @param showThreadInfo Whether to show thread info or not. Default true
 * @param methodCount How many method line to show. Default 2
 * @param methodOffset Hides internal method calls up to offset.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 *
 */
open class DefaultLogger @JvmOverloads constructor(
    private val showThreadInfo: Boolean = true,
    private val methodCount: Int = 2,
    private val methodOffset: Int = 0
) : Logger(methodOffset) {

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return LogX.isDebug
    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [LogX] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message.
     * @param t Accompanying exceptions. May be `null`.
     */
    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        var logMessage = message.toString()
        if (message.isNullOrEmpty()) {
            if (t != null) {
                logMessage = Utils.getStackTraceString(t)
            }
        } else if (t != null) {
            logMessage += "\n" + Utils.getStackTraceString(t)
        }

        logTopBorder(priority, tag)
        logHeaderContent(priority, tag, methodCount, lastOffset)

        // get bytes of message with system's default charset (which is UTF-8 for Android)
        val bytes = logMessage.toByteArray()
        val length = bytes.size
        if (length <= MAX_LOG_LENGTH) {
            if (methodCount > 0) {
                logDivider(priority, tag)
            }
            logContent(priority, tag, logMessage)
            logBottomBorder(priority, tag)
            return
        }
        if (methodCount > 0) {
            logDivider(priority, tag)
        }
        var i = 0
        while (i < length) {
            val count = (length - i).coerceAtMost(MAX_LOG_LENGTH)
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(priority, tag, String(bytes, i, count))
            i += MAX_LOG_LENGTH
        }
        logBottomBorder(priority, tag)
    }

    private fun logTopBorder(priority: Int, tag: String?) {
        println(priority, tag, TOP_BORDER)
    }

    private fun logHeaderContent(priority: Int, tag: String?, methodCount: Int, methodOffset: Int) {
        if (showThreadInfo) {
            println(priority, tag, "$HORIZONTAL_LINE Thread: ${Thread.currentThread().name}")
            logDivider(priority, tag)
        }
        val stackTrace = getStackTrace()
        var level = ""
        val stackOffset = getStackOffset(stackTrace) + methodOffset
        var showMethodCount = methodCount

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (showMethodCount + stackOffset > stackTrace.size) {
            showMethodCount = stackTrace.size - stackOffset - 1
        }
        for (i in showMethodCount downTo 1) {
            val stackIndex = i + stackOffset
            if (stackIndex >= stackTrace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append(HORIZONTAL_LINE)
                .append(' ')
                .append(level)
                .append(getSimpleClassName(stackTrace[stackIndex].className))
                .append(".")
                .append(stackTrace[stackIndex].methodName)
                .append("(")
                .append(stackTrace[stackIndex].fileName)
                .append(":")
                .append(stackTrace[stackIndex].lineNumber)
                .append(")")
            level += INDENT
            println(priority, tag, builder.toString())
        }
    }

    private fun getSimpleClassName(name: String): String {
        return name.substringAfterLast('.')
    }

    private fun logBottomBorder(priority: Int, tag: String?) {
        println(priority, tag, BOTTOM_BORDER)
    }

    private fun logDivider(priority: Int, tag: String?) {
        println(priority, tag, MIDDLE_BORDER)
    }

    private fun logContent(priority: Int, tag: String?, chunk: String) {
        chunk.split(System.lineSeparator().toRegex()).dropLastWhile { it.isEmpty() }.forEach {
            println(priority, tag, "$HORIZONTAL_LINE $it")
        }
    }

    /**
     * Write a log message to its destination.
     *
     * @param priority Log level. See [LogX] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message log message.
     */
    protected open fun println(priority: Int, tag: String?, message: String) {
        Log.println(priority, tag, message)
    }

}