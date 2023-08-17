package com.frstudio.bilibilivideomanagerpro

import android.app.Application
import android.content.SharedPreferences
import com.frstudio.bilibilivideomanagerpro.crash.CrashHandler
import de.mindpipe.android.logging.log4j.LogConfigurator
import java.io.File

lateinit var sharedPreferences: SharedPreferences
lateinit var app: App
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        configLogger()
        configCrash()
        sharedPreferences = getSharedPreferences("APP", MODE_PRIVATE)
    }
    private fun configLogger() {
        // 日志配置设置
        val logConfigurator = LogConfigurator()
        // 配置日志文件路径
        logConfigurator.fileName = File(getExternalFilesDir("Log"),"ComposeMusicCopy.log").toString()
        logConfigurator.configure()
    }
    private fun configCrash() {
        CrashHandler.init(this)
    }
}