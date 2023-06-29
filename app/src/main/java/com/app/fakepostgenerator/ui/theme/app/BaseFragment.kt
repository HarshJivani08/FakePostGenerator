package com.app.fakepostgenerator.ui.theme.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.grewon.qmaker.utils.PreferenceUtils

open class BaseFragment:Fragment() {

    lateinit var preferenceUtils : PreferenceUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceUtils = PreferenceUtils(requireActivity().getSharedPreferences("QMakerPreference",
            AppCompatActivity.MODE_PRIVATE
        ))
    }


}