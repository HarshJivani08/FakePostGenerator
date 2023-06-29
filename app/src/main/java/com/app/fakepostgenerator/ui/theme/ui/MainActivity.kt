package com.app.fakepostgenerator.ui.theme.ui

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityMainBinding
import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.grewon.qmaker.ui.home.HomeFragment
import com.grewon.qmaker.ui.recent_design.RecentDesignFragment

class MainActivity : BaseActivity() {

    var TAG = "MainActivity"

    var binding: ActivityMainBinding? = null

//    lateinit var billingClient: BillingClient

    var MY_REQUEST_CODE = 100


    var deviceId: String? = null
    var dialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)


        addFragment(HomeFragment())
        binding?.root?.setBackgroundResource(R.drawable.bg_blue_canavs);

        initView()

        deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

    }



    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
//        if (appUpdateManager != null) {
//            appUpdateManager.unregisterListener(listener)
//        }
        super.onStop()
    }


    private fun initView() {
        binding?.bottomNavigtion?.itemIconTintList = null

        binding?.bottomNavigtion?.setOnNavigationItemSelectedListener { item ->
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            when (item.itemId) {
                R.id.nav_home -> {
                    if (fragment !is HomeFragment) replaceFragment(HomeFragment())
                    binding?.root?.setBackgroundResource(R.drawable.bg_blue_canavs);
                    true
                }
                R.id.nav_profile -> {
//                    if (fragment !is ProfileFragment) replaceFragment(ProfileFragment())
//                    binding?.root?.setBackgroundResource(R.drawable.bg_green_canvas);

                    if (fragment !is HomeFragment) replaceFragment(HomeFragment())
                    binding?.root?.setBackgroundResource(R.drawable.bg_blue_canavs);
                    true
                }
                R.id.nav_recent_design -> {
                    if (fragment !is RecentDesignFragment) replaceFragment(RecentDesignFragment())
                    binding?.root?.setBackgroundResource(R.drawable.bg_pink_canvas);
                    true
                }
                else -> false
            }
        }
    }

    fun setBottomSelected(position: Int) {
        for (item in 0 until binding?.bottomNavigtion?.menu?.size()!!) {
            if (item == position) {
                binding?.bottomNavigtion?.menu?.getItem(item)?.isChecked = true
            }
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is RecentDesignFragment) {
            if (fragment.onbackPress()) {
                return
            }
            replaceFragment(HomeFragment())
        }
//        else if (fragment is ProfileFragment) {
//            if (fragment.onbackPress()) {
//                return
//            }
//            replaceFragment(HomeFragment())
//        }
        else {
            finishAffinity()
        }
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }


}