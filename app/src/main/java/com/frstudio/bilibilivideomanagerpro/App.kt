package com.frstudio.bilibilivideomanagerpro

import android.app.Application
import android.content.SharedPreferences

lateinit var sharedPreferences: SharedPreferences
lateinit var app: App
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        sharedPreferences = getSharedPreferences("APP", MODE_PRIVATE)
    }
}