package com.app.fakepostgenerator.ui.theme.ui.menu

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fakepostgenerator.BuildConfig
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityMenuBinding
import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.app.fakepostgenerator.ui.theme.model.DataMenu
import com.app.fakepostgenerator.ui.theme.ui.menu.adapter.AdapterMenuItem
import com.app.fakepostgenerator.ui.theme.utils.ListUtils
import com.tombayley.activitycircularreveal.CircularReveal


class MenuActivity : BaseActivity(), View.OnClickListener,
    AdapterMenuItem.OnMenuClickListener {

    lateinit var binding: ActivityMenuBinding
    protected lateinit var activityCircularReveal: CircularReveal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

       // activityCircularReveal = CircularReveal(view)
       // activityCircularReveal.onActivityCreate(intent)

//      setupWindowAnimations();
        initView()
        setCLick()
    }

    private fun initView() {
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerMenu.layoutManager = layoutManager
        val adapter = AdapterMenuItem(applicationContext, this)
        adapter.addMenuList(ListUtils.addMenuList())
        binding.recyclerMenu.adapter = adapter
    }

    private fun setCLick() {
        binding.imgCancel.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgCancel -> {
                onBackPressed()
               // activityCircularReveal.unRevealActivity(this)
            }
        }
    }

    override fun onBackPressed() {
         super.onBackPressed()
       // activityCircularReveal.unRevealActivity(this)
    }

    override fun onMenuItemClick(data: DataMenu, position: Int) {
        when (position) {
            0 -> {
                //get premium
               /* launchInApp()*/
//                val intent =Intent(applicationContext,PremiumActivity::class.java)
//                startActivity(intent)
            }
            1 -> {
                //terms & condition
                val intent = Intent(applicationContext, TermsConditionActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                //privacy policy
                val intent = Intent(applicationContext, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }
            3 -> {
               //FAQ's
//               val intent = Intent(applicationContext,FaqActivity::class.java)
//               startActivity(intent)
            }
            4 -> {
                //about us
                val intent = Intent(applicationContext, AboutActivity::class.java)
                startActivity(intent)
            }
            5 -> {
                //contact us
                startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${Constant.EMAIL}")))
            }
            6 -> {

                //share App
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    var shareMessage = "\nLet me recommend you this application\n\n"
                    shareMessage =
                        """
                       ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                        """.trimIndent()
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }
            }

            7 -> {
                // rate us
                val uri: Uri = Uri.parse("market://details?id=$packageName")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=$packageName")

                        )
                    )
                }
            }
            8 ->{
//                val intent =Intent(applicationContext,AppListActivity::class.java)
//                startActivity(intent)
            }


        }

    }



}