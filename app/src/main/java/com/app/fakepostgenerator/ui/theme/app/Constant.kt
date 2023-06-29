package com.app.fakepostgenerator.ui.theme.app

import android.os.Environment

class Constant {
    companion object {

        const val CAMERA_PERMISSION_REQUEST_CODE: Int = 802
        const val CAMERA_PERMISSION_CODE = 100
        const val STORAGE_PERMISSION_CODE = 101
        const val GALLERY_PERMISSION_REQUEST_CODE: Int = 803

        const val CAMERA_INTENT_REQUEST_CODE: Int = 800
        const val GALLERY_INTENT_REQUEST_CODE: Int = 801

        const val STORAGE_PERMISSION_REQUEST_CODE :Int = 100

        const val USER_DATA = "userdata"
        const val TAG = "tag"

        const val PLATFORM_USER = "platformuser"


         val FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/QMaker/"

         val RECENT_POST = "recent_post"

        val SELECTED_POST = "selected_post"

        const val DATA_QUOTE = "quote"

        const val FIREBASE_TOKEN = "fb_token"

        const val PRIVACY_POLICY = "https://sites.google.com/view/qmaker-privacy-policy"
        const val TERMS_CONDITION = "https://sites.google.com/view/qmaker-terms-and-condition"
        const val EMAIL = "grewon.technologies@gmail.com"

        const val PREMIUM = "premium"

        const val VIEW_PAGER = "viewpager"

        const val APP_DATA: String = "notes_list"

       const val IN_APP_PRODUCT_ID = "app.qmaker.lifetime"
//        const val IN_APP_PRODUCT_ID = "product.purchase.lifetime"
 //       const val IN_APP_PRODUCT_ID = "android.test.purchased"

    }

}