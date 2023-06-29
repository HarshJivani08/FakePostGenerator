package com.app.fakepostgenerator.ui.theme.ui.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import com.app.fakepostgenerator.databinding.ActivityTermsConditionBinding
import com.app.fakepostgenerator.ui.theme.app.Constant


class TermsConditionActivity : AppCompatActivity() {

    lateinit var binding: ActivityTermsConditionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsConditionBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)


        binding.webTerms.settings.javaScriptEnabled = true
        binding.webTerms.loadUrl(Constant.TERMS_CONDITION)


        binding.imgCancel.setOnClickListener{
            onBackPressed()
        }

    }
}