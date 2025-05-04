package com.king.logx.util

import android.content.Context
import android.os.Build
import com.king.logx.LogX
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter


/**
 * 工具类（仅供内部使用）
 *
 * Utility class.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
internal class Utils private constructor() {

    init {
        throw AssertionError("No instances")
    }

    companion object {
        private const val MAX_TAG_LENGTH = 23  // Android 7.0以下最大合法TAG长度

        /**
         * 从Throwable获取可记录的堆栈轨迹字符串
         *
         * Handy function to get a loggable stack trace from a Throwable
         *
         * @param t 需要转换的异常对象
         * @return 完整的堆栈轨迹字符串
         */
        fun getStackTraceString(t: Throwable): String {
            // 注意：不能替换为Log.getStackTraceString()，因其会隐藏UnknownHostException
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        /**
         * 从堆栈元素提取适合作为日志`TAG`的字符串（默认使用：简单的类名）
         *
         * Extract the tag which should be used for the message from the `element`. By default
         * this will use the simple class name.
         */
        fun createStackElementTag(element: StackTraceElement): String {
            val tag = element.className.substringAfterLast('.').substringBefore('$')
            return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
                tag
            } else {
                tag.substring(0, MAX_TAG_LENGTH)  // Android 7.0以下TAG长度限制23字符
            }
        }

        /**
         * 根据优先级获取日志等级字符
         *
         * Gets log level character by priority.
         */
        fun getLogLevel(priority: Int) = when (priority) {
            LogX.VERBOSE -> "V"
            LogX.DEBUG -> "D"
            LogX.INFO -> "I"
            LogX.WARN -> "W"
            LogX.ERROR -> "E"
            LogX.ASSERT -> "A"
            else -> priority.toString()
        }

        /**
         * 估算字符的UTF-8编码字节大小
         *
         * Estimates the UTF-8 encoded byte size of a character.
         */
        fun Char.utf8ByteSize(): Int {
            return when (this.code) {
                in 0..0x7F -> 1 // ASCII
                in 0x80..0x7FF -> 2 // 双字节字符
                else -> 3 // 其他字符（包括中文、代理区等）
            }
        }

        /**
         * 计算字符串的UTF-8编码字节长度
         *
         * Calculates the total UTF-8 encoded byte size of the string.
         */
        fun String.utf8ByteSize(): Long {
            var bytes = 0L
            for (char in this) {
                bytes += char.utf8ByteSize()
            }
            return bytes
        }

        /**
         * 获取缓存文件夹
         */
        fun getCacheFileDir(context: Context, childDir: String): File {
            return context.getExternalFilesDir(childDir) ?: context.filesDir.resolve(childDir)
        }
    }
}
