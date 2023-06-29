package com.app.fakepostgenerator.ui.theme.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.view.View
import com.grewon.qmaker.utils.PreferenceUtils

class QMakerApp : Application() {

    private val view: View? = null

    companion object {

        lateinit var instance: QMakerApp

        var isOpenInFailed:Boolean=false

        var preferenceUtils: PreferenceUtils? = null

        var PLAY_STORE_BASE = "https://play.google.com/store/apps/details?id="

        fun getAppInstance(): QMakerApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        sdkInitialize(instance)
//        AppEventsLogger.activateApp(this)
        preferenceUtils = PreferenceUtils(getSharedPreferences("QMakerPreference", MODE_PRIVATE))
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "INFO_NOTIFICATION"
            val description = "INFO_NOTIFICATION"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("INFO_NOTIFICATION", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun Dp2px(dp: Float): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}