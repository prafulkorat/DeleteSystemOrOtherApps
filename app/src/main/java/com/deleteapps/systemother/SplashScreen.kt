package com.deleteapps.systemother

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    var SPLASH_TIME_OUT = 2000
    var h = Handler()
    var isActivityIsVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar!!.hide()
        if (VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        SPLASH_TIME_OUT = 3000
    }

    override fun onResume() {
        super.onResume()
        isActivityIsVisible = true
        h.postDelayed({ gotoNext() }, SPLASH_TIME_OUT.toLong())
    }

    private fun gotoNext() {
        val i = Intent(this@SplashScreen, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onPause() {
        super.onPause()
        h.removeCallbacksAndMessages(null)
        isActivityIsVisible = false
    }

    override fun onBackPressed() {}
    override fun onDestroy() {
        super.onDestroy()
    }
}