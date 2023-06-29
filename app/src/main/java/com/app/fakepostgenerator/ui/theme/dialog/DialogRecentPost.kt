package com.app.fakepostgenerator.ui.theme.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.LayoutRecentPostDialogBinding
import com.bumptech.glide.Glide

class DialogRecentPost : Dialog {

    private var shareListener: View.OnClickListener? = null

    private lateinit var binding:LayoutRecentPostDialogBinding

    constructor(context: Context) : super(context)
    lateinit var ImgFile : String
    constructor(context: Context, themeResId: Int,ImgPath : String) : super(context, themeResId)  {
        ImgFile = ImgPath
    }

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable,cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent))
        binding=LayoutRecentPostDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgShare.setOnClickListener(shareListener)
        Glide.with(context)
            .load(ImgFile)
            .into(binding.imgRecent)
    }



    fun shareListener(onClickListener: View.OnClickListener){
        this.shareListener = onClickListener
    }

    fun setImage(data: String) {

    }

}