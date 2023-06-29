package com.app.fakepostgenerator.ui.theme.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.fakepostgenerator.BuildConfig
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityTweeterPostBinding
import com.app.fakepostgenerator.databinding.LayoutTweeterFieldDialogBinding
import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.app.fakepostgenerator.ui.theme.app.QMakerApp
import com.app.fakepostgenerator.ui.theme.dialog.SimpleImagePickerBottomDialog
import com.app.fakepostgenerator.ui.theme.model.DataRecentPost
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grewon.qmaker.ui.recent_design.RecentDesignFragment
import com.grewon.qmaker.utils.DateUtils
import com.grewon.qmaker.utils.ImageUtils
import com.grewon.qmaker.utils.prettyCount
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_MEDIA
import droidninja.filepicker.FilePickerConst.REQUEST_CODE_PHOTO
import droidninja.filepicker.utils.ContentUriUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TweeterPostActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityTweeterPostBinding

    lateinit var editBinding: LayoutTweeterFieldDialogBinding

    private var bottomDialog: BottomSheetDialog? = null
    lateinit var dateUtils: DateUtils
    lateinit var imageUtils: ImageUtils
    private var isVerify: Boolean = false
    private var date: String? = null
    private var time: String? = null
    private var dateTime: String? = null
    private var isEdit: Boolean = false
    private var imageList: ArrayList<String>? = ArrayList()
    private var isViewMode: Boolean? = false
    private var userName: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTweeterPostBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        imageUtils = ImageUtils(applicationContext)
        dateUtils = DateUtils(applicationContext)

        preSetData()
        setData()
        setClick()
    }


    private fun preSetData() {
        // name
        if (preferenceUtils?.getUser() != null) {

            name = preferenceUtils!!.getUser()!!.name.toString()

            var txtName = name
            if (txtName.length > 12) {
                txtName = txtName.substring(0, 12) + getString(R.string.three_dots)
            }
            binding.txtName.setText(txtName)
            // user name
            userName = preferenceUtils!!.getUser()?.id.toString()

            var text = userName
            if (text.length > 15) {
                text = text.substring(0, 15) + getString(R.string.three_dots)
            }

            binding.txtUserName.text = "@$text"
            if (!preferenceUtils?.getUser()!!.image.isNullOrEmpty()) {
                Glide.with(this@TweeterPostActivity).load(QMakerApp.preferenceUtils?.getUser()!!.image).circleCrop().into(binding.imgUser)
            }
        } else {
            binding.txtName.setText("Anna Marina")
            binding.txtUserName.text = ("@annamarina")
        }

        // postDateTime
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
        val currentDate = sdf.format(Date())
        dateTime = currentDate
        binding.txtTime.text = " . " + dateUtils.covertTimeToTextTweet(dateTime)

        Log.e("CURRENT_DATE_TIME", "setPreData: " + dateTime)

        //Description text
        binding.txtDescText.setLinkText("This is a sample tweet. @mentions, #hashtags, https://links.com are all automatically converted.")
    }

    private fun setData() {
        // like count
        binding.etLikeCount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.txtLikeCount.text = "$s"
                if (!s.isNullOrEmpty()) {
                    binding.txtLikeCount.visibility = View.VISIBLE
                    val number = s
                    binding.txtLikeCount.text = prettyCount(number.toString())
                } else {
                    binding.txtLikeCount.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        // comment count
        binding.etCommentCount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.txtCommentCount.text = "$s"
                if (!s.isNullOrEmpty()) {
                    val number = s
                    binding.txtCommentCount.visibility = View.VISIBLE
                    binding.txtCommentCount.text = prettyCount(number.toString())
                } else {
                    binding.txtCommentCount.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })
        // view count
        binding.etViewCount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.txtViewCount.text = "$s"
                if (!s.isNullOrEmpty()) {
                    val number = s
                    binding.txtViewCount.visibility = View.VISIBLE
                    binding.txtViewCount.text = prettyCount(number.toString())
                } else {
                    binding.txtViewCount.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}

        })

        // retweet count
        binding.etRetweetCount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.txtRetweetCount.text = "$s"
                if (!s.isNullOrEmpty()) {
                    binding.txtRetweetCount.visibility = View.VISIBLE
                    val number = s
                    binding.txtRetweetCount.text = prettyCount(number.toString())
                } else {
                    binding.txtRetweetCount.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgUser.setOnClickListener(this)
        binding.imgEditName.setOnClickListener(this)
        binding.lUserName.setOnClickListener(this)
        binding.imgEditTime.setOnClickListener(this)
        binding.lTime.setOnClickListener(this)
        binding.imgEditDesc.setOnClickListener(this)
        binding.lDesc.setOnClickListener(this)
        binding.txtDescText.setOnClickListener(this)
        binding.lImage.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)

        binding.checkboxVerify.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.imgVerified.visibility = View.VISIBLE
            } else {
                binding.imgVerified.visibility = View.GONE
            }
            isEdit = true
        })

        binding.rdGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rdEdit -> {
                        binding.imgEditName.visibility = View.VISIBLE
                        binding.imgEditTime.visibility = View.VISIBLE
                        binding.imgEditDesc.visibility = View.VISIBLE
                        binding.llEdit.visibility = View.VISIBLE
                        isViewMode = false
                    }
                    R.id.rdView -> {
                        binding.imgEditName.visibility = View.GONE
                        binding.imgEditTime.visibility = View.GONE
                        binding.imgEditDesc.visibility = View.GONE
                        binding.llEdit.visibility = View.GONE
                        isViewMode = true
                    }
                    // add more cases here to handle other buttons in the your RadioGroup
                }
                isEdit = true
            }
        }

        binding.rgPostAs.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rdText -> {
                        binding.lDesc.visibility = View.VISIBLE
                        binding.cardImage.visibility = View.GONE
                    }
                    R.id.rdImage -> {
                        binding.lDesc.visibility = View.GONE
                        binding.cardImage.visibility = View.VISIBLE
                    }
                    R.id.rdImageText -> {
                        binding.lDesc.visibility = View.VISIBLE
                        binding.cardImage.visibility = View.VISIBLE
                    }
                    // add more cases here to handle other buttons in the your RadioGroup
                }
            }
            isEdit = true
        }
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
//                      binding.imgUserTop.loadWithGlide(image,R.drawable.ic_user_insta_placeholder)
                            Glide.with(this@TweeterPostActivity).load(image).circleCrop().into(binding.imgUser)
                            binding.imgUser.tag = image
                            isEdit = true
                        }
                    })
                    dialog.show(supportFragmentManager, "ImagePicker")
                }
            }

            binding.imgEditName -> {
                initEditTweetDialog("NAME")
            }
            binding.lUserName -> {
                initEditTweetDialog("NAME")
            }
            binding.imgEditTime -> {
                initEditTweetDialog("TIME")
            }
            binding.lTime -> {
                initEditTweetDialog("TIME")
            }
            binding.lImage -> {
                if (isViewMode == false) {
                    selectPhoto()
                }
            }
            binding.imgEditDesc -> {
                initEditTweetDialog("DESC")
            }
            binding.lDesc -> {
                initEditTweetDialog("DESC")
            }
            binding.txtDescText -> {
                initEditTweetDialog("DESC")
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
        }
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

        if (requestCode == REQUEST_CODE_PHOTO) {
            imageList = null
            if (data != null) {
                data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_MEDIA)?.let {
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
        binding.cardImage.visibility = View.VISIBLE
        if (pathList.size == 1) {
            binding.imgPost1.visibility = View.VISIBLE
            binding.lImageSecond.visibility = View.GONE
            binding.lImageThird.visibility = View.GONE
            binding.lImageForth.visibility = View.GONE
            Glide.with(this@TweeterPostActivity).load(pathList[0]).into(binding.imgPost1)

        } else if (pathList.size == 2) {
            binding.imgPost1.visibility = View.GONE
            binding.lImageSecond.visibility = View.VISIBLE
            binding.lImageThird.visibility = View.GONE
            binding.lImageForth.visibility = View.GONE
            Glide.with(this@TweeterPostActivity).load(pathList[0]).into(binding.imgSecond1)
            Glide.with(this@TweeterPostActivity).load(pathList[1]).into(binding.imgSecond2)
        } else if (pathList.size == 3) {
            binding.imgPost1.visibility = View.GONE
            binding.lImageSecond.visibility = View.GONE
            binding.lImageThird.visibility = View.VISIBLE
            binding.lImageForth.visibility = View.GONE
            Glide.with(this@TweeterPostActivity).load(pathList[0]).into(binding.imgThird1)
            Glide.with(this@TweeterPostActivity).load(pathList[1]).into(binding.imgThird2)
            Glide.with(this@TweeterPostActivity).load(pathList[2]).into(binding.imgThird3)
        } else if (pathList.size == 4) {
            binding.imgPost1.visibility = View.GONE
            binding.lImageSecond.visibility = View.GONE
            binding.lImageThird.visibility = View.GONE
            binding.lImageForth.visibility = View.VISIBLE
            Glide.with(this@TweeterPostActivity).load(pathList[0]).into(binding.imgForth1)
            Glide.with(this@TweeterPostActivity).load(pathList[1]).into(binding.imgForth2)
            Glide.with(this@TweeterPostActivity).load(pathList[2]).into(binding.imgForth3)
            Glide.with(this@TweeterPostActivity).load(pathList[3]).into(binding.imgForth4)
        }

    }

    private fun initEditTweetDialog(tag: String) {
        bottomDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        editBinding = LayoutTweeterFieldDialogBinding.inflate(layoutInflater)
        bottomDialog?.setContentView(editBinding.root)
        when (tag) {
            "NAME" -> {
                editBinding.etName.setText(name)
                editBinding.etUserName.setText(userName)
                editBinding.lName.visibility = View.VISIBLE
            }
            "TIME" -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
                val currentDate = sdf.format(Date())

                editBinding.etTweetDate.setText(dateUtils.convertDateFormat(currentDate.toString(), "dd/MM/yyyy HH:mm a", "dd/MM/yyyy"))
                val time = dateUtils.convertDateFormat(currentDate.toString(), "dd/MM/yyyy HH:mm a", "HH:mm a")
                val timeComeFromServer = dateUtils.convert24to12Time(time).replace("PM", "pm").replace("AM", "am");
                editBinding.etTweetTime.setText(timeComeFromServer)
                editBinding.lTime.visibility = View.VISIBLE
            }
            "DESC" -> {
                editBinding.etDesc.setText(binding.txtDescText.text)
                editBinding.lDesc.visibility = View.VISIBLE
            }
        }
        editBinding.btnDone.setOnClickListener {
            getData(tag, bottomDialog!!)
        }
        editBinding.etTweetDate.setOnClickListener {
            dateUtils.showDatePickerDialog(this, editBinding.etTweetDate)
        }
        editBinding.etTweetTime.setOnClickListener {
            dateUtils.showTimePickerDialogForText(this, editBinding.etTweetTime)
        }
        if (!bottomDialog!!.isShowing) {
            bottomDialog!!.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData(tag: String, bottomDialog: BottomSheetDialog) {
        when (tag) {
            "NAME" -> {
                if (editBinding.etName.text.isNullOrEmpty()) {
                    Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show()
                } else if (editBinding.etUserName.text.isNullOrEmpty()) {
                    Toast.makeText(this, getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show()
                } else {
                    name = editBinding.etName.text.toString()
                    var txtName = name
                    if (txtName.length > 12) {
                        txtName = txtName.substring(0, 12) + getString(R.string.three_dots)
                    }
                    binding.txtName.text = txtName
                    userName = editBinding.etUserName.text.toString()
                    var text = userName
                    if (text.length > 15) {
                        text = text.substring(0, 15) + getString(R.string.three_dots)
                    }
                    binding.txtUserName.text = "@$text"
                    bottomDialog.dismiss()
                }
                isEdit = true
            }
            "TIME" -> {
                if (editBinding.etTweetDate.text.isNullOrEmpty()) {
                    Toast.makeText(this, "enter post date", Toast.LENGTH_SHORT).show()
                } else if (editBinding.etTweetTime.text.isNullOrEmpty()) {
                    Toast.makeText(this, "enter post time", Toast.LENGTH_SHORT).show()
                } else {
                    date = editBinding.etTweetDate.text.toString()
                    time = editBinding.etTweetTime.text.toString()
                    dateTime = editBinding.etTweetDate.text.toString() + " " + editBinding.etTweetTime.text.toString()
                    if (dateUtils.getMillis(dateTime.toString(), "dd/MM/yyyy HH:mm a") < System.currentTimeMillis()) {
                        val time = dateUtils.covertTimeToTextTweet(dateTime)
                        Log.e("TIMETOTEXT", "onTextChanged: " + time)
                        binding.txtTime.setText(" . $time")
                        bottomDialog.dismiss()
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.select_past_date), Toast.LENGTH_SHORT).show()
                    }
                }
                isEdit = true
            }
            "DESC" -> {
                if (editBinding.etDesc.text.isNullOrEmpty()) {
                    binding.txtDescText.visibility = View.GONE
                } else {
                    binding.txtDescText.setLinkText(editBinding.etDesc.text.toString())

                }

                bottomDialog.dismiss()
                isEdit = true
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
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT, "Hey, checkout This Amazing App at: \n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
            )
            startActivity(Intent.createChooser(sharingIntent, "Share using"))


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun viewToBitmap(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}