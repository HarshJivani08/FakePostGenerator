package com.grewon.qmaker.utils

import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.ui.theme.model.DataMenu


class ListUtils {

    companion object {



        fun getPostCountList(): ArrayList<String> {
            val data: ArrayList<String> = ArrayList()
            data.add("1")
            data.add("2")
            data.add("3")
            data.add("4")
            data.add("5")
            data.add("6")
            data.add("7")
            data.add("8")
            data.add("9")
            data.add("10")

            return data
        }


        fun addMenuList(): ArrayList<DataMenu> {
            val data: ArrayList<DataMenu> = ArrayList()

            data.add(DataMenu(R.drawable.ic_premium, "Get Premium"))
            data.add(DataMenu(R.drawable.ic_terms, "Term & Condition"))
            data.add(DataMenu(R.drawable.ic_privacy, "Privacy Policy"))
            data.add(DataMenu(R.drawable.ic_que, "FAQ’s"))
            data.add(DataMenu(R.drawable.ic_about, "About Us"))
            data.add(DataMenu(R.drawable.ic_contact, "Contact Us"))
            data.add(DataMenu(R.drawable.ic_share_app, "Share App"))
            data.add(DataMenu(R.drawable.ic_rate, "Rate Us"))
            data.add(DataMenu(R.drawable.ic_more_app, "More Apps"))

            return data
        }



    }

}