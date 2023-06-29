package com.app.fakepostgenerator.ui.theme.dialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.LayoutImageDialogBinding
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.ByteArrayOutputStream
import java.io.File

class SimpleImagePickerBottomDialog : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: LayoutImageDialogBinding

    private var onViewClickedListener: OnViewClickedListener? = null

    interface OnViewClickedListener {
        fun onImageSelected(image: String)
    }

    fun setOnViewClickedListener(onViewClickedListener: OnViewClickedListener?) {
        this.onViewClickedListener = onViewClickedListener
    }


    private lateinit var picturePath: File
    var selectedImagePaths: String? = null
    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LayoutImageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

    }

    private fun setListeners() {

        binding.imgClose.setOnClickListener(this)
        binding.linearGallery.setOnClickListener(this)
        binding.linearCamera.setOnClickListener(this)

    }

    override fun onClick(view: View?) {

        when (view?.id) {


            R.id.img_close -> {
                dismiss()
            }

            R.id.linear_camera -> {
                checkCameraPermission(Manifest.permission.CAMERA, Constant.CAMERA_PERMISSION_CODE)
            }

            R.id.linear_gallery -> {
                checkGalleryPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.STORAGE_PERMISSION_CODE)
            }

        }
    }

    private fun checkCameraPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            openCamera()
        }
    }


    private fun checkGalleryPermissions(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            openGallery()
        }
    }

    private fun openCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        cameraresultLauncher.launch(takePictureIntent)
    }

    var cameraresultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            if (dialog != null) {
                dialog?.dismiss()
            }

            val tempUri: Uri = getImageUri(requireContext(), imageBitmap)!!

            // CALL THIS METHOD TO GET THE ACTUAL PATH

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            selectedImagePaths = File(getRealPathsFromURI(tempUri).toString()).toString()
            System.out.println(selectedImagePaths)
            Log.e("CAMERA", selectedImagePaths.toString())
            if (onViewClickedListener != null)
                onViewClickedListener!!.onImageSelected(selectedImagePaths.toString())

        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryresultLauncher.launch(intent)
    }

    var galleryresultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (dialog != null) {
                dialog?.dismiss()
            }

            imageUri = data?.data
//            binding!!.userImage.setImageURI(imageUri)

//                val selectedImageUri: String = attr.data.toString()
//                val projection = arrayOf(MediaStore.Images.Media.DATA)
//                uploaduserimage.setImageURI(data?.data) // handle chosen image
            //val selectedImageUri: Int = attr.data?.extras?.get("data")
            selectedImagePaths = getRealPathsFromURI(imageUri!!)
            Log.e("Image File Path", "" + selectedImagePaths)
            if (onViewClickedListener != null)
                onViewClickedListener!!.onImageSelected(selectedImagePaths.toString())


        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            inContext.contentResolver, inImage, "Title", null
        )
        return Uri.parse(path)
    }

    fun getRealPathsFromURI(uri: Uri?): String {
        var path = ""
        if (requireContext().contentResolver != null) {
            val cursor = requireContext().contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

}