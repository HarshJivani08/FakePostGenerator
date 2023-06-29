package com.grewon.qmaker.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.app.fakepostgenerator.ui.theme.helper.LogX
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateUtils(val context: Context) {


    // old

//    fun covertTimeToText(dataDate: String?): String? {
//        var convTime: String? = null
//        val prefix = ""
//        val suffix = "Ago"
//        try {
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm a")
////            val localDate = getLocalDateFromUTC(dataDate!!, "dd/MM/yyyy HH:mm a", "dd/MM/yyyy HH:mm a")
//            val pasTime = dateFormat.parse(dataDate)
//            val nowTime = Date()
//            val dateDiff = nowTime.time - pasTime.time
//            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
//            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
//            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
//            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
//            if (second < 60) {
//                if (second < 0){
//                    convTime = "1 Seconds $suffix"
//                }else{
//                    convTime = "$second Seconds $suffix"
//                }
//            } else if (minute < 60) {
//                convTime = "$minute Minutes $suffix"
//            } else if (hour < 24) {
//                convTime = "$hour Hours $suffix"
//            } else if (day >= 7) {
//                convTime = if (day > 360) {
//                    (day / 360).toString() + " Years " + suffix
//                } else if (day > 30) {
//                    (day / 30).toString() + " Months " + suffix
//                } else {
//                    (day / 7).toString() + " Week " + suffix
//                }
//            } else if (day < 7) {
//                if (day > 2) {
//                    val day = SimpleDateFormat("EEEE hh:mm a")
//                    convTime = day.format(pasTime)
//                } else {
//                    convTime = "$day Days $suffix"
//                }
//            }
//        } catch (e: ParseException) {
//            e.printStackTrace()
//            Log.e("ConvTimeE", e.message!!)
//        }
//        return convTime
//    }

//    fun covertTimeToText(dataDate: String?): String? {
//        var convTime: String? = null
//        val prefix = ""
//        val suffix = "Ago"
//        try {
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm a")
//            val pasTime = dateFormat.parse(dataDate)
//            val nowTime = Date()
//            val dateDiff = nowTime.time - pasTime.time
//            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
//            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
//            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
//            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)
//            if (second < 60) {
//                convTime = "$second Seconds $suffix"
//            } else if (minute < 60) {
//                convTime = "$minute Minutes $suffix"
//            } else if (hour < 24) {
//                convTime = "$hour Hours $suffix"
//            } else if (day >= 7) {
//                convTime = if (day > 360) {
//                    (day / 360).toString() + " Years " + suffix
//                } else if (day > 30) {
//                    (day / 30).toString() + " Months " + suffix
//                } else {
//                    (day / 7).toString() + " Week " + suffix
//                }
//            } else if (day < 7) {
//                convTime = "$day Days $suffix"
//            }
//        } catch (e: ParseException) {
//            e.printStackTrace()
//            Log.e("ConvTimeE", e.message!!)
//        }
//        return convTime
//    }

    fun covertTimeToTextInsta(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = "ago"
        try {
            Log.e("TAG", "covertTimeToTextInsta: " + dataDate)
            val gettingDate = convertDateFormat(dataDate.toString(), "dd/MM/yyyy hh:mm a", "dd/MM/yyyy HH:mm")
            Log.e("TAG", "covertTimeToTextInsta: " + gettingDate)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
//          val localDate = getLocalDateFromUTC(dataDate!!, "dd/MM/yyyy HH:mm a", "dd/MM/yyyy HH:mm a")
            val pasTime = dateFormat.parse(gettingDate)
            val nowTime = Date()
            Log.e("TAG", "covertTimeToTextInsta: " + pasTime.time + "  " + nowTime.time)
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = if (second <= 0) {
                    "1 second $suffix"
                } else {
                    "$second seconds $suffix"
                }
            } else if (minute < 60) {
                convTime = "$minute minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour hours $suffix"
            } else if (hour >= 24 && day < 7) {
                convTime = "$day days $suffix"
            } else if (day >= 7) {
                convTime = (day / 7).toString() + " week " + suffix
//                  if (day > 360) {
//                     (day / 360).toString() + " Years " + suffix
//                  } else if (day > 30) {
//                     (day / 30).toString() + " Months " + suffix
//                  } else {
//                     (day / 7).toString() + " Week " + suffix
//                  }
            }
//            else if (day < 7) {
//                if (day > 2) {
//                    val day = SimpleDateFormat("EEEE hh:mm a")
//                    convTime = day.format(pasTime)
//                } else {
//                    convTime = "$day Days $suffix"
//                }
//            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }

    fun covertTimeToTextTweet(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = ""
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
        val currentDate = sdf.format(Date())
        try {
            val gettingDate = convertDateFormat(dataDate.toString(), "dd/MM/yyyy hh:mm a", "dd/MM/yyyy HH:mm")
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
//          val localDate = getLocalDateFromUTC(dataDate!!, "dd/MM/yyyy HH:mm aa", "dd/MM/yyyy HH:mm a")
            val pasTime = dateFormat.parse(gettingDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                if (second < 0) {
                    convTime = "1 s $suffix"
                } else {
                    convTime = "$second s $suffix"
                }
            } else if (minute < 60) {
                convTime = "$minute m $suffix"
            } else if (hour < 24) {
                convTime = "$hour h $suffix"
            } else if (hour >= 24 && day < 7) {
                convTime = "$day d $suffix"
            } else if (day >= 7) {
                val currentYear = convertDateFormat(currentDate, "dd/MM/yyyy HH:mm a", "yyyy")
                val enteredyear = convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "yyyy")
                convTime = if (currentYear.equals(enteredyear)) {
                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM")
                } else {
                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM yy")
                }
//                 if (day < 30) {
//                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM")
//                } else {
//                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM yy")
//                }
//                    if (day > 360) {
//                    (day / 360).toString() + " y " + suffix
//                } else if (day > 30) {
//                    (day / 30).toString() + " m " + suffix
//                } else {
//                    (day / 7).toString() + " Week " + suffix
//                }
//            } else if (day < 7) {
//                if (day > 2) {
//                    val day = SimpleDateFormat("EEEE hh:mm a")
//                    convTime = day.format(pasTime)
//                } else {
//                convTime = "$day d $suffix"
//                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }

    fun covertTimeToTextFb(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = ""
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm a")
        val currentDate = sdf.format(Date())
        try {
            val gettingDate = convertDateFormat(dataDate.toString(), "dd/MM/yyyy hh:mm a", "dd/MM/yyyy HH:mm")
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
//            val localDate = getLocalDateFromUTC(dataDate!!, "dd/MM/yyyy HH:mm aa", "dd/MM/yyyy HH:mm a")
            val pasTime = dateFormat.parse(gettingDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                if (second < 0) {
                    convTime = "1 s $suffix"
                } else {
                    convTime = "$second s $suffix"
                }
            } else if (minute < 60) {
                convTime = "$minute m $suffix"
            } else if (hour < 24) {
                convTime = "$hour h $suffix"
            } else if (hour >= 24 && day < 7) {
                convTime = "$day d $suffix"
            } else if (day >= 7) {
                val currentYear = convertDateFormat(currentDate, "dd/MM/yyyy HH:mm a", "yyyy")
                val enteredyear = convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "yyyy")
                convTime = if (currentYear.equals(enteredyear)) {
                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM")
                } else {
                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM yyyy")
                }

//                    if (day < 30) {
//                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM")
//                } else {
//                    convertDateFormat(dataDate.toString(), "dd/MM/yyyy HH:mm a", "dd MMM yyyy")
//                }


//                    if (day > 360) {
//                    (day / 360).toString() + " y " + suffix
//                } else if (day > 30) {
//                    (day / 30).toString() + " m " + suffix
//                } else {
//                    (day / 7).toString() + " Week " + suffix
//                }
//            } else if (day < 7) {
//                if (day > 2) {
//                    val day = SimpleDateFormat("EEEE hh:mm a")
//                    convTime = day.format(pasTime)
//                } else {
//                convTime = "$day d $suffix"
//                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("ConvTimeE", e.message!!)
        }
        return convTime
    }

    fun getMillis(toParse: String, haveFormat: String): Long {
        val formatter = SimpleDateFormat(haveFormat, Locale.getDefault())
        val date: Date = formatter.parse(toParse) as Date
        return date.time
    }

    fun getLocalDateFromUTC(utcDate: String, utcFormat: String, localFormat: String): String {

        try {

            val formatter = SimpleDateFormat(utcFormat, Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val date = formatter.parse(utcDate)

            val dateFormatter = SimpleDateFormat(localFormat, Locale.getDefault())
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(date)

        } catch (e: Exception) {
            return "00-00-0000 00:00"
        }

    }

    fun getMessageHeaderConvertDate(server_time: String): String {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("d MMMM, yyyy", Locale.getDefault())

        if (server_time == sdf.format(calendar.time)) {
            return "Today"
        }
        LogX.E(sdf.format(calendar.time))
        calendar.add(Calendar.DATE, -1)
        LogX.E(sdf.format(calendar.time))
        if (server_time == sdf.format(calendar.time)) {
            return "Yesterday"
        }

        return server_time
    }


    fun getCurrentDate(format: String): String {

        try {

            val formatter = SimpleDateFormat(format, Locale.getDefault())
            return formatter.format(Calendar.getInstance().timeInMillis)

        } catch (e: Exception) {
            return "00-00-0000 00:00"
        }

    }

    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun getNextDate(curDate: String): String? {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(curDate)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return format.format(calendar.time)
    }

    fun getPreviousDate(preDate: String): String? {
        val format = SimpleDateFormat("dd mmmm,yyyy", Locale.getDefault())
        val date = format.parse(preDate)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return format.format(calendar.time)

    }


    fun getPrevDate(currentDate: String, format: String): String {

        try {

            val formatter = SimpleDateFormat(format, Locale.getDefault())

            val date = formatter.parse(currentDate)

            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DATE, -1)

            return formatter.format(cal.time)

        } catch (e: Exception) {
            return "00-00-0000"
        }

    }

    fun getNextDate(currentDate: String, format: String): String {

        try {

            val formatter = SimpleDateFormat(format, Locale.getDefault())

            val date = formatter.parse(currentDate)

            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DATE, 1)

            return formatter.format(cal.timeInMillis)

        } catch (e: Exception) {
            return "00-00-0000"
        }

    }

    fun isCurrentDate(currentDate: String, format: String): Boolean {

        try {

            val formatter = SimpleDateFormat(format, Locale.getDefault())

            return currentDate == formatter.format(Date())

        } catch (e: Exception) {
            return false
        }

    }

    fun isDatePassed(date: String): Boolean {

        return try {

            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val dateToCheck = formatter.parse(date)

            val dateFormat = formatter.format(Date())
            val currentDate = formatter.parse(dateFormat)

            dateToCheck.before(currentDate)

        } catch (e: Exception) {
            false
        }

    }

    fun convertDate(tempDate: String, from: String, to: String, isUTC: Boolean): String {
        val oldFormat = SimpleDateFormat(from, Locale.getDefault())
        if (isUTC) {
            oldFormat.timeZone = TimeZone.getTimeZone("UTC")
        }
        val newFormat = SimpleDateFormat(to, Locale.getDefault())
        newFormat.timeZone = TimeZone.getDefault()

        val date: Date
        var str = tempDate

        try {
            date = oldFormat.parse(tempDate)
            str = newFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str.capitalize(Locale.ROOT)
    }

    fun convertDateFormat(
        dateToFormat: String,
        haveFormat: String,
        wantFormat: String,
    ): String {
        try {

            val haveFormatter = SimpleDateFormat(haveFormat, Locale.getDefault())
            val wantFormatter = SimpleDateFormat(wantFormat, Locale.getDefault())

            val date = haveFormatter.parse(dateToFormat)
            return wantFormatter.format(date)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return dateToFormat
    }

    fun getMonth(dateToFormat: String): String {
        try {
            //Sat Aug 28 2021 00:00:00 GMT+0100

            val haveFormatter = SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.US)
            val wantFormatter = SimpleDateFormat("MMMM", Locale.US)

            val calender = Calendar.getInstance()
            val currentMonth = wantFormatter.format(calender.timeInMillis)

            val date = haveFormatter.parse(dateToFormat)
            val month = wantFormatter.format(date)

            if (month == currentMonth) {
                return "THIS MONTH"
            } else {
                return "THIS $month"
            }

        } catch (e: Exception) {
            return ""
        }

    }

    fun showTimePickerDialogForText(context: Context, editText: TextView) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(
            context, { timePicker, selectedHour, selectedMinute ->

                editText.text = convert24to12Time("$selectedHour:$selectedMinute")

            }, hour, minute, false
        )
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    fun showDatePickerDialog(context: Context, editText: EditText) {
        val mcurrentDate = Calendar.getInstance()

        val mYear = mcurrentDate[Calendar.YEAR]
        val mMonth = mcurrentDate[Calendar.MONTH]
        val mDay = mcurrentDate[Calendar.DAY_OF_MONTH]
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val datePickerDialog = DatePickerDialog(context, { datePicker, i, i1, i2 ->
            val calendar = Calendar.getInstance()
            calendar[Calendar.YEAR] = i
            calendar[Calendar.MONTH] = i1
            calendar[Calendar.DATE] = i2
            editText.setText(sdf.format(calendar.time))
        }, mYear, mMonth, mDay)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis();
        datePickerDialog.show()
    }

    @Throws(ParseException::class)
    fun timeConversion12to24(twelveHoursTime: String?, haveFormat: String, wantFormat: String): String? {
        //Date/time pattern of input date (12 Hours format - hh used for 12 hours)
        val df: DateFormat = SimpleDateFormat(haveFormat)
        //Date/time pattern of desired output date (24 Hours format HH - Used for 24 hours)
        val outputformat: DateFormat = SimpleDateFormat(wantFormat)
        var date: Date? = null
        var output: String? = null

        //Returns Date object
        date = df.parse(twelveHoursTime)

        //old date format to new date format
        output = outputformat.format(date)
        println(output)
        return output
    }

    fun convert24to12Time(server_time: String): String {
        var ourdate: String
        try {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val value = formatter.parse(server_time)
            val dateFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault()) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            ourdate = dateFormatter.format(value)

            //Log.d("OurDate", OurDate);
        } catch (e: Exception) {
            ourdate = "0000-00-00 00:00:00"
        }

        return ourdate
    }

    fun convertDateIntoString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = date
        return sdf.format(calendar.time)
    }


    fun dateIsPast(startDate: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = sdf.parse(startDate)

        val now = Calendar.getInstance()
        val yourDate = Calendar.getInstance()
        yourDate.time = date
//        now.before(yourDate)
        return daysBetween(now, yourDate)
    }

    fun daysBetween(day1: Calendar, day2: Calendar): Int {
        var dayOne = day1.clone() as Calendar
        var dayTwo = day2.clone() as Calendar
        return if (dayOne[Calendar.YEAR] == dayTwo[Calendar.YEAR]) {
            Math.abs(dayOne[Calendar.DAY_OF_YEAR] - dayTwo[Calendar.DAY_OF_YEAR])
        } else {
            if (dayTwo[Calendar.YEAR] > dayOne[Calendar.YEAR]) {
                //swap them
                val temp = dayOne
                dayOne = dayTwo
                dayTwo = temp
            }
            var extraDays = 0
            val dayOneOriginalYearDays = dayOne[Calendar.DAY_OF_YEAR]
            while (dayOne[Calendar.YEAR] > dayTwo[Calendar.YEAR]) {
                dayOne.add(Calendar.YEAR, -1)
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR)
            }
            extraDays - dayTwo[Calendar.DAY_OF_YEAR] + dayOneOriginalYearDays
        }
    }
}