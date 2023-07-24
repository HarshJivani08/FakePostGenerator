package com.app.fakepostgenerator.ui.theme.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityFacebookPostBinding
import com.app.fakepostgenerator.databinding.LayoutFacebookFieldDialogBinding
import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.app.fakepostgenerator.ui.theme.dialog.SimpleImagePickerBottomDialog
import com.app.fakepostgenerator.ui.theme.model.DataRecentPost
import com.app.fakepostgenerator.ui.theme.utils.DateUtils
import com.app.fakepostgenerator.ui.theme.utils.prettyCount
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grewon.qmaker.ui.recent_design.RecentDesignFragment
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FacebookPostActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityFacebookPostBinding
    lateinit var editBinding: LayoutFacebookFieldDialogBinding
    private var bottomDialog: BottomSheetDialog? = null
    lateinit var dateUtils: DateUtils
    private var dateTime: String? = null
    private var date: String? = null
    private var time: String? = null
    private var commentCount: Int = 0
    private var likeCount: Int = 123
    private var isEdit: Boolean = false
    private var isViewMode: Boolean? = false
    private var imageList: ArrayList<String>? = ArrayList()
    private var reactionCount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacebookPostBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        dateUtils = DateUtils(applicationContext)

        setClick()
        setPreData()
    }

    private fun setPreData() {
        if (preferenceUtils?.getUser() != null) {
            binding.txtUserName.setText(preferenceUtils!!.getUser()!!.name)
            if (!preferenceUtils?.getUser()!!.image.isNullOrEmpty()) {
                Glide.with(this@FacebookPostActivity).load(preferenceUtils?.getUser()!!.image).circleCrop().into(binding.imgUser)
            }
        } else {
            binding.txtUserName.setText("Anna Marina")
        }
        commentCount = 10
        binding.txtCommentCount.text = "$commentCount comments"
        binding.txtLikeCount.text = likeCount.toString()
    }

    private fun setClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgUser.setOnClickListener(this)
        binding.rlImage.setOnClickListener(this)
        binding.imgEditUserName.setOnClickListener(this)
        binding.imgEditTime.setOnClickListener(this)
        binding.imgEditDesc.setOnClickListener(this)
        binding.imgEditLikeCount.setOnClickListener(this)
        binding.lLike.setOnClickListener(this)
        binding.lDesc.setOnClickListener(this)
        binding.txtDesc.setOnClickListener(this)
        binding.lTime.setOnClickListener(this)
        binding.lComment.setOnClickListener(this)
        binding.imgEditCommentCount.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.imgLike.setOnClickListener(this)
        binding.txtCheckLike.setOnClickListener(this)
        binding.txtCheckHaha.setOnClickListener(this)
        binding.txtCheckLove.setOnClickListener(this)
        binding.txtCheckWow.setOnClickListener(this)
        binding.txtCheckLike.setOnClickListener(this)
        binding.txtCheckSad.setOnClickListener(this)
        binding.txtCheckAngry.setOnClickListener(this)


        binding.rdGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rdEdit -> {
                        isViewMode = false
                        visibleEditicon()
                    }
                    R.id.rdView -> {
                        isViewMode = true
                        hideEditIcon()
                    }
                    // add more cases here to handle other buttons in the your RadioGroup
                }
                isEdit = true
            }
        }
        binding.rgPost.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rdText -> {
                        binding.rlImage.visibility = View.GONE
                    }
                    R.id.rdPhoto -> {
                        binding.rlImage.visibility = View.VISIBLE
                        binding.imgPlay.visibility = View.GONE
                    }
                    R.id.rdVideo -> {
                        binding.rlImage.visibility = View.VISIBLE
                        binding.imgPlay.visibility = View.VISIBLE
                    }
                }
            }
            isEdit = true
        }
        binding.rgVisible.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rdPublic -> {
                        binding.imgVisibleFor.setImageResource(R.drawable.ic_fb_public)
                    }
                    R.id.rdFriends -> {
                        binding.imgVisibleFor.setImageResource(R.drawable.ic_fb_friend)
                    }
                }
            }
            isEdit = true
        }
        binding.checkboxVerify.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.imgVerified.visibility = View.VISIBLE
            } else {
                binding.imgVerified.visibility = View.GONE
            }
            isEdit = true
        })
        setReaction()
    }

    private fun setReaction() {
        binding.checkHaha.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (reactionCount >= 3) {
                    binding.checkHaha.isChecked = false
                } else {
                    reactionCount += 1
                    binding.imgHaha.visibility = View.VISIBLE
                }
            } else {
                if (reactionCount == 1) {
                    binding.checkHaha.isChecked = true
                } else {
                    reactionCount -= 1
                    binding.imgHaha.visibility = View.GONE
                }
            }
            isEdit = true
        })

        binding.checkAngry.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (reactionCount >= 3) {
                    binding.checkAngry.isChecked = false
                } else {
                    binding.imgAngry.visibility = View.VISIBLE
                    reactionCount += 1
                }
            } else {
                if (reactionCount == 1) {
                    binding.checkAngry.isChecked = true
                } else {
                    binding.imgAngry.visibility = View.GONE
                    reactionCount -= 1
                }
            }
            isEdit = true
        })

        binding.checkLike.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (reactionCount >= 3) {
                    binding.checkLike.isChecked = false
                } else {
                    binding.imgLikeCount.visibility = View.VISIBLE
                    reactionCount += 1
                }
            } else {
                if (reactionCount == 1) {
                    binding.checkLike.isChecked = true
                } else {
                    binding.imgLikeCount.visibility = View.GONE
                    reactionCount -= 1
                }

            }
            isEdit = true
        })

        binding.checkSmile.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (reactionCount >= 3) {
                    binding.checkSmile.isChecked = false
                } else {
                    binding.imgLove.visibility = View.VISIBLE
                    reactionCount += 1
                }
            } else {
                if (reactionCount == 1) {
                    binding.checkSmile.isChecked = true
                } else {
                    binding.imgLove.visibility = View.GONE
                    reactionCount -= 1
                }
            }
            isEdit = true
        })

        binding.checkWow.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (reactionCount >= 3) {
                    binding.checkWow.isChecked = false
                } else {
                    binding.imgWow.visibility = View.VISIBLE
                    reactionCount += 1
                }
            } else {
                if (reactionCount == 1) {
                    binding.checkWow.isChecked = true
                } else {
                    binding.imgWow.visibility = View.GONE
                    reactionCount -= 1
                }
            }
            isEdit = true
        })

        binding.checkSad.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (reactionCount >= 3) {
                    binding.checkSad.isChecked = false
                } else {
                    binding.imgSad.visibility = View.VISIBLE
                    reactionCount += 1
                }
            } else {
                if (reactionCount == 1) {
                    binding.checkSad.isChecked = true
                } else {
                    binding.imgSad.visibility = View.GONE
                    reactionCount -= 1
                }
            }
            isEdit = true
        })
    }

    private fun countRections() {
        if (reactionCount < 3) {
            binding.checkHaha.isEnabled = true
            binding.checkSmile.isEnabled = true
            binding.checkWow.isEnabled = true
            binding.checkSad.isEnabled = true
            binding.checkAngry.isEnabled = true
            binding.checkLike.isEnabled = true
        } else {
            binding.checkHaha.isEnabled = false
            binding.checkSmile.isEnabled = true
            binding.checkWow.isEnabled = true
            binding.checkSad.isEnabled = true
            binding.checkAngry.isEnabled = true
            binding.checkLike.isEnabled = true
        }
    }

    private fun hideEditIcon() {
        binding.imgEditUserName.visibility = View.GONE
        binding.imgEditTime.visibility = View.GONE
        binding.imgEditDesc.visibility = View.GONE
        binding.imgEditLikeCount.visibility = View.GONE
        binding.imgEditCommentCount.visibility = View.GONE
        binding.llEdit.visibility = View.GONE
    }

    private fun visibleEditicon() {
        binding.imgEditUserName.visibility = View.VISIBLE
        binding.imgEditTime.visibility = View.VISIBLE
        binding.imgEditDesc.visibility = View.VISIBLE
        binding.imgEditLikeCount.visibility = View.VISIBLE
        binding.imgEditCommentCount.visibility = View.VISIBLE
        binding.llEdit.visibility = View.VISIBLE
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.imgBack -> {
                onBackPressedDispatcher.onBackPressed()
            }

            binding.imgUser -> {
                if (isViewMode == false) {
                    val dialog = SimpleImagePickerBottomDialog()
                    dialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {

                        override fun onImageSelected(image: String) {
                            Log.e("INSTA_USER_IMAGE", "onImageSelected: " + image)
                            dialog.dismiss()
                            Glide.with(this@FacebookPostActivity).load(image).circleCrop().into(binding.imgUser)
                            binding.imgUser.tag = image
                            isEdit = true
                        }
                    })
                    dialog.show(supportFragmentManager, "ImagePicker")
                }
            }

