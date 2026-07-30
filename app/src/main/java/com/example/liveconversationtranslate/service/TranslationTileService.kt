package com.example.liveconversationtranslate.service

import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import com.example.liveconversationtranslate.MainActivity

class TranslationTileService : TileService() {

    override fun onClick() {
        super.onClick()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("START_TRANSLATION", true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(android.app.PendingIntent.getActivity(
                this,
                100,
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or
                        android.app.PendingIntent.FLAG_IMMUTABLE
            ))
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }
}