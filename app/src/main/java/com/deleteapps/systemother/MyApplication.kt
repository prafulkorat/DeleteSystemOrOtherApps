package com.deleteapps.systemother

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instace = this
    }

    companion object {
        var instace: MyApplication? = null
            private set
    }
}