package com.grewon.qmaker.ui.recent_design

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.fakepostgenerator.BuildConfig
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.FragmentRecentDesignBinding
import com.app.fakepostgenerator.ui.theme.app.BaseFragment
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.app.fakepostgenerator.ui.theme.dialog.DialogRecentPost
import com.app.fakepostgenerator.ui.theme.model.DataRecentPost
import com.app.fakepostgenerator.ui.theme.ui.MainActivity
import com.app.fakepostgenerator.ui.theme.ui.menu.MenuActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.grewon.qmaker.ui.recent_design.adapter.AdapterRecentPost
import com.tombayley.activitycircularreveal.CircularReveal
import java.io.File
import java.io.FilenameFilter
import java.util.*


class RecentDesignFragment : BaseFragment(), View.OnClickListener,
    AdapterRecentPost.OnPostClickListener, AdapterRecentPost.OnLongClickListerner {

//    var rewardedAd: RewardedAd? = null

    var button: Button? = null

    companion object {
        lateinit var objectList: ArrayList<DataRecentPost>
    }

    lateinit var binding: FragmentRecentDesignBinding

    var flagSelectAll = false

    private var arrayList: String? = null

    private var adapter: AdapterRecentPost? = null

    private var recentData = ArrayList<DataRecentPost>()

    private var isOpenShareDialog: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecentDesignBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initView()
        selectedItem()

//        if(!preferenceUtils.getIsPremium()) {
//
//            val mViewModel = ViewModelProvider(this)[SoundAdxViewModel::class.java]
//
//            mViewModel.status.observe(requireActivity()) { status ->
//                if (status == Status.DONE) {
//                   Monetize.loadReward(context, adxRewardCallback, true)
//                }
//            }
//
////            if (rewardedAd == null) {
////                Log.e("TAG", "The rewarded ad wasn't ready yet...")
////            } else {
////                rewardedAd!!.show(requireActivity()) {
////                    Log.e("TAG", "The user earned the reward.")
////                }
////            }
//        }

    }

    fun getDirectoryPaths(directory: String?): ArrayList<File> {
        val pathArray: ArrayList<File> = ArrayList()
        val file = File(directory)
        if (file.exists()) {
            val listfiles: Array<File> = file.listFiles(object : FilenameFilter {
                override fun accept(dir: File?, name: String): Boolean {
                    return name.endsWith(".png")
                }
            })

            pathArray.addAll(listfiles)
        }
        Collections.reverse(pathArray)
        return pathArray
    }

    private fun initView() {

        (activity as MainActivity).setBottomSelected(2)
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing
            setAdapter()
            binding.swipeRefresh.isRefreshing = false
        }
        setAdapter()
    }

    private fun setAdapter() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerRecentPost.layoutManager = layoutManager
        adapter = AdapterRecentPost(requireContext(), this, this)
        recentData = adapter?.beans!!

