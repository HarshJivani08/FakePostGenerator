package com.grewon.qmaker.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class ImageUtils(val context: Context) {

    fun loadImage(
        imageURL: String? = "",
        imageRes: String = "",
        imageView: ImageView,
        progressView: View? = null,
        isCircular: Boolean = false,
        isRoundCorners: Boolean = false,
        corners: Int = 15,
        isLocal: Boolean = false,
        placeholderRes: Int = 0,
        placeholderColor: Int = 0,
        placeholderDrawable: GradientDrawable? = null,
        forceReload: Boolean = true
    ) {

        var requestOptions = com.bumptech.glide.request.RequestOptions()
        requestOptions = requestOptions.format(DecodeFormat.DEFAULT)
        requestOptions = requestOptions.override(Target.SIZE_ORIGINAL)

        if (isCircular)
            requestOptions = requestOptions.fitCenter().circleCrop()

        if (isRoundCorners)
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(corners))

        if (placeholderRes != 0)
            requestOptions = requestOptions.placeholder(placeholderRes)

        if (placeholderColor != 0)
            requestOptions = requestOptions.placeholder(ColorDrawable(placeholderColor))

        if (placeholderDrawable != null)
            requestOptions = requestOptions.placeholder(placeholderDrawable)

        Glide.with(context).load(
            if (imageURL.isNullOrEmpty())
                imageRes
            else {
                if (isLocal) {
                    "file:///$imageURL"
                } else {
                    imageURL
                }
            }
        )
            .apply(requestOptions)
            .apply(com.bumptech.glide.request.RequestOptions().override(512, 512))
            .apply(com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf(forceReload))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressView != null)
                        progressView.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (progressView != null)
                        progressView.visibility = View.GONE
                    return false
                }
            })
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)

    }




}