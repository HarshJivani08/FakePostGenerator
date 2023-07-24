package com.app.fakepostgenerator.ui.theme.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.fakepostgenerator.ui.theme.utils.PreferenceUtils

open class BaseActivity : AppCompatActivity() {

    var preferenceUtils: PreferenceUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceUtils = PreferenceUtils(getSharedPreferences("QMakerPreference", MODE_PRIVATE))
    }

    fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

}