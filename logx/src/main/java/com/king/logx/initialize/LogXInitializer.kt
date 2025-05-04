package com.king.logx.initialize

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.startup.Initializer
import com.king.logx.LogX
import com.king.logx.logger.ILogger

/**
 * LogX初始化
 *
 * LogX initialization.
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class LogXInitializer : Initializer<ILogger> {

    override fun create(context: Context): ILogger {
        LogX.isDebug = isDebuggable(context)
        return LogX.Companion
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    /**
     * 检查是否为调试模式
     *
     * Check if debug mode is enabled.
     */
    private fun isDebuggable(context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

}
