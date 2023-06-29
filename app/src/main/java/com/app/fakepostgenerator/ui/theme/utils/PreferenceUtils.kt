package com.grewon.qmaker.utils

import android.content.SharedPreferences
import com.app.fakepostgenerator.ui.theme.app.Constant
import com.app.fakepostgenerator.ui.theme.model.DataRecentPost
import com.app.fakepostgenerator.ui.theme.model.UserModel
import com.google.gson.Gson


class PreferenceUtils(val sharedPreferences: SharedPreferences) {

    // user credential

    fun saveUser(userModel: UserModel) {
        val json = Gson().toJson(userModel)
        sharedPreferences.edit().putString(Constant.USER_DATA, json).apply()

    }

    fun getUser(): UserModel? {
        return Gson().fromJson(sharedPreferences.getString(Constant.USER_DATA, ""), UserModel::class.java)
    }


    //for save platform wise user_name
//
//    fun savePlatFormUserName(platformuser: DataUserName) {
//        val json = Gson().toJson(platformuser)
//        sharedPreferences.edit().putString(Constant.PLATFORM_USER, json).apply()
//    }
//
//    fun getPlatFormUser(): DataUserName {
//        return Gson().fromJson(
//             sharedPreferences.getString(Constant.PLATFORM_USER, ""),
//             DataUserName::class.java
//        ) ?: DataUserName()
//    }
//
//    // for show recent post
//    fun saveRecentQuotes(recentQuote: DataRecentQuote) {
//        val json = Gson().toJson(recentQuote)
//        sharedPreferences.edit().putString(Constant.PLATFORM_USER, json).apply()
//    }
//
//    fun getRecentQuotes(): DataRecentQuote {
//        return Gson().fromJson(
//            sharedPreferences.getString(Constant.PLATFORM_USER, ""),
//            DataRecentQuote::class.java
//        ) ?: DataRecentQuote()
//    }



   // check premium or not
    fun saveIsPremiun(isPremium : Boolean){
            sharedPreferences.edit().putBoolean(Constant.PREMIUM,isPremium).apply()
    }
    fun getIsPremium():Boolean{
        return sharedPreferences.getBoolean(Constant.PREMIUM,false)
    }

    // show viewPager when first open app
    fun saveViewPager(isShown : Boolean){
        sharedPreferences.edit().putBoolean(Constant.VIEW_PAGER,isShown).apply()
    }

    fun getViewPager():Boolean{
        return sharedPreferences.getBoolean(Constant.VIEW_PAGER,false)
    }


    // save post for recent post
    fun saveRecentPost(recentPost: String) {
        sharedPreferences.edit().putString(Constant.RECENT_POST, recentPost).apply()
    }

    fun getRecentPost(): DataRecentPost {
        return Gson().fromJson(
            sharedPreferences.getString(Constant.RECENT_POST, ""),
            DataRecentPost::class.java
        ) ?: DataRecentPost()
    }

    fun saveSelectedPost(arrayList: ArrayList<String>){
        sharedPreferences.edit().putString(Constant.SELECTED_POST, arrayList.toString()).apply()
    }

    fun deleteSelectedPost(){
        sharedPreferences.edit().clear().apply()
        sharedPreferences.edit().remove(Constant.SELECTED_POST).apply()
    }

    // for clear recentPost
    fun clearRecentPost(){
        sharedPreferences.edit().clear().apply()
        sharedPreferences.edit().remove(Constant.SELECTED_POST).apply()
    }


//    fun clear() {

//        sharedPreferences.edit().clear().apply()
//        sharedPreferences.edit().remove(CONSTANT_AUTH).apply()
//    }

}