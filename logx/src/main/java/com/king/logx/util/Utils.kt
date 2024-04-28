package com.king.logx.util

import android.os.Build
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 工具类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
internal class Utils private constructor() {

    init {
        throw AssertionError()
    }

    companion object {
        private const val MAX_TAG_LENGTH = 23

        /**
         * Handy function to get a loggable stack trace from a Throwable
         */
        fun getStackTraceString(t: Throwable): String {
            // Don't replace this with Log.getStackTraceString() - it hides
            // UnknownHostException, which is not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        /**
         * Extract the tag which should be used for the message from the `element`. By default
         * this will use the simple class name.
         */
        fun createStackElementTag(element: StackTraceElement): String {
            // Remove the package name
            var tag = element.className.substringAfterLast('.')
            // Remove inner class information
            val lastDollarIndex = tag.indexOf('$')
            if (lastDollarIndex != -1) {
                tag = tag.substring(0, lastDollarIndex)
            }

            return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 26) {
                tag
            } else {
                tag.substring(0, MAX_TAG_LENGTH)
            }
        }
    }
}