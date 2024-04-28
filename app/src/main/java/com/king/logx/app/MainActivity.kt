package com.king.logx.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.king.logx.LogX
import com.king.logx.app.databinding.ActivityMainBinding

/**
 * LogX 示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        LogX.v("verbose")
        LogX.d("debug")
        LogX.i("info")
        LogX.w("warn")
        LogX.e("error")
        LogX.tag("LogX").d("hello %s", "world")

    }

}