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
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityInstagramPostBinding
import com.app.fakepostgenerator.databinding.LayoutInstagramFieldDialogBinding
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
import com.grewon.qmaker.utils.ListUtils
import com.grewon.qmaker.utils.prettyCount
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class InstagramPostActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityInstagramPostBinding
    lateinit var editBinding: LayoutInstagramFieldDialogBinding
    private var bottomDialog: BottomSheetDialog? = null
    lateinit var dateUtils: DateUtils
    lateinit var imageUtils: ImageUtils
    private var dateTime: String? = null
    private var likeCount: Int = 123
    private var commentCount: Int = 0
    private var fCommentUser: String = ""
    private var sCommentUser: String = ""
    private var fCommentText: String = ""
    private var sCommentText: String = ""
    private var postText: String = ""
    private var isViewMode: Boolean? = false
    private var isView: Boolean = false
    private var isEdit: Boolean = false
    var mentionPattern: Pattern = Pattern.compile("(@[A-Za-z0-9_-]+)")
    var hashtagPattern: Pattern = Pattern.compile("(#[A-Za-z0-9_-]+)")
    var urlPattern: Pattern = Patterns.WEB_URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstagramPostBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        imageUtils = ImageUtils(applicationContext)
        dateUtils = DateUtils(applicationContext)
        setPreData()
        setClick()
    }

    private fun setClick() {
        binding.imgBack.setOnClickListener(this)
        binding.imgUser.setOnClickListener(this)
        binding.imgPost.setOnClickListener(this)
        binding.imgEditUserName.setOnClickListener(this)
        binding.lName.setOnClickListener(this)
        binding.lDesc.setOnClickListener(this)
        binding.imgEditDescription.setOnClickListener(this)
        binding.imgEditTime.setOnClickListener(this)
        binding.lTime.setOnClickListener(this)
        binding.imgEditPostText.setOnClickListener(this)
        binding.lPostText.setOnClickListener(this)
        binding.imgEditCommentCount.setOnClickListener(this)
        binding.lComment.setOnClickListener(this)
        binding.imgEditFComment.setOnClickListener(this)
        binding.imgEditSComment.setOnClickListener(this)
        binding.imgEditLikeCount.setOnClickListener(this)
        binding.lLikeCount.setOnClickListener(this)
        binding.imgLike.setOnClickListener(this)
        binding.imgSave.setOnClickListener(this)
        binding.imgFCommentLike.setOnClickListener(this)
        binding.lFComment.setOnClickListener(this)
        binding.lSComment.setOnClickListener(this)
        binding.imgSCommentLike.setOnClickListener(this)
        binding.imgOwnuser.setOnClickListener(this)
        binding.etPostCount.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)

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

        binding.checkboxTag.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.imgTagged.visibility = View.VISIBLE
            } else {
                binding.imgTagged.visibility = View.GONE
            }
            isEdit = true
        })
        binding.checkboxStory.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.imgUser.setBackgroundResource(R.drawable.ic_profile_ring_insta)
            } else {
                binding.imgUser.background = null
            }
            isEdit = true
        })
        binding.checkboxVerify.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.imgVerified.visibility = View.VISIBLE
            } else {
                binding.imgVerified.visibility = View.GONE
            }
            isEdit = true
        })
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
                            Log.e("INST_USER_IMAGE", "onImageSelected: $image")
                            dialog.dismiss()
                            Glide.with(this@InstagramPostActivity).load(image).circleCrop().into(binding.imgUser)
                            Glide.with(this@InstagramPostActivity).load(image).circleCrop().into(binding.imgOwnuser)
                            binding.imgUser.tag = image
                            binding.imgOwnuser.tag = image
                            isEdit = true
                        }
                    })
                    dialog.show(supportFragmentManager, "ImagePicker")
                }
            }
            binding.imgOwnuser -> {
                if (isViewMode == false) {
                    val dialog = SimpleImagePickerBottomDialog()
                    dialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {
                        override fun onImageSelected(image: String) {
                            Log.e("INST_USER_IMAGE", "onImageSelected: $image")
                            dialog.dismiss()
                            Glide.with(this@InstagramPostActivity).load(image).circleCrop().into(binding.imgOwnuser)
                            Glide.with(this@InstagramPostActivity).load(image).circleCrop().into(binding.imgUser)
                            binding.imgOwnuser.tag = image
                            binding.imgUser.tag = image
                            isEdit = true
                        }
                    })
                    dialog.show(supportFragmentManager, "ImagePicker")
                }
            }
            binding.imgPost -> {
                if (isViewMode == false) {
                    val dialog = SimpleImagePickerBottomDialog()
                    dialog.setOnViewClickedListener(object : SimpleImagePickerBottomDialog.OnViewClickedListener {
                        override fun onImageSelected(image: String) {
                            Log.e("INSTA_USER_IMAGE", "onImageSelected: " + image)
                            dialog.dismiss()
//                      binding.imgUserTop.loadWithGlide(image,R.drawable.ic_user_insta_placeholder)
                            Glide.with(this@InstagramPostActivity).load(image).into(binding.imgPost)
                            binding.imgPost.tag = image
                            isEdit = true
                        }
                    })
                    dialog.show(supportFragmentManager, "ImagePicker")
                }
            }
            binding.lName -> {
                initEditPostDialog("USER_NAME")
            }
            binding.imgEditUserName -> {
                initEditPostDialog("USER_NAME")
            }
            binding.lDesc -> {
                initEditPostDialog("DESC")
            }
            binding.imgEditDescription -> {
                initEditPostDialog("DESC")
            }
            binding.imgEditTime -> {
                initEditPostDialog("TIME")
            }
            binding.lTime -> {
                initEditPostDialog("TIME")
            }
            binding.lPostText -> {
                initEditPostDialog("POST_TEXT")
            }
            binding.imgEditPostText -> {
                initEditPostDialog("POST_TEXT")
            }
            binding.lComment -> {
                initEditPostDialog("COMMENT_COUNT")
            }
            binding.imgEditCommentCount -> {
                initEditPostDialog("COMMENT_COUNT")
            }
            binding.imgEditFComment -> {
                initEditPostDialog("F_COMMENT")
            }
            binding.lFComment -> {
                initEditPostDialog("F_COMMENT")
            }
            binding.imgEditSComment -> {
                initEditPostDialog("S_COMMENT")
            }
            binding.lSComment -> {
                initEditPostDialog("S_COMMENT")
            }
            binding.lLikeCount -> {
                initEditPostDialog("LIKE_COUNT")
            }
            binding.imgEditLikeCount -> {
                initEditPostDialog("LIKE_COUNT")
            }
            binding.imgLike -> {
                binding.imgLike.isSelected = binding.imgLike.isSelected == false
            }
            binding.imgSave -> {
                binding.imgSave.isSelected = binding.imgSave.isSelected == false
            }
            binding.imgFCommentLike -> {
                binding.imgFCommentLike.isSelected = binding.imgFCommentLike.isSelected == false
            }
            binding.imgSCommentLike -> {
                binding.imgSCommentLike.isSelected = binding.imgSCommentLike.isSelected == false
            }
            binding.etPostCount -> {
                binding.etPostCount.showListPopup(ListUtils.getPostCountList()) {
                    Log.e("POST_COUNT", "onClick: " + it.toInt())

                    binding.etPostCount.setText(it)
                    if (it.toInt() > 1) {
                        binding.dotIndicator.initDots(it.toInt())
                        binding.imgNextPost.visibility = View.VISIBLE
                        binding.dotIndicator.visibility = View.VISIBLE
                        binding.txtImageCount.visibility = View.VISIBLE
                        binding.txtImageCount.setText("1/$it")
                    } else {
                        binding.dotIndicator.visibility = View.INVISIBLE
                        binding.imgNextPost.visibility = View.GONE
                        binding.txtImageCount.visibility = View.GONE
                    }
                }
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

    private fun visibleEditicon() {
        binding.imgEditUserName.visibility = View.VISIBLE
        binding.imgEditDescription.visibility = View.VISIBLE
        binding.imgEditTime.visibility = View.VISIBLE
        binding.imgEditPostText.visibility = View.VISIBLE
        binding.imgEditCommentCount.visibility = View.VISIBLE
        binding.imgEditFComment.visibility = View.VISIBLE
        binding.imgEditSComment.visibility = View.VISIBLE
        binding.imgEditLikeCount.visibility = View.VISIBLE
        binding.llEdit.visibility = View.VISIBLE
    }

    private fun hideEditIcon() {
        binding.imgEditUserName.visibility = View.GONE
        binding.imgEditDescription.visibility = View.GONE
        binding.imgEditTime.visibility = View.GONE
        binding.imgEditPostText.visibility = View.GONE
        binding.imgEditCommentCount.visibility = View.GONE
        binding.imgEditFComment.visibility = View.GONE
        binding.imgEditSComment.visibility = View.GONE
        binding.imgEditLikeCount.visibility = View.GONE
        binding.llEdit.visibility = View.GONE
    }

    private fun initEditPostDialog(tag: String) {
        bottomDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        editBinding = LayoutInstagramFieldDialogBinding.inflate(layoutInflater)
        bottomDialog?.setContentView(editBinding.root)

        when (tag) {
            "USER_NAME" -> {
                editBinding.lUserName.visibility = View.VISIBLE
                editBinding.etUserName.setText(binding.txtUserName.text.toString())
            }
            "DESC" -> {
                editBinding.lDesc.visibility = View.VISIBLE
                editBinding.etDescription.setText(binding.txtDescription.text)
            }
            "TIME" -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
                val currentDate = sdf.format(Date())
                editBinding.etPostDate.setText(dateUtils.convertDateFormat(currentDate.toString(), "dd/MM/yyyy HH:mm a", "dd/MM/yyyy"))
                val time = dateUtils.convertDateFormat(currentDate.toString(), "dd/MM/yyyy HH:mm a", "HH:mm a")
                val timeComeFromServer = dateUtils.convert24to12Time(time).replace("PM", "pm").replace("AM", "am");
                editBinding.etPostTime.setText(timeComeFromServer)
                editBinding.lPostDateTime.visibility = View.VISIBLE
//              editBinding.inputPostDate.visibility = View.VISIBLE
//              editBinding.inpuPostTime.visibility = View.VISIBLE
            }
            "POST_TEXT" -> {
                editBinding.lPostText.visibility = View.VISIBLE
                editBinding.etPostText.setText(postText)
            }
            "COMMENT_COUNT" -> {
                editBinding.etCommentCount.setText(commentCount.toString())
                editBinding.lCommentCount.visibility = View.VISIBLE
            }
            "F_COMMENT" -> {
                editBinding.etFCommentUser.setText(fCommentUser)
                editBinding.etFCommentText.setText(fCommentText)
                editBinding.lFComment.visibility = View.VISIBLE
            }
            "S_COMMENT" -> {
                editBinding.etSCommentUser.setText(sCommentUser)
                editBinding.etSCommentText.setText(sCommentText)
                editBinding.lSComment.visibility = View.VISIBLE
            }
            "LIKE_COUNT" -> {
                editBinding.etLikeCount.setText(likeCount.toString())
                editBinding.lLikeCount.visibility = View.VISIBLE
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
                if (editBinding.etUserName.text.isNullOrEmpty()) {
                    Toast.makeText(this, getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show()
                } else {
                    binding.txtUserName.text = editBinding.etUserName.text
                    setPostText(binding.txtUserName.text.toString(), postText)
                    bottomDialog.dismiss()
                }
                isEdit = true
            }
            "DESC" -> {
                if (editBinding.etDescription.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, "Enter Description", Toast.LENGTH_SHORT).show()
                } else {
                    binding.txtDescription.text = editBinding.etDescription.text
                    bottomDialog.dismiss()
                }
                isEdit = true
            }
            "TIME" -> {
                if (editBinding.etPostDate.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, getString(R.string.enter_post_date), Toast.LENGTH_SHORT).show()
                } else if (editBinding.etPostTime.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, getString(R.string.enter_post_time), Toast.LENGTH_SHORT).show()
                } else {
                    dateTime = editBinding.etPostDate.text.toString() + " " + editBinding.etPostTime.text.toString()
                    Log.e("DATE_TIME", "getData: " + dateTime)

                    if (dateUtils.getMillis(dateTime.toString(), "dd/MM/yyyy HH:mm a") < System.currentTimeMillis()) {
                        val time = dateUtils.covertTimeToTextInsta(dateTime)
                        Log.e("TIMETOTEXT", "onTextChanged: " + time)
                        binding.txtPostTime.text = time
                        bottomDialog.dismiss()
                        isEdit = true
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.select_past_date), Toast.LENGTH_SHORT).show()
                    }
                }

            }
            "POST_TEXT" -> {
                postText = editBinding.etPostText.text.toString()
                if (postText.isNullOrEmpty()) {
                    binding.txtPostText.visibility = View.GONE
                } else {
//                    val postText = applicationContext.getString(R.string.data_post_text, binding.txtUserName.text, editBinding.etPostText.text).makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.black))
//                    binding.txtPostText.setLinkText(postText.toString())

                    binding.txtPostText.visibility = View.VISIBLE
                    setPostText(binding.txtUserName.text.toString(), postText)

                }
                bottomDialog.dismiss()
                isEdit = true
            }
            "F_COMMENT" -> {
                if (editBinding.etFCommentUser.text.isNullOrEmpty() || editBinding.etFCommentText.text.isNullOrEmpty()) {
                    binding.rlFComment.visibility = View.GONE
                    if (binding.rlFComment.visibility == View.GONE && binding.rlSComment.visibility == View.GONE) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else if (sCommentUser == binding.txtUserName.text.toString()) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else {
                        binding.lOwnAddComment.visibility = View.GONE
                    }
                    fCommentUser = ""
                    fCommentText = ""
                } else {
//                  binding.lOwnAddComment.visibility = View.GONE
                    fCommentUser = editBinding.etFCommentUser.text.toString()
                    fCommentText = editBinding.etFCommentText.text.toString()

                    setFCommentText()
                    binding.rlFComment.visibility = View.VISIBLE
                    if (fCommentUser == binding.txtUserName.text.toString() && sCommentUser == binding.txtUserName.text.toString()) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else if (binding.rlSComment.visibility == View.GONE && fCommentUser == binding.txtUserName.text.toString()) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else {
                        binding.lOwnAddComment.visibility = View.GONE
                    }
                }
                bottomDialog.dismiss()
                isEdit = true
            }
            "S_COMMENT" -> {
                if (editBinding.etSCommentUser.text.isNullOrEmpty() || editBinding.etSCommentText.text.isNullOrEmpty()) {
                    binding.rlSComment.visibility = View.GONE

                    if (binding.rlFComment.visibility == View.GONE && binding.rlSComment.visibility == View.GONE) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else if (fCommentUser == binding.txtUserName.text.toString()) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else {
                        binding.lOwnAddComment.visibility = View.GONE
                    }
                    sCommentUser = ""
                    sCommentText = ""
                } else {
//                    binding.lOwnAddComment.visibility = View.GONE
                    sCommentUser = editBinding.etSCommentUser.text.toString()
                    sCommentText = editBinding.etSCommentText.text.toString()
                    setSCommentText()
                    if (fCommentUser == binding.txtUserName.text.toString() && sCommentUser == binding.txtUserName.text.toString()) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else if (binding.rlFComment.visibility == View.GONE && sCommentUser == binding.txtUserName.text.toString()) {
                        binding.lOwnAddComment.visibility = View.VISIBLE
                    } else {
                        binding.lOwnAddComment.visibility = View.GONE
                    }
                    binding.rlSComment.visibility = View.VISIBLE
                }
                bottomDialog.dismiss()
                isEdit = true
            }
            "LIKE_COUNT" -> {
                Log.e("LIKE_COUNT", "getData: " + editBinding.etLikeCount.text)
                if (editBinding.etLikeCount.text.isNullOrEmpty() || editBinding.etLikeCount.text.toString() == "0") {
                    binding.txtLikeCount.visibility = View.GONE
                } else {
                    binding.txtLikeCount.visibility = View.VISIBLE
                    val count: String = editBinding.etLikeCount.text.toString()
                    likeCount = count.toInt()
//                   binding.txtLikeCount.text = "$likeCount likes"
                    binding.txtLikeCount.text = prettyCount(likeCount.toString()) + " likes"
                }
                bottomDialog.dismiss()
                isEdit = true
            }
            "COMMENT_COUNT" -> {
                Log.e("COMMENT_COUNT", "getData: " + editBinding.etCommentCount.text)
                if (editBinding.etCommentCount.text.isNullOrEmpty() || editBinding.etCommentCount.text.toString() == "0") {
                    binding.txtCommentCount.visibility = View.GONE
                } else {
                    binding.txtCommentCount.visibility = View.VISIBLE
                    val count: String = editBinding.etCommentCount.text.toString()
                    commentCount = count.toInt()
                    binding.txtCommentCount.text = "View all ${prettyCount(commentCount.toString())} comments"
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

    private fun setPreData() {
        //post text
        if (preferenceUtils?.getUser() != null) {
            binding.txtUserName.setText(preferenceUtils!!.getUser()!!.id)
            if (!QMakerApp.preferenceUtils?.getUser()!!.image.isNullOrEmpty()) {
                Glide.with(this@InstagramPostActivity).load(QMakerApp.preferenceUtils?.getUser()!!.image).circleCrop().into(binding.imgUser)
                Glide.with(this@InstagramPostActivity).load(QMakerApp.preferenceUtils?.getUser()!!.image).circleCrop().into(binding.imgOwnuser)
            }
        }

//        val postText = applicationContext.getString(R.string.data_post_text, binding.etUserName.text, "This is a sample post text. @mentions, #hashtags, https://links.com are all automatically converted.").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.black))
//        binding.txtPostText.setLinkText(postText.toString())
//        val desctext = applicationContext.getString(R.string.data_post_text, "<b>${binding.txtUserName.text}</b>", mDeskText)
//        binding.txtPostText.setLinkText(Html.fromHtml(desctext).toString())
//        binding.txtPostText.setLinkText(getString(R.string.data_post_text, binding.txtUserName.text.toString(), mDescText))
        postText = "This is a sample post text. @mentions, #hashtags, https://links.com are all automatically converted."
        setPostText(binding.txtUserName.text.toString(), postText)
//      binding.txtPostText.setLinkText(Html.fromHtml("<b>${binding.txtUserName.text}</b>").toString() + " " + mDescText)


        // First Comment
        binding.lFComment.visibility = View.VISIBLE
        fCommentUser = "janedoe"
        fCommentText = "I liked the post John. Thanks for sharing"
//      val commentText = applicationContext.getString(R.string.data_first_comment, "janedoe", "I liked the post John. Thanks for sharing").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.black))
//      val commentText = applicationContext.getString(R.string.data_first_comment, "<b>${fCommentUser}</b>", fCommentText)
//      binding.txtFirstComment.text = Html.fromHtml(commentText)
        setFCommentText()

        // Second Comment
        binding.lSComment.visibility = View.VISIBLE
        sCommentUser = "johnniedoe"
        sCommentText = "\uDD25\uDD25\uD83D\uDD25\uD83D\uDD25"
//      val sCommentText = applicationContext.getString(R.string.data_second_comment, sCommentUser, sCommentText).makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.black))
//        val sCommentText = applicationContext.getString(R.string.data_second_comment, "<b>${sCommentUser}</b>", sCommentText)
//        binding.txtSecondComment.text = Html.fromHtml(sCommentText)
        setSCommentText()

        // like count
        binding.txtLikeCount.setText("$likeCount likes")

        // commentCount
        commentCount = 10
        binding.txtCommentCount.setText("View all $commentCount comments")

        // postDateTime
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
        val currentDate = sdf.format(Date())
        val date = dateUtils.convertDateFormat(currentDate, "dd/MM/yyyy HH:mm a", "dd/MM/yyyy")
        val time = dateUtils.convertDateFormat(currentDate, "dd/MM/yyyy HH:mm a", "HH:mm")
        binding.txtPostTime.setText(dateUtils.covertTimeToTextInsta(currentDate))
    }

    private fun setPostText(userName: String, text: String) {

//        val txt = if (text.length > 60) {
//            text.substring(0, 60) + getString(R.string.more)
//        } else {
//            text
//        }

        val a = "<b>${userName}</b>" + " " + text

        val mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)")
        val hashtagPattern = Pattern.compile("(#[A-Za-z0-9_-]+)")
        val urlPattern = Patterns.WEB_URL

        var sb = StringBuffer(a.length)
        val o: Matcher = hashtagPattern.matcher(a)

        while (o.find()) {
            o.appendReplacement(sb, "<font color=\"#00376B\">" + o.group(1) + "</font>")
        }
        o.appendTail(sb)

        val n: Matcher = mentionPattern.matcher(sb.toString())
        sb = StringBuffer(sb.length)

        while (n.find()) {
            n.appendReplacement(sb, "<font color=\"#00376B\">" + n.group(1) + "</font>")
        }
        n.appendTail(sb)

        val m: Matcher = urlPattern.matcher(sb.toString())
        sb = StringBuffer(sb.length)

        while (m.find()) {
            m.appendReplacement(sb, "<font color=\"#00376B\">" + m.group(1) + "</font>")
        }
        m.appendTail(sb)


//        if (sb.length > 60){
//            val commentText = applicationContext.getString(R.string.data_post_text, Html.fromHtml(sb.toString()), "...more").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.grey_66))
//            binding.txtPostText.text = applicationContext.getString(R.string.data_post_text, Html.fromHtml(sb.toString()), "...more").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.grey_66))
//        }else{
//            binding.txtPostText.setText(Html.fromHtml(sb.toString()))
//        }

//        if (sb.length > 60) {
//            return sb.substring(0, 60) + "...";
//        } else {
//            return sb;
//        }

        binding.txtPostText.setText(Html.fromHtml(sb.toString()))

//        if (sb.length > 60) {
//            val txt = Html.fromHtml(sb.toString())
//            binding.txtPostText.text = txt.substring(0, 60) + "..."
//        } else {
//            binding.txtPostText.setText(Html.fromHtml(sb.toString()))
//        }

    }

    private fun setFCommentText() {

//        val txt = if (txt.length > 60) {
//            txt.substring(0, 60) + "...more"
//        } else {
//            txt
//        }

        val a = "<b>${fCommentUser}</b>" + " " + fCommentText

        val mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)")
        val hashtagPattern = Pattern.compile("(#[A-Za-z0-9_-]+)")
        val urlPattern = Patterns.WEB_URL

        var sb = StringBuffer(a.length)
        val o: Matcher = hashtagPattern.matcher(a)

        while (o.find()) {
            o.appendReplacement(sb, "<font color=\"#00376B\">" + o.group(1) + "</font>")
        }
        o.appendTail(sb)

        val n: Matcher = mentionPattern.matcher(sb.toString())
        sb = StringBuffer(sb.length)

        while (n.find()) {
            n.appendReplacement(sb, "<font color=\"#00376B\">" + n.group(1) + "</font>")
        }
        n.appendTail(sb)

        val m: Matcher = urlPattern.matcher(sb.toString())
        sb = StringBuffer(sb.length)

        while (m.find()) {
            m.appendReplacement(sb, "<font color=\"#00376B\">" + m.group(1) + "</font>")
        }
        m.appendTail(sb)

//        if (sb.length > 60){
//            val commentText = applicationContext.getString(R.string.data_post_text, Html.fromHtml(sb.toString()), "...more").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.grey_66))
//            binding.txtPostText.text = applicationContext.getString(R.string.data_post_text, Html.fromHtml(sb.toString()), "...more").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.grey_66))
//        }else{
//            binding.txtPostText.setText(Html.fromHtml(sb.toString()))
//        }

//        if (sb.length > 60) {
//            return sb.substring(0, 60) + "...";
//        } else {
//            return sb;
//        }
        binding.txtFirstComment.setText(Html.fromHtml(sb.toString()))

//        if (sb.length > 60) {
//            val txt = Html.fromHtml(sb.toString())
//            binding.txtPostText.text = txt.substring(0, 60) + "..."
//        } else {
//            binding.txtPostText.setText(Html.fromHtml(sb.toString()))
//        }

    }

    private fun setSCommentText() {

//        val txt = if (txt.length > 60) {
//            txt.substring(0, 60) + "...more"
//        } else {
//            txt
//        }

        val a = "<b>${sCommentUser}</b>" + " " + sCommentText

        val mentionPattern = Pattern.compile("(@[A-Za-z0-9_-]+)")
        val hashtagPattern = Pattern.compile("(#[A-Za-z0-9_-]+)")
        val urlPattern = Patterns.WEB_URL

        var sb = StringBuffer(a.length)
        val o: Matcher = hashtagPattern.matcher(a)

        while (o.find()) {
            o.appendReplacement(sb, "<font color=\"#00376B\">" + o.group(1) + "</font>")
        }
        o.appendTail(sb)

        val n: Matcher = mentionPattern.matcher(sb.toString())
        sb = StringBuffer(sb.length)

        while (n.find()) {
            n.appendReplacement(sb, "<font color=\"#00376B\">" + n.group(1) + "</font>")
        }
        n.appendTail(sb)

        val m: Matcher = urlPattern.matcher(sb.toString())
        sb = StringBuffer(sb.length)

        while (m.find()) {
            m.appendReplacement(sb, "<font color=\"#00376B\">" + m.group(1) + "</font>")
        }
        m.appendTail(sb)

//        if (sb.length > 60){
//            val commentText = applicationContext.getString(R.string.data_post_text, Html.fromHtml(sb.toString()), "...more").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.grey_66))
//            binding.txtPostText.text = applicationContext.getString(R.string.data_post_text, Html.fromHtml(sb.toString()), "...more").makeSpanColorBetween('[', ']', getColor(this@InstagramPostActivity, R.color.grey_66))
//        }else{
//            binding.txtPostText.setText(Html.fromHtml(sb.toString()))
//        }

//        if (sb.length > 60) {
//            return sb.substring(0, 60) + "...";
//        } else {
//            return sb;
//        }
        binding.txtSecondComment.setText(Html.fromHtml(sb.toString()))

//        if (sb.length > 60) {
//            val txt = Html.fromHtml(sb.toString())
//            binding.txtPostText.text = txt.substring(0, 60) + "..."
//        } else {
//            binding.txtPostText.setText(Html.fromHtml(sb.toString()))
//        }
    }

    private fun View.showListPopup(
        items: ArrayList<String>,
        @LayoutRes resource: Int? = null,
        onItemSelect: (item: String) -> Unit,
    ) {
        val listPopupWindow = ListPopupWindow(context, null, androidx.constraintlayout.widget.R.attr.listPopupWindowStyle)
        listPopupWindow.anchorView = this
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(AppCompatResources.getDrawable(this@InstagramPostActivity, R.drawable.custom_popupmenu_background))

        val adapter = ArrayAdapter(this@InstagramPostActivity, resource ?: R.layout.dropdown_popup_item, items)
        listPopupWindow.setAdapter(adapter)

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            if (items.isNotEmpty()) {
                onItemSelect(items[position])
            }
            listPopupWindow.dismiss()
        }

        if (listPopupWindow.isShowing) {
            listPopupWindow.dismiss()
        } else {
            listPopupWindow.show()
        }
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

    fun isUserNameValid(txt: String): Boolean {
        val regx = "[A-Z0-9a-z.]+_[A-Za-z0-9-]"
        val pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(txt)
        return matcher.find()
    }

}