//            binding.imgPost -> {
//                if (isViewMode == false) {
//                    val dialog = SimpleImagePickerBottomDialog()
//                    dialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {
//
//                        override fun onImageSelected(image: String) {
//                            Log.e("INSTA_USER_IMAGE", "onImageSelected: " + image)
//                            dialog.dismiss()
//                            Glide.with(this@FacebookPostActivity).load(image).into(binding.imgPost)
//                            binding.imgPost.tag = image
//                            isEdit = true
//                        }
//                    })
//                    dialog.show(supportFragmentManager, "ImagePicker")
//                }
//            }
            binding.rlImage ->{
                if (isViewMode == false) {
                    selectPhoto()
                }
            }

            binding.imgLike -> {
                if (binding.imgLike.isSelected) {
                    binding.imgLike.isSelected = false
                    binding.txtLikeCount.text = likeCount.toString()
                    binding.txtLikeCount.text = prettyCount(likeCount.toString())
                } else {
                    binding.imgLike.isSelected = true
                    binding.txtLikeCount.text = "you and " + prettyCount(likeCount.toString()) + " Others"
                }
            }

            binding.lLike -> {
                initEditPostDialog("LIKE_COUNT")
            }

            binding.imgEditUserName -> {
                initEditPostDialog("USER_NAME")
            }

            binding.lUserName -> {
                initEditPostDialog("USER_NAME")
            }

            binding.imgEditTime -> {
                initEditPostDialog("TIME")
            }

            binding.lTime -> {
                initEditPostDialog("TIME")
            }

            binding.imgEditDesc -> {
                initEditPostDialog("DESC")
            }

            binding.lDesc -> {
                initEditPostDialog("DESC")
            }

            binding.txtDesc -> {
                initEditPostDialog("DESC")
            }

            binding.imgEditLikeCount -> {
                initEditPostDialog("LIKE_COUNT")
            }

            binding.imgEditCommentCount -> {
                initEditPostDialog("COMMENT_COUNT")
            }

            binding.lComment -> {
                initEditPostDialog("COMMENT_COUNT")
            }

            binding.btnShare -> {
                if (isViewMode == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                                applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_DENIED
                        ) {
                            requestPermissions(
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                                ), Constant.STORAGE_PERMISSION_REQUEST_CODE
                            )
                        } else {
                            shareImage()
                        }
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                this, Manifest.permission.READ_MEDIA_IMAGES
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            shareImage()
                        } else {
                            galleryPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    } else {
                        shareImage()
                    }
                } else {
                    Toast.makeText(applicationContext, getString(R.string.enable_view_mode), Toast.LENGTH_SHORT).show()
                }
            }

            binding.txtCheckLike -> {
                binding.checkLike.performClick()
            }

            binding.txtCheckSad -> {
                binding.checkSad.performClick()
            }

            binding.txtCheckAngry -> {
                binding.checkAngry.performClick()
            }

            binding.txtCheckWow -> {
                binding.checkWow.performClick()
            }

            binding.txtCheckHaha -> {
                binding.checkHaha.performClick()
            }

            binding.txtCheckLove -> {
                binding.checkSmile.performClick()
            }
        }
    }

    private fun initEditPostDialog(tag: String) {
        bottomDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        editBinding = LayoutFacebookFieldDialogBinding.inflate(layoutInflater)
        bottomDialog?.setContentView(editBinding.root)

        when (tag) {
            "USER_NAME" -> {
                editBinding.etUserName.setText(binding.txtUserName.text)
                editBinding.lUserName.visibility = View.VISIBLE
            }
            "TIME" -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
                val currentDate = sdf.format(Date())

                editBinding.etPostDate.setText(dateUtils.convertDateFormat(currentDate.toString(), "dd/MM/yyyy HH:mm a", "dd/MM/yyyy"))
                val time = dateUtils.convertDateFormat(currentDate.toString(), "dd/MM/yyyy HH:mm a", "HH:mm a")
                val timeComeFromServer = dateUtils.convert24to12Time(time).replace("PM", "pm").replace("AM", "am");
                editBinding.etPostTime.setText(timeComeFromServer)
                editBinding.lPostDateTime.visibility = View.VISIBLE
            }
            "DESC" -> {
                editBinding.lDesc.visibility = View.VISIBLE
                editBinding.etDesc.setText(binding.txtDesc.text)
            }
            "LIKE_COUNT" -> {
                editBinding.lLikeCount.visibility = View.VISIBLE
                editBinding.etLikeCount.setText(likeCount.toString())
            }
            "COMMENT_COUNT" -> {
                editBinding.lCommentCount.visibility = View.VISIBLE
                editBinding.etCommentCount.setText(commentCount.toString())
            }
        }
        editBinding.etPostDate.setOnClickListener {
            dateUtils.showDatePickerDialog(this, editBinding.etPostDate)
        }
        editBinding.etPostTime.setOnClickListener {
            dateUtils.showTimePickerDialogForText(this, editBinding.etPostTime)
        }

        editBinding.btnDone.setOnClickListener {
            getData(tag, bottomDialog!!)
        }

        if (!bottomDialog!!.isShowing) {
            bottomDialog!!.show()
        }
    }

    private fun getData(tag: String, bottomDialog: BottomSheetDialog) {
        when (tag) {
            "USER_NAME" -> {
                if (!editBinding.etUserName.text.isNullOrEmpty()) {
                    binding.txtUserName.text = editBinding.etUserName.text
                    bottomDialog.dismiss()
                    isEdit = true
                } else {
                    Toast.makeText(this, getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show()
                }

            }
            "LIKE_COUNT" -> {
                if (editBinding.etLikeCount.text.isNullOrEmpty() || editBinding.etLikeCount.text.toString() == "0") {
                    binding.imgLikeCount.visibility = View.GONE
                    binding.txtLikeCount.visibility = View.GONE
                } else {
                    binding.imgLikeCount.visibility = View.VISIBLE
                    binding.txtLikeCount.visibility = View.VISIBLE
                    if (binding.imgLike.isSelected) {
//                        binding.imgPost.isSelected = false
                        val count: String = editBinding.etLikeCount.text.toString()
                        likeCount = count.toInt()
                        binding.txtLikeCount.text = "you and " + prettyCount(likeCount.toString()) + " Others"
                    } else {
//                      binding.imgPost.isSelected = true
                        val count: String = editBinding.etLikeCount.text.toString()
                        likeCount = count.toInt()
                        binding.txtLikeCount.text = prettyCount(likeCount.toString())
                    }
                }
                isEdit = true
                bottomDialog.dismiss()
            }
            "COMMENT_COUNT" -> {
                if (editBinding.etCommentCount.text.isNullOrEmpty() || editBinding.etCommentCount.text.toString() == "0") {
                    binding.txtCommentCount.visibility = View.GONE
                } else {
                    binding.txtCommentCount.visibility = View.VISIBLE
                    val count: String = editBinding.etCommentCount.text.toString()
                    commentCount = count.toInt()
                    binding.txtCommentCount.text = "${prettyCount(commentCount.toString())} comments"
                }
                bottomDialog.dismiss()
                isEdit = true
            }
            "TIME" -> {
                if (editBinding.etPostDate.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, getString(R.string.enter_date), Toast.LENGTH_SHORT).show()
                } else if (editBinding.etPostTime.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, getString(R.string.enter_time), Toast.LENGTH_SHORT).show()
                } else {
                    date = editBinding.etPostDate.text.toString()
                    time = editBinding.etPostTime.text.toString()
                    dateTime = editBinding.etPostDate.text.toString() + " " + editBinding.etPostTime.text.toString()

                    if (dateUtils.getMillis(dateTime.toString(), "dd/MM/yyyy HH:mm a") < System.currentTimeMillis()) {
                        val time = dateUtils.covertTimeToTextFb(dateTime)
                        Log.e("TIMETOTEXT", "onTextChanged: $time")
                        binding.txtTime.text = " . $time"
                        isEdit = true
                        bottomDialog.dismiss()
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.select_past_date), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "DESC" -> {
                if (!editBinding.etDesc.text.isNullOrEmpty()) {
                    binding.txtDesc.visibility = View.VISIBLE
                    binding.txtDesc.setLinkText(editBinding.etDesc.text.toString())
                } else {
                    binding.txtDesc.visibility = View.GONE
                }
                isEdit = true
                bottomDialog.dismiss()

            }

        }
    }

    private val galleryPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            shareImage()
        } else {
            showAlert()
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(this).setTitle(getString(R.string.permission_denied)).setMessage(getString(R.string.allow_permission_from_app_setting)).setPositiveButton(getString(R.string.app_setting), DialogInterface.OnClickListener { dialogInterface, i ->
            // send to app settings if permission is denied permanently
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", getPackageName(), null)
            intent.data = uri
            startActivity(intent)
        }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            showAlert()
        }).show()
    }

    private fun shareImage() {
        /*
        *  if (TextUtils.isEmpty(reviewText)){
            isTextEdit = true
            reviewText = txtReview.text.toString().trim()
            clientText = txtClientName.text.toString().trim()
        }else{
            if (reviewText.equals(txtReview.text.toString().trim()) && clientText.equals(txtClientName.text.toString().trim())){
                isTextEdit = false
            }else{
                isTextEdit = true
                reviewText = txtReview.text.toString().trim()
                clientText = txtClientName.text.toString().trim()
            }
        }*/

        try {
            val folderPath = File(Constant.FOLDER_PATH)
            if (!folderPath.exists()) {
                folderPath.mkdir()
                folderPath.mkdirs()
            }
            val file = File(Constant.FOLDER_PATH + "TEMP" + System.currentTimeMillis() + ".png")
            val output = FileOutputStream(file)

            /*------------------------------------*/

            if (isEdit) {

                try {
                    RecentDesignFragment.objectList = Gson().fromJson(
                        preferenceUtils?.sharedPreferences?.getString(
                            Constant.RECENT_POST, ""
                        ), object : TypeToken<List<DataRecentPost>>() {}.type
                    )
                } catch (e: Exception) {
                    RecentDesignFragment.objectList = arrayListOf()
                }

                RecentDesignFragment.objectList.add(DataRecentPost(file.absolutePath.toString()))

                val postList = Gson().toJson(RecentDesignFragment.objectList)

                preferenceUtils?.saveRecentPost(postList)
            }

            isEdit = false

            /*------------------------------------*/


            val bitmap = viewToBitmap(binding.lPreview)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.close()
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val screenshotUri = FileProvider.getUriForFile(this, packageName + ".provider", file)
            sharingIntent.type = "image/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
//            sharingIntent.putExtra(
//                Intent.EXTRA_TEXT, "Hey, checkout This Amazing App at: \n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
//            )
            startActivity(Intent.createChooser(sharingIntent, "Share using"))


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun viewToBitmap(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun selectPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestImagePermissionGallery.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.CAMERA,
                ),
            )
        } else {
            requestImagePermissionGallery.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                ),
            )
        }
    }

    private val requestImagePermissionGallery = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { map ->
        if (!ArrayList(map.map { it.value }).contains(false)) {
            galleryIntent()
        }
    }
    private fun galleryIntent() {
        FilePickerBuilder.instance.setMaxCount(4).setActivityTheme(R.style.Theme_PickerTheme).pickPhoto(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            imageList = null
            if (data != null) {
                data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)?.let {
                    var pathList: ArrayList<String> = ArrayList()
                    for (item in it) {
                        val path = ContentUriUtils.getFilePath(this, item)
                        path?.let { it1 -> pathList.add(it1) }
                    }
                    imageList = pathList
                    if (!imageList.isNullOrEmpty()) {
                        setImages(pathList)
                    }
                }
            }
        }
    }

    private fun setImages(pathList: ArrayList<String>) {
//        binding.cardImage.visibility = View.VISIBLE
        when (pathList.size) {
            1 -> {
                binding.imgPost1.visibility = View.VISIBLE
                binding.lImageSecond.visibility = View.GONE
                binding.lImageThird.visibility = View.GONE
                binding.lImageForth.visibility = View.GONE
                Glide.with(this@FacebookPostActivity).load(pathList[0]).into(binding.imgPost1)
            }
            2 -> {
                binding.imgPost1.visibility = View.GONE
                binding.lImageSecond.visibility = View.VISIBLE
                binding.lImageThird.visibility = View.GONE
                binding.lImageForth.visibility = View.GONE
                Glide.with(this@FacebookPostActivity).load(pathList[0]).into(binding.imgSecond1)
                Glide.with(this@FacebookPostActivity).load(pathList[1]).into(binding.imgSecond2)
            }
            3 -> {
                binding.imgPost1.visibility = View.GONE
                binding.lImageSecond.visibility = View.GONE
                binding.lImageThird.visibility = View.VISIBLE
                binding.lImageForth.visibility = View.GONE
                Glide.with(this@FacebookPostActivity).load(pathList[0]).into(binding.imgThird1)
                Glide.with(this@FacebookPostActivity).load(pathList[1]).into(binding.imgThird2)
                Glide.with(this@FacebookPostActivity).load(pathList[2]).into(binding.imgThird3)
            }
            4 -> {
                binding.imgPost1.visibility = View.GONE
                binding.lImageSecond.visibility = View.GONE
                binding.lImageThird.visibility = View.GONE
                binding.lImageForth.visibility = View.VISIBLE
                Glide.with(this@FacebookPostActivity).load(pathList[0]).into(binding.imgForth1)
                Glide.with(this@FacebookPostActivity).load(pathList[1]).into(binding.imgForth2)
                Glide.with(this@FacebookPostActivity).load(pathList[2]).into(binding.imgForth3)
                Glide.with(this@FacebookPostActivity).load(pathList[3]).into(binding.imgForth4)
            }
        }
    }

}