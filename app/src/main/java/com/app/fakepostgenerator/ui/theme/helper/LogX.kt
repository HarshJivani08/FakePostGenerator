package com.app.fakepostgenerator.ui.theme.helper

import android.util.Log
import android.os.Build
import com.app.fakepostgenerator.BuildConfig


class LogX {

    companion object {

        fun E(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun D(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun V(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun W(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

    }
}