package  com.app.fakepostgenerator.ui.theme.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.app.fakepostgenerator.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import de.hdodenhof.circleimageview.BuildConfig
import java.io.File
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URL
import kotlin.math.roundToInt


///////////////////////////////////////////////////////////////////////////
// LUCIFER //
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// ViewBinding
///////////////////////////////////////////////////////////////////////////

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

inline fun <T : ViewBinding> Fragment.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

///////////////////////////////////////////////////////////////////////////
// Colors & Dimensions
///////////////////////////////////////////////////////////////////////////

fun View.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context, id)
}

fun View.bindDimen(@DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.resources.getDimension(id)
}

fun View.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.getString(id)
}

fun Activity.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(this, id)
}

fun Activity.bindDimen(@DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    resources.getDimension(id)
}

fun Activity.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    getString(id)
}

fun Fragment.bindColor(@ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context!!, id)
}

fun Fragment.bindDimen(@DimenRes id: Int): kotlin.Lazy<Float> = lazy(LazyThreadSafetyMode.NONE) {
    context!!.resources.getDimension(id)
}

fun Fragment.bindString(@StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context!!.getString(id)
}

fun Any.bindColor(context: Context, @ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(context, id)
}

fun Any.bindDimen(context: Context, @DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.resources.getDimension(id)
}

fun Any.bindString(context: Context, @StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    context.getString(id)
}

fun Any.bindColor(view: View, @ColorRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    ContextCompat.getColor(view.context, id)
}

fun Any.bindDimen(view: View, @DimenRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    view.context.resources.getDimension(id)
}

fun Any.bindString(view: View, @StringRes id: Int) = lazy(LazyThreadSafetyMode.NONE) {
    view.context.getString(id)
}

fun getColor(context: Context, @ColorRes id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun getColorStateList(context: Context, @ColorRes id: Int): ColorStateList {
    return ColorStateList.valueOf(ContextCompat.getColor(context, id))
}

fun getColorFromAttr(context: Context, @ColorInt id: Int): Int {
    val typedValue = TypedValue();
    context.theme.resolveAttribute(id, typedValue, true);
    return ContextCompat.getColor(context, typedValue.resourceId)
}

fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(context, id)
}

///////////////////////////////////////////////////////////////////////////
// Logger
///////////////////////////////////////////////////////////////////////////
private var isDebug = BuildConfig.DEBUG || true

fun luciferLog(message: String?) {
    if (isDebug) {
        Log.e("lucifer", message ?: "null")
    }
}

fun luciferLog(tag: String, message: String?) {
    if (isDebug) {
        Log.e(tag, message ?: "null")
    }
}

fun luciferLog(message: Any?) {
    if (isDebug) {
        Log.e("lucifer", Gson().toJson(message ?: "null"))
    }
}

fun luciferLog(tag: String, message: Any?) {
    if (isDebug) {
        Log.e("lucifer", Gson().toJson(message ?: "null"))
    }
}

fun luciferLogWtf(message: String?) {
    if (isDebug) {
        Log.e("luciferWTF", ">>>>>>>>==========------------>  " + (message ?: "null"))
    }
}

fun luciferLogWtf(vararg message: String?) {
    if (isDebug) {
        message.forEachIndexed { index, msg ->
            Log.e("luciferWTF", "$index ::::::::====---->>> $msg")
        }
    }
}

fun luciferLogWtf(vararg message: Any?) {
    if (isDebug) {
        message.forEachIndexed { index, msg ->
            Log.e("luciferWTF", "$index ::::::::====------>>> $msg")
        }
    }
}

fun luciferLogWtf(message: Any?) {
    if (isDebug) {
        Log.e("luciferWTF", ">>>>>>========---------->  " + (message ?: "null"))
    }
}

fun luciferLogLong(str: String) {
    if (isDebug) {
        if (str.length > 4000) {
            Log.e("lucifer", str.substring(0, 4000))
            luciferLogLong(str.substring(4000))
        } else Log.e("lucifer", str)
    }
}

///////////////////////////////////////////////////////////////////////////
// Intents & navigation
///////////////////////////////////////////////////////////////////////////

