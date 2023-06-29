package com.grewon.qmaker.utils

import android.R.attr
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.res.ResourcesCompat
import java.text.DecimalFormat


///////////////////////////////////////////////////////////////////////////
//region Variables
/** color string between given char **/
fun String.makeSpanColorBetween(startChar: Char, endChar: Char, color: Int): Spannable {
    val startIndexChar: Int = indexOf(startChar)
    val endIndexChar: Int = indexOf(endChar)
    val subString = substring((startIndexChar.plus(1)), (endIndexChar))
    val newString = replace(
        startChar.toString(), ""
    ).replace(
        endChar.toString(), ""
    )
    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)

    val spannableString: Spannable = SpannableString(newString)
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}
/**change font**/
fun String.makeSpanFontBetWeen(startChar: Char, endChar: Char, font: Int,context:Context):Spannable{
    val startIndexChar: Int = indexOf(startChar)
    val endIndexChar: Int = indexOf(endChar)
    val subString = substring((startIndexChar.plus(1)), (endIndexChar))
    val newString = replace(
        startChar.toString(), ""
    ).replace(
        endChar.toString(), ""
    )
    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)
    val spannableString: Spannable = SpannableString(newString)
    val typeface: Typeface? = ResourcesCompat.getFont(context, font)
    spannableString.setSpan(StyleSpan(typeface!!.getStyle()),startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return spannableString
}

/** color string between given char **/
fun String.makeTwoSpanColorBetween(startChar1: Char, endChar1: Char, startChar2: Char, endChar2: Char, color: Int): Spannable {
    val startIndexChar: Int = indexOf(startChar1)
    val endIndexChar: Int = indexOf(endChar1)
    val startIndex2Char: Int = indexOf(startChar2)
    val endIndex2Char: Int = indexOf(endChar2)
    val subString = substring((startIndexChar.plus(1)), (endIndexChar))
    val subString2 = substring((startIndex2Char.plus(1)), (endIndex2Char))
    val newString = replace(
        startChar1.toString(), ""
    ).replace(
        endChar1.toString(), ""
    ).replace(
        startChar2.toString(), ""
    ).replace(
        endChar2.toString(), ""
    )

    val startIndex: Int = newString.indexOf(subString)
    val endIndex: Int = newString.indexOf(subString).plus(subString.length)

    val startIndex2: Int = newString.indexOf(subString2)
    val endIndex2: Int = newString.indexOf(subString2).plus(subString2.length)

    val spannableString: Spannable = SpannableString(newString)
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableString.setSpan(
        ForegroundColorSpan(color), startIndex2, endIndex2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannableString
}

fun prettyCount(number: String): String? {
    if (!number.isNullOrEmpty()){
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        val value = Math.floor(Math.log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(numValue / Math.pow(10.0, (base * 3).toDouble())) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }
    return ""

}

