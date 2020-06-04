package com.deleteapps.systemother

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class BroadCastUninstall : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}