//      adapter!!.addFileList(getDirectoryPaths(Constant.FOLDER_PATH))

        try {
            objectList = Gson().fromJson(
                preferenceUtils.sharedPreferences.getString(
                    Constant.RECENT_POST,
                    ""
                ), object : TypeToken<List<DataRecentPost>>() {}.type
            )
        } catch (e: Exception) {
            objectList = arrayListOf()
        }

        val tempList: ArrayList<DataRecentPost> = ArrayList()

        for (index in 0 until objectList.size) {
            val file = File(objectList[index].postPath)
            if (file.exists()) {
                tempList.add(objectList[index])
            }
        }
        objectList = tempList
        preferenceUtils.saveRecentPost(Gson().toJson(objectList))
        objectList.reverse()
        adapter?.addFileList(objectList)

        val folderPath = File(Constant.FOLDER_PATH)

        binding.recyclerRecentPost.adapter = adapter

        if (adapter!!.itemCount == 0) {
            binding.viewAnimator.displayedChild = 0
        } else {
            binding.viewAnimator.displayedChild = 1
        }
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
    }

    override fun onPostClickListener(data: String, position: Int) {
        openDialog(data)
    }

    override fun onPostLongClickListener(data: String, position: Int) {
        binding.imgDelete.visibility = View.VISIBLE
    }


    private fun openDialog(data: String) {

        isOpenShareDialog = true
        val dialog = DialogRecentPost(requireActivity(), R.style.DialogThme, data)
        dialog.setImage(data)
        dialog.shareListener() {

//          sharePost(data)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        Constant.STORAGE_PERMISSION_REQUEST_CODE
                    )
                } else {
                    if (!preferenceUtils.getIsPremium()){

                        sharePost(File(data))

//                        if (rewardedAd == null) {
//
//                        } else {
//                            rewardedAd!!.show(
//                                requireActivity()
//                            ) { Log.e("TAG", "The user earned the reward.") }
//
//                            arrayList=data
//                        }

                    } else{
                        sharePost(File(data))
                    }


                }
            } /*else {

                if (!preferenceUtils.getIsPremium()){
                    if (rewardedAd == null) {
                        sharePost(File(data))
                    } else {
                        rewardedAd!!.show(
                            requireActivity()
                        ) { Log.e("TAG", "The user earned the reward.") }

                    }
                }else{
                    sharePost(File(data))
                }

            }*/
        }
        dialog.show()
        isOpenShareDialog = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constant.STORAGE_PERMISSION_REQUEST_CODE) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(requireActivity(),"User has allowed Pemissions", Toast.LENGTH_SHORT).show()
                sharePost(File(String()))
            } else {
                showAlert()
//          Toast.makeText(requireActivity(),"User denied Permissions", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.permission_denied))
            .setMessage(getString(R.string.allow_permission_from_app_setting))
            .setPositiveButton(getString(R.string.app_setting),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", activity?.getPackageName(), null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    showAlert()
                })
            .show()
    }

    private fun sharePost(data: File) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        val screenshotUri = FileProvider.getUriForFile(
            requireActivity(),
            requireActivity().packageName + ".provider",
            data
        )
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout This Amazing App at: \n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }

    private fun onClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.imgDelete.setOnClickListener(this)
        binding.createBtn.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgMenu -> {
                val builder = CircularReveal.Builder(
                    requireActivity(),
                    binding.imgMenu,
                    Intent(requireContext(), MenuActivity::class.java),
                    900
                ).apply {
                    revealColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_200
                    )
                }

                CircularReveal.presentActivity(builder)
            }

            R.id.imgDelete -> {
//                val data = adapter!!.getmainList()
//                val selectedData = adapter!!.getselectedList()
//                Log.e("TEMP", "onClick: " + data)
//
//                for (index in 0 until data.size) {
//                    if (selectedData.contains(data[index].postPath)) {
//                        val file = File(data[index].postPath)
//                        file.delete()
////                        temp.removeAt(index)
//                        adapter!!.removeAt(index)
//                    }
//                }
////                Log.e("TEMP", "onClick: " + data)
//
//
////                adapter!!.notifyDataSetChanged()
//                adapter!!.updateData(data)
//                preferenceUtils.saveRecentPost(Gson().toJson(data))
//
//                if (data.isNullOrEmpty()) {
//                    binding.viewAnimator.displayedChild = 0
//                } else {
//                    binding.viewAnimator.displayedChild = 1
//                }
                val data = adapter!!.getmainList()
                val selectedData = adapter!!.getselectedList()
                val newList = arrayListOf<DataRecentPost>()

                for ((Index, item) in data.withIndex()) {
                    if (selectedData.contains(item.postPath)) {
                        val file = File(item.postPath)
                        file.delete()
//                        temp.removeAt(index)

                    } else {
                        newList.add(item)
                    }
                }
                adapter!!.updateData(newList)
                preferenceUtils.saveRecentPost(Gson().toJson(data))

                onbackPress()


                if (newList.isNullOrEmpty()) {
                    binding.viewAnimator.displayedChild = 0
                } else {
                    binding.viewAnimator.displayedChild = 1
                }
            }

            R.id.create_btn -> {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
        }
    }

    fun onbackPress(): Boolean {
        binding.imgDelete.visibility = View.GONE
        if (adapter!!.getselection()) {
            adapter!!.onbackPress()
            return true
        } else {
            return false
        }
    }

    private fun selectedItem(): Boolean {
        if (adapter!!.getselection()) {
            adapter!!.onbackPress()
            return true
        } else {
            return false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun selectAll() {
        val data = adapter?.getmainList()
        for (i in 0 until data!!.size) {
            if (data.get(i).isSelected) {
                flagSelectAll = true
                adapter!!.selectAllItem(false)
                adapter!!.notifyDataSetChanged()
            } else {
                adapter!!.selectAllItem(true)
                adapter!!.notifyDataSetChanged()
            }
            break
        }
    }

}

/*
*  val data = adapter!!.getmainList()
                val selectedData = adapter!!.getselectedList()

                    for (index in 0 until data.size) {
                        if (selectedData.contains(data[index].postPath)) {
                            val file = File(data[index].postPath)
                            file.delete()
                        }
                    }

                try {
                    for (index in 0 until data.size) {
                        if (selectedData.contains(data[index].postPath)) {
                            data.removeAt(index)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                adapter!!.updateData(data)
                preferenceUtils.saveRecentPost(Gson().toJson(data))

                if (data  == null){
                    binding.viewAnimator.displayedChild = 0
                }else{
                    binding.viewAnimator.displayedChild = 1
                }
            }
*/