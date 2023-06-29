package com.grewon.qmaker.ui.menu

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.fakepostgenerator.databinding.ActivityPrivacyPolicyBinding
import com.app.fakepostgenerator.ui.theme.app.Constant

class PrivacyPolicyActivity : AppCompatActivity() {


    lateinit var binding: ActivityPrivacyPolicyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)


        binding.webPrivacy.settings.javaScriptEnabled = true
        binding.webPrivacy.loadUrl(Constant.PRIVACY_POLICY)



        binding.imgCancel.setOnClickListener {
            onBackPressed()
        }
    }
}