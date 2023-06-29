package com.app.fakepostgenerator.ui.theme.ui.menu

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.fakepostgenerator.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        try {
            val pInfo: PackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0)
            val version = pInfo.versionName
            binding.txtVersion.setText(Html.fromHtml("<u>Version $version</u>"))
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        binding.imgCancel.setOnClickListener{
            onBackPressed()
        }


    }
}