inline fun <reified T : Activity> Context.launch(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

inline fun <reified T : Activity> Fragment.launch(block: Intent.() -> Unit = {}) {
    startActivity(Intent(requireActivity(), T::class.java).apply(block))
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

fun AppCompatActivity.launchShareSimpleTextChooser(
    contentToShare: String,
    subject: String? = null,
    chooserText: String? = null,
    sharePackageName: String? = null,
) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
    intent.putExtra(Intent.EXTRA_TEXT, contentToShare)
    if (sharePackageName.isNullOrBlank()) {
        intent.setPackage(sharePackageName)
    }
    startActivity(Intent.createChooser(intent, chooserText ?: "share using..."))
}

fun Fragment.launchShareSimpleTextChooser(
    contentToShare: String,
    subject: String? = null,
    chooserText: String? = null,
    sharePackageName: String? = null,
) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
    intent.putExtra(Intent.EXTRA_TEXT, contentToShare)
    if (sharePackageName.isNullOrBlank()) {
        intent.setPackage(sharePackageName)
    }
    startActivity(Intent.createChooser(intent, chooserText ?: "share using..."))
}

///////////////////////////////////////////////////////////////////////////
// ImageLoading
///////////////////////////////////////////////////////////////////////////

var imageCrossFadeSmall = 300
var imageCrossFadeMedium = 600
var imageCrossFadeLarge = 900

fun ImageView.loadWithGlide(
    data: Any?,
    @RawRes @DrawableRes placeHolder: Int = R.drawable.ic_user_insta_placeholder,
    fadeTime: Int = imageCrossFadeSmall,
    showLoader: Boolean = false,
) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 2f
    circularProgressDrawable.centerRadius = 40f
    circularProgressDrawable.start()
    if (data is Int?) {
        data?.let { this.setImageResource(it) }
    } else {
        if (showLoader) {
            Glide.with(this.context).load(data).transition(DrawableTransitionOptions.withCrossFade(fadeTime)).placeholder(circularProgressDrawable).error(placeHolder).into(this)
        } else {
            Glide.with(this.context).load(data).transition(DrawableTransitionOptions.withCrossFade(fadeTime)).placeholder(placeHolder).error(placeHolder).into(this)
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Toaster
///////////////////////////////////////////////////////////////////////////

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

///////////////////////////////////////////////////////////////////////////
// Navigation
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// Refresh
///////////////////////////////////////////////////////////////////////////

fun SwipeRefreshLayout.refreshAndDo(task: () -> Unit) {
    if (isRefreshing) isRefreshing = false
    setOnRefreshListener {
        task()
    }
}

///////////////////////////////////////////////////////////////////////////
// Animation
///////////////////////////////////////////////////////////////////////////

fun TextView.setTextWithAnimation(text: String, duration: Long = 300, completion: (() -> Unit)? = null) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadOutAnimation(duration: Long = 100, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate().alpha(0.5f).setDuration(duration).withEndAction {
        this.visibility = visibility
        completion?.let {
            it()
        }
    }
}

fun View.fadInAnimation(duration: Long = 500, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration).withEndAction {
        completion?.let {
            it()
        }
    }
}

fun View.setRotateAnimation(
    duration: Long = 5000,
    repeatCount: Int = Animation.INFINITE,
) {
    val rotate = RotateAnimation(
        0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    rotate.repeatCount = repeatCount
    rotate.duration = duration
    rotate.interpolator = LinearInterpolator()
    startAnimation(rotate)
}

fun View.setScaleUpAnimation(
    scaleUpDuration: Long = 3000,
    scaleDownDuration: Long = 2000,
) {
    animate().scaleX(1.1f).scaleY(1.1f).setDuration(scaleUpDuration).withEndAction {
        animate().scaleX(1f).scaleY(1f).setDuration(scaleDownDuration).withEndAction {
            setScaleUpAnimation(scaleUpDuration, scaleDownDuration)
        }
    }
}

fun Context.getFileFromCache(): ArrayList<File> {
    val dir = File(cacheDir.absolutePath)
    val fileNameList = ArrayList<File>()
    if (dir.exists()) {
        val fileList = dir.listFiles()
        if (!fileList.isNullOrEmpty()) {
            for (file in fileList) {
                fileNameList.add(file)
            }
        }
    }
    return fileNameList
}

///////////////////////////////////////////////////////////////////////////
//region Math

fun Double.roundToNearest(nearest: Int): Int {
    return (this / nearest).roundToInt() * nearest
}

fun Float.roundToNearest(nearest: Int): Int {
    return (this / nearest).roundToInt() * nearest
}

fun Double.toBigDecimal(): Double {
    return BigDecimal.valueOf(this).setScale(0, RoundingMode.HALF_EVEN).toPlainString().toDouble()
}


//endregion Math
///////////////////////////////////////////////////////////////////////////