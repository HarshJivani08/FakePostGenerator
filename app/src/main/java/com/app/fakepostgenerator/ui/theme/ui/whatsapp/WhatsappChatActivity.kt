package com.app.fakepostgenerator.ui.theme.ui.whatsapp

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityWhatsappChatBinding
import com.app.fakepostgenerator.databinding.LayoutWhatsappFieldDialogBinding
import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.app.fakepostgenerator.ui.theme.app.QMakerApp
import com.app.fakepostgenerator.ui.theme.dialog.SimpleImagePickerBottomDialog
import com.app.fakepostgenerator.ui.theme.model.DataRecentPost
import com.app.fakepostgenerator.ui.theme.model.DataWhatsappChat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grewon.qmaker.ui.fake_post.adapter.AdapterWhatsappChat
import com.grewon.qmaker.ui.recent_design.RecentDesignFragment
import com.grewon.qmaker.utils.DateUtils
import com.grewon.qmaker.utils.ImageUtils
import com.grewon.qmaker.utils.ListUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class WhatsappChatActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityWhatsappChatBinding
    lateinit var editBinding: LayoutWhatsappFieldDialogBinding
    private var bottomDialog: BottomSheetDialog? = null
    lateinit var dateUtils: DateUtils
    lateinit var imageUtils: ImageUtils
    private var time: String? = null
    lateinit var adapter: AdapterWhatsappChat
    private var receiveImage: String? = null
    private var sendImage: String? = null
    private var isSeen: Boolean = false
    private var device: String = "ANDROID"
    private var isViewMode: Boolean? = false
    private var isFirst: Boolean = true
    private var isEdit: Boolean = false
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatsappChatBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        imageUtils = ImageUtils(applicationContext)
        dateUtils = DateUtils(applicationContext)

        setClick()
        setPreData()
        setData()
    }

    private fun setPreData() {
        if (preferenceUtils?.getUser() != null) {
            userName = preferenceUtils!!.getUser()!!.name.toString()
            binding.txtUserName.setText(userName)
            binding.txtUserNameIos.setText(userName)
            if (!preferenceUtils?.getUser()!!.image.isNullOrEmpty()) {
                Glide.with(this@WhatsappChatActivity).load(QMakerApp.preferenceUtils?.getUser()!!.image).circleCrop().into(binding.imgUser)
                Glide.with(this@WhatsappChatActivity).load(QMakerApp.preferenceUtils?.getUser()!!.image).circleCrop().into(binding.imgUserIos)
            }
        }
    }

    private fun setData() {
        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.rvChat.layoutManager = layoutManager
        adapter = AdapterWhatsappChat(applicationContext)
        binding.rvChat.adapter = adapter
        adapter.setList(ListUtils.addChatList())
    }

    private fun setClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgUser.setOnClickListener(this)
        binding.imgUserIos.setOnClickListener(this)
        binding.imgEditUserName.setOnClickListener(this)
        binding.lUserName.setOnClickListener(this)
        binding.imgEditUserNameIos.setOnClickListener(this)
        binding.lUserNameIos.setOnClickListener(this)
        binding.btnBreakMsg.setOnClickListener(this)
        binding.btnSenderChast.setOnClickListener(this)
        binding.btnReceiverChat.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)

        binding.checkBoxHeader.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.lMianHeader.visibility = View.GONE
            } else {
                binding.lMianHeader.visibility = View.VISIBLE
            }
            isEdit = true
        })

        binding.checkboxFooter.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.lMainFooter.visibility = View.GONE
            } else {
                binding.lMainFooter.visibility = View.VISIBLE
            }
            isEdit = true
        })

        binding.checkboxonline.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.txtOnlineStatus.visibility = View.VISIBLE
                binding.txtOnlineStatusIos.visibility = View.VISIBLE
            } else {
                binding.txtOnlineStatus.visibility = View.GONE
                binding.txtOnlineStatusIos.visibility = View.GONE
            }
            isEdit = true
        })

        binding.rdGroup.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.rdEdit -> {
                        adapter.showEditIcons()
                        binding.imgEditUserName.visibility = View.VISIBLE
                        binding.imgEditUserNameIos.visibility = View.VISIBLE
                        binding.lEdit.visibility = View.VISIBLE
                        isViewMode = false
                    }
                    R.id.rdView -> {
                        adapter.removeEdiIcons()
                        binding.imgEditUserName.visibility = View.GONE
                        binding.imgEditUserNameIos.visibility = View.GONE
                        binding.lEdit.visibility = View.GONE
                        isViewMode = true
                    }
                    // add more cases here to handle other buttons in the your RadioGroup
                }
            }
            isEdit = true
        }

        binding.rgDevice.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.rdAndroid -> {
                        adapter.androidDevice()
                        binding.lHeader.visibility = View.VISIBLE
                        binding.lFooter.visibility = View.VISIBLE
                        binding.lHeaderIos.visibility = View.GONE
                        binding.lFooterIos.visibility = View.GONE
                        binding.imgBackground.setImageResource(R.drawable.bg_whatsapp)
                        device = "ANDROID"
                    }
                    R.id.rdIos -> {
                        adapter.iosDevice()
                        binding.lHeaderIos.visibility = View.VISIBLE
                        binding.lFooterIos.visibility = View.VISIBLE
                        binding.lHeader.visibility = View.GONE
                        binding.lFooter.visibility = View.GONE
                        device = "IOS"
                        binding.imgBackground.setImageResource(R.drawable.bg_whatsapp_ios)
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
                            Log.e("INSTA_USER_IMAGE", "onImageSelected: $image")
                            dialog.dismiss()
//                      binding.imgUserTop.loadWithGlide(image,R.drawable.ic_user_insta_placeholder)
//                      Glide.with(this@WhatsappChatActivity).load(image).circleCrop().into(binding.imgUserTop)
                            Glide.with(this@WhatsappChatActivity).load(image).circleCrop().into(binding.imgUser)
//                      binding.imgDeleteUser.visibility = View.VISIBLE
//                      binding.imgUserTop.tag = image
                            binding.imgUser.tag = image
                        }
                    })
                    dialog.show(supportFragmentManager, "ImagePicker")
                }
            }
            binding.imgUserIos -> {
                val dialog = SimpleImagePickerBottomDialog()
                dialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {

                    override fun onImageSelected(image: String) {
                        Log.e("INSTA_USER_IMAGE", "onImageSelected: " + image)
                        dialog.dismiss()
//                      binding.imgUserTop.loadWithGlide(image,R.drawable.ic_user_insta_placeholder)
//                      Glide.with(this@WhatsappChatActivity).load(image).circleCrop().into(binding.imgUserTop)
                        Glide.with(this@WhatsappChatActivity).load(image).circleCrop().into(binding.imgUserIos)
//                      binding.imgDeleteUser.visibility = View.VISIBLE
//                      binding.imgUserTop.tag = image
                        binding.imgUserIos.tag = image
                    }
                })
                dialog.show(supportFragmentManager, "ImagePicker")
            }
            binding.imgEditUserName -> {
                initEditWhatsAppDialog("NAME")
            }
            binding.lUserName -> {
                initEditWhatsAppDialog("NAME")
            }
            binding.imgEditUserNameIos -> {
                initEditWhatsAppDialog("NAME")
            }
            binding.lUserNameIos -> {
                initEditWhatsAppDialog("NAME")
            }
            binding.btnBreakMsg -> {
                initEditWhatsAppDialog("BREAK")
            }
            binding.btnSenderChast -> {
                initEditWhatsAppDialog("SEND_CHAT")
            }
            binding.btnReceiverChat -> {
                initEditWhatsAppDialog("RECEIVE_CHAT")
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

    private fun initEditWhatsAppDialog(tag: String) {
        bottomDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        editBinding = LayoutWhatsappFieldDialogBinding.inflate(layoutInflater)
        bottomDialog?.setContentView(editBinding.root)

        when (tag) {
            "NAME" -> {
                editBinding.lName.visibility = View.VISIBLE
                editBinding.etName.setText(binding.txtUserName.text)
            }
            "BREAK" -> {
                editBinding.lMsgBreak.visibility = View.VISIBLE
            }
            "SEND_CHAT" -> {
                editBinding.lSendChat.visibility = View.VISIBLE
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
                val currentDate = sdf.format(Date())
                val time = dateUtils.convertDateFormat(currentDate, "dd/MM/yyyy HH:mm a", "HH:mm a")
                val timeComeFromServer = dateUtils.convert24to12Time(time).replace("PM", "pm").replace("AM", "am");
                editBinding.etSendTime.setText(timeComeFromServer)

                sendImage = ""
            }
            "RECEIVE_CHAT" -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
                val currentDate = sdf.format(Date())
                val time = dateUtils.convertDateFormat(currentDate, "dd/MM/yyyy HH:mm a", "HH:mm a")
                val timeComeFromServer = dateUtils.convert24to12Time(time).replace("PM", "pm").replace("AM", "am");
                editBinding.etReceiveTime.setText(timeComeFromServer)
                editBinding.lReceiveChat.visibility = View.VISIBLE
                receiveImage = ""
            }
        }
        isSeen = editBinding.checkBoxSeen.isChecked
        editBinding.checkBoxSeen.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            isSeen = isChecked
        })
        editBinding.btnDone.setOnClickListener {
            getData(tag, bottomDialog!!)
        }
        editBinding.etSendTime.setOnClickListener() {
            dateUtils.showTimePickerDialogForText(this, editBinding.etSendTime)
        }
        editBinding.etReceiveTime.setOnClickListener() {
            dateUtils.showTimePickerDialogForText(this, editBinding.etReceiveTime)
        }
        editBinding.rlSendImage.setOnClickListener {
            Log.e("RLIMAGE_SELECTED", "initEditWhatsAppDialog: rlSendImage")
            val dialog = SimpleImagePickerBottomDialog()
            dialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {
                override fun onImageSelected(image: String) {
                    Log.e("INSTA_USER_IMAGE", "onImageSelected: " + image)
                    dialog.dismiss()
//                  binding.imgUserTop.loadWithGlide(image,R.drawable.ic_user_insta_placeholder)
                    Glide.with(this@WhatsappChatActivity).load(image).into(editBinding.imgSendImage)
                    editBinding.imgDeleteSendImage.visibility = View.VISIBLE
                    editBinding.imgSendImage.tag = image
                    sendImage = image
                    Log.e("IMAGE_PATH", "onImageSelected: " + image)
                }
            })
            dialog.show(supportFragmentManager, "ImagePicker")
        }
        editBinding.rlReceiveImage.setOnClickListener {
            Log.e("RLIMAGE_SELECTED", "initEditWhatsAppDialog: rlReceiveImage")
            val mDialog = SimpleImagePickerBottomDialog()
            mDialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {
                override fun onImageSelected(image: String) {
                    Log.e("INSTA_USER_IMAGE", "onImageSelected: " + image)
                    mDialog.dismiss()
//                  binding.imgUserTop.loadWithGlide(image,R.drawable.ic_user_insta_placeholder)
                    Glide.with(this@WhatsappChatActivity).load(image).into(editBinding.imgReceiveImage)
                    editBinding.imgDeleteReceiveImage.visibility = View.VISIBLE
                    editBinding.imgReceiveImage.tag = image
                    receiveImage = image
                    Log.e("IMAGE_PATH", "onImageSelected: " + image)
                }
            })
            mDialog.show(supportFragmentManager, "ImagePicker")
        }
        editBinding.imgDeleteSendImage.setOnClickListener {
            sendImage = ""
            editBinding.imgDeleteSendImage.visibility = View.GONE
                Glide.with(this@WhatsappChatActivity).load(R.drawable.ic_image_placeholder).into(editBinding.imgSendImage)
        }
        editBinding.imgDeleteReceiveImage.setOnClickListener {
            receiveImage = ""
            editBinding.imgDeleteReceiveImage.visibility = View.GONE
            Glide.with(this@WhatsappChatActivity).load(R.drawable.ic_image_placeholder).into(editBinding.imgReceiveImage)
        }

        if (!bottomDialog!!.isShowing) {
            bottomDialog!!.show()
        }
    }

    private fun getData(tag: String, bottomDialog: BottomSheetDialog) {
        when (tag) {
            "NAME" -> {
                if (!editBinding.etName.text.isNullOrEmpty()) {
                    userName = editBinding.etName.text.toString()
                    binding.txtUserName.text = userName
                    binding.txtUserNameIos.text = userName
                    bottomDialog.dismiss()
                    isEdit = true
                } else {
                    Toast.makeText(this, getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show()
                }
            }
            "BREAK" -> {
                if (!editBinding.etMsgBreak.text.isNullOrEmpty()) {
                    val data: ArrayList<DataWhatsappChat> = ArrayList()
                    if (binding.rdEdit.isChecked) {
                        data.add(DataWhatsappChat("", "", editBinding.etMsgBreak.text.toString(), 2, "", isEditCheck = true, isSeen = false, device, 0))
                    } else {
                        data.add(DataWhatsappChat("", "", editBinding.etMsgBreak.text.toString(), 2, "", isEditCheck = false, isSeen = false, device, 0))
                    }
                    adapter.addList(data)
                    bottomDialog.dismiss()
                } else {
                    bottomDialog.dismiss()
                }
                isEdit = true
            }
            "SEND_CHAT" -> {
                val chatList = adapter.getChatList()

                if (!chatList.isNullOrEmpty()) {
                    isFirst = chatList.last().msgType == 0 || chatList.last().msgType == 2
                } else {
                    isFirst = true
                }

                if (!editBinding.etSendText.text.isNullOrEmpty() && !editBinding.etSendTime.text.isNullOrEmpty() || !sendImage.isNullOrEmpty()) {
                    val data: ArrayList<DataWhatsappChat> = ArrayList()
                    if (!editBinding.etSendText.text.isNullOrEmpty()) {
                        if (binding.rdEdit.isChecked) {
                            data.add(DataWhatsappChat(editBinding.etSendText.text.toString(), editBinding.etSendTime.text.toString(), "", 0, sendImage!!, true, isSeen, device, 1, isFirst))
                        } else {
                            data.add(DataWhatsappChat(editBinding.etSendText.text.toString(), editBinding.etSendTime.text.toString(), "", 0, sendImage!!, false, isSeen, device, 1, isFirst))
                        }
                    } else if (editBinding.etSendText.text.isNullOrEmpty() && !sendImage.isNullOrEmpty()) {
                        if (binding.rdEdit.isChecked) {
                            data.add(DataWhatsappChat(editBinding.etSendText.text.toString(), editBinding.etSendTime.text.toString(), "", 3, sendImage!!, true, isSeen, device, 1, isFirst))
                        } else {
                            data.add(DataWhatsappChat(editBinding.etSendText.text.toString(), editBinding.etSendTime.text.toString(), "", 3, sendImage!!, false, isSeen, device, 1, isFirst))
                        }
                    } else {
                        if (binding.rdEdit.isChecked) {
                            data.add(DataWhatsappChat(editBinding.etSendText.text.toString(), editBinding.etSendTime.text.toString(), "", 0, "", true, isSeen, device, 1, isFirst))
                        } else {
                            data.add(DataWhatsappChat(editBinding.etSendText.text.toString(), editBinding.etSendTime.text.toString(), "", 0, "", false, isSeen, device, 1, isFirst))
                        }
                    }
                    adapter.addList(data)
                }
                bottomDialog.dismiss()
                isEdit = true
            }
            "RECEIVE_CHAT" -> {
                val chatList = adapter.getChatList()
                if (!chatList.isNullOrEmpty()) {
                    isFirst = chatList.last().msgType == 0 || chatList.last().msgType == 1
                } else {
                    isFirst = true
                }
                Log.e("LAST_POSITION", "getData: " + chatList.last().text)
                if (!editBinding.etReceiveText.text.isNullOrEmpty() && !editBinding.etReceiveTime.text.isNullOrEmpty() || !receiveImage.isNullOrEmpty()) {
                    val data: ArrayList<DataWhatsappChat> = ArrayList()
                    if (!editBinding.etReceiveText.text.isNullOrEmpty()) {
                        if (binding.rdEdit.isChecked) {
                            data.add(DataWhatsappChat(editBinding.etReceiveText.text.toString(), editBinding.etReceiveTime.text.toString(), "", 1, receiveImage!!, true, isSeen, device, 2, isFirst))
                        } else {
                            data.add(DataWhatsappChat(editBinding.etReceiveText.text.toString(), editBinding.etReceiveTime.text.toString(), "", 1, receiveImage!!, false, isSeen, device, 2, isFirst))
                        }
                    } else if (editBinding.etReceiveText.text.isNullOrEmpty() && !receiveImage.isNullOrEmpty()) {
                        if (binding.rdEdit.isChecked) {
                            data.add(DataWhatsappChat(editBinding.etReceiveText.text.toString(), editBinding.etReceiveTime.text.toString(), "", 4, receiveImage!!, true, isSeen, device, 2, isFirst))
                        } else {
                            data.add(DataWhatsappChat(editBinding.etReceiveText.text.toString(), editBinding.etReceiveTime.text.toString(), "", 4, receiveImage!!, false, isSeen, device, 2, isFirst))
                        }
                    } else {
                        if (binding.rdEdit.isChecked) {
                            data.add(DataWhatsappChat(editBinding.etReceiveText.text.toString(), editBinding.etReceiveTime.text.toString(), "", 1, "", true, isSeen, device, 2, isFirst))
                        } else {
                            data.add(DataWhatsappChat(editBinding.etReceiveText.text.toString(), editBinding.etReceiveTime.text.toString(), "", 1, "", false, isSeen, device, 2, isFirst))
                        }
                    }
                    adapter.addList(data)
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

    fun viewToBitmap(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}