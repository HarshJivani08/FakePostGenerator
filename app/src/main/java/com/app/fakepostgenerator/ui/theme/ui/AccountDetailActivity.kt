package com.app.fakepostgenerator.ui.theme.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.ActivityAccountDetailBinding
import com.app.fakepostgenerator.ui.theme.app.BaseActivity
import com.app.fakepostgenerator.ui.theme.dialog.SimpleImagePickerBottomDialog
import com.app.fakepostgenerator.ui.theme.model.UserModel
import com.app.fakepostgenerator.ui.theme.utils.ImageUtils

class AccountDetailActivity : BaseActivity(), View.OnClickListener {

    var TAG="AccountDetailActivity"

    lateinit var imageUtils: ImageUtils
    lateinit var binding: ActivityAccountDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        setClick()
        initView()
    }

    private fun setClick() {
        binding.imgEdit.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    private fun initView() {
            imageUtils = ImageUtils(applicationContext)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_edit -> {
                 val dialog = SimpleImagePickerBottomDialog()
                dialog.setOnViewClickedListener(object :
                    SimpleImagePickerBottomDialog.OnViewClickedListener {

                    override fun onImageSelected(image: String) {

                        dialog.dismiss()

                        imageUtils.loadImage(
                             imageURL = image,
                             isLocal = true,
                             isCircular = true,
                             imageView = binding.imgUser,
                             placeholderRes = R.drawable.ic_profile_avatar
                        )
                        binding.imgUser.tag = image
                    }
                })
                dialog.show(supportFragmentManager, "ImagePicker")
            }
            R.id.btnSave -> {
//                 if (imgUser.tag == null){
//                     showToast(getString(R.string.set_profile_image))
//                 }else
                 if (binding.edtName.text.isNullOrEmpty()){
                     showToast(getString(R.string.enter_your_name))
                 }else if (binding.edtUserId.text.isNullOrEmpty()){
                     showToast(getString(R.string.add_user_name))
                 }
//                 else if (!isUserNameValid(binding.edtUserId.text.toString())){
//                     showToast(getString(R.string.username_validation))
//                 }
                 else{
                       if (binding.imgUser.tag == null){
                           preferenceUtils?.saveUser(UserModel("", binding.edtName.text.toString(), binding.edtUserId.text.toString()))
                       }else{
                           preferenceUtils?.saveUser(UserModel(binding.imgUser.tag.toString(), binding.edtName.text.toString(), binding.edtUserId.text.toString()))
                       }
                         val intent = Intent(applicationContext, MainActivity::class.java)
                         startActivity(intent)
                 }
            }
        }
    }




}