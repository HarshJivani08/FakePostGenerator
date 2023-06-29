//package com.app.fakepostgenerator.ui.theme.dialog
//
//import android.Manifest
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
//import com.app.fakepostgenerator.R
//import com.app.fakepostgenerator.databinding.LayoutImageDialogBinding
//import com.app.fakepostgenerator.ui.theme.app.Constant
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.theartofdev.edmodo.cropper.CropImage
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//
//class ImagePickerBottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
//
//    private lateinit var binding: LayoutImageDialogBinding
//
//    private lateinit var picturePath: File
//
//    private var onViewClickedListener: OnViewClickedListener? = null
//
//    interface OnViewClickedListener {
//        fun onImageSelected(image: String)
//    }
//
//
//    fun setOnViewClickedListener(onViewClickedListener: OnViewClickedListener?) {
//        this.onViewClickedListener = onViewClickedListener
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        binding = LayoutImageDialogBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setListeners()
//    }
//
//    private fun setListeners() {
//        binding.imgClose.setOnClickListener(this)
//        binding.linearGallery.setOnClickListener(this)
//        binding.linearCamera.setOnClickListener(this)
//    }
//
//    override fun onClick(view: View?) {
//        when (view?.id) {
//            R.id.img_close -> {
//                dismiss()
//            }
//            R.id.linear_camera -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//                if (ContextCompat.checkSelfPermission(
//                        requireActivity(),
//                        Manifest.permission.CAMERA
//                    ) == PackageManager.PERMISSION_DENIED
//                ) {
//                    requestPermissions(
//                        arrayOf(
//                            Manifest.permission.CAMERA,
//                        ),
//                        Constant.CAMERA_PERMISSION_REQUEST_CODE
//                    )
//                } else {
//                    cameraIntent()
//                }
//            } else {
//                cameraIntent()
//            }
//            R.id.linear_gallery -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//
//                if (ContextCompat.checkSelfPermission(
//                        requireActivity(),
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_DENIED ||
//                    ContextCompat.checkSelfPermission(
//                        requireActivity(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_DENIED
//                ) {
//                    requestPermissions(
//                        arrayOf(
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ),
//                        Constant.GALLERY_PERMISSION_REQUEST_CODE
//                    )
//                } else {
//                    galleryIntent()
//                }
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                if (ContextCompat.checkSelfPermission(
//                        requireContext(),
//                        Manifest.permission.READ_MEDIA_IMAGES
//                    ) == PackageManager.PERMISSION_DENIED
//                ) {
//                    requestPermissions(
//                        arrayOf(
//                            Manifest.permission.READ_MEDIA_IMAGES,
//                        ),
//                        Constant.GALLERY_PERMISSION_REQUEST_CODE
//                    )
//                } else {
//                    galleryIntent()
//                }
//            } else {
//                galleryIntent()
//            }
//        }
//    }
//
//    private fun cameraIntent() {
//        val timeStamp =
//            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
//                .format(Date())
//        picturePath = File(
//            Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DCIM + "/photo_" + timeStamp + ".jpg"
//        )
//        val outputUri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", picturePath)
////        val outputUri = Uri.fromFile(picturePath)
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
//        startActivityForResult(intent, Constant.CAMERA_INTENT_REQUEST_CODE)
//    }
//
//    private fun galleryIntent() {
//        val pickPhoto = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        startActivityForResult(pickPhoto, Constant.GALLERY_INTENT_REQUEST_CODE)
//    }
//
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?,
//    ) {
//        when (requestCode) {
//
//            Constant.CAMERA_INTENT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
//
//                try {
//                    val path = picturePath.absolutePath
//
//                    CropImage.activity(Uri.fromFile(File(path)))
//                        .setAspectRatio(1, 1)
//                        .start(requireActivity(), this)
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//
//            Constant.GALLERY_INTENT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
//
//                try {
//                    val selectedImage = data!!.data
//                    val filePathColumn =
//                        arrayOf(MediaStore.Images.Media.DATA)
//                    val cursor = requireActivity().contentResolver
//                        .query(
//                            selectedImage!!, filePathColumn, null, null,
//                            null
//                        )
//                    cursor!!.moveToFirst()
//                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                    val path = cursor.getString(columnIndex)
//
//                    CropImage.activity(Uri.fromFile(File(path)))
//                        .setAspectRatio(1, 1)
//                        .start(requireActivity(), this)
//
//                } catch (e: Exception) {
//                    Log.e("PICTURE", "UnSuccess" + e.message)
//
//                    e.printStackTrace()
//                }
//
//            }
//
//            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//
//                val result = CropImage.getActivityResult(data)
//
//                if (resultCode == Activity.RESULT_OK) {
//
//                    val resultUri = result.uri
//                    val finalFile = File(resultUri.path)
//                    val croppedPath = finalFile.toString()
//
//                    if (onViewClickedListener != null)
//                        onViewClickedListener!!.onImageSelected(croppedPath)
//
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    val error = result.error
//                    error.printStackTrace()
//                }
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == Constant.CAMERA_PERMISSION_REQUEST_CODE) {
//
//            if (grantResults.isNotEmpty()
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED
//            ) {
//                cameraIntent()
//            } else {
//                Toast.makeText(activity, getString(R.string.please_allow_given_permission), Toast.LENGTH_LONG).show()
//            }
//
//        } else if (requestCode == Constant.GALLERY_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty()
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                || grantResults[1] == PackageManager.PERMISSION_GRANTED
//            ) {
//                galleryIntent()
//            } else {
//                Toast.makeText(activity, getString(R.string.please_allow_given_permission), Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//}