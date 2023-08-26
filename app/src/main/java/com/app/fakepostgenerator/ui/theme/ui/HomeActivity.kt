package com.app.fakepostgenerator.ui.theme.ui

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityHomeBinding

import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.app.fakepostgenerator.ui.theme.app.QMakerApp
import com.app.fakepostgenerator.ui.theme.ui.menu.MenuActivity
import com.app.fakepostgenerator.ui.theme.ui.whatsapp.WhatsappChatActivity
import com.tombayley.activitycircularreveal.CircularReveal

class HomeActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        initView()
        setClick()
    }

    private fun setClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.lInstagramPost.setOnClickListener(this)
        binding.lFacebookPost.setOnClickListener(this)
        binding.lWhatsAppChat.setOnClickListener(this)
        binding.lTweeterPost.setOnClickListener(this)
    }

    private fun initView() {

    }

    override fun onClick(v: View?) {
        when (v){
            binding.imgMenu -> {

                val builder = CircularReveal.Builder(
                    this,
                    binding.imgMenu,
                    Intent(this, MenuActivity::class.java),
                    1600
                ).apply {
                    revealColor = ContextCompat.getColor(
                        applicationContext,
                        R.color.light_sky
                    )
                }
                CircularReveal.presentActivity(builder)
//                QMakerApp.getAppInstance().
                setAnimation(this,binding.imgMenu)
            }
            binding.lInstagramPost -> {
                val intent = Intent(this, InstagramPostActivity::class.java)
                startActivity(intent)
                setAnimation(this,binding.lInstagramPost)
            }
            binding.lFacebookPost -> {
                val intent = Intent(this, FacebookPostActivity::class.java)
                startActivity(intent)
                setAnimation(this,binding.lFacebookPost)
            }
            binding.lTweeterPost -> {
                val intent = Intent(this, TweeterPostActivity::class.java)
                startActivity(intent)
                setAnimation(this,binding.lTweeterPost)
            }
           binding.lWhatsAppChat -> {
               val intent = Intent(this, WhatsappChatActivity::class.java)
               startActivity(intent)
               setAnimation(this,binding.lWhatsAppChat)
            }

        }
    }
    fun setAnimation(context: Context?, view: View) {
//        view.setAnimation(AnimationUtils.loadAnimation(context,R.anim.bounce));
        val clickAnimation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        view.startAnimation(clickAnimation)
    }

}