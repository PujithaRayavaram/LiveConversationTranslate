package com.example.liveconversationtranslate.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SpeechService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        android.util.Log.d("SpeechService", "Service Created")
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        android.util.Log.d("SpeechService", "Service Started")

        return START_STICKY
    }
}