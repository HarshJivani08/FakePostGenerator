package com.grewon.qmaker.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.fakepostgenerator.R
import com.app.fakepostgenerator.databinding.FragmentHomeBinding
import com.app.fakepostgenerator.ui.theme.ui.FacebookPostActivity
import com.app.fakepostgenerator.ui.theme.ui.InstagramPostActivity
import com.app.fakepostgenerator.ui.theme.ui.MainActivity
import com.grewon.qmaker.ui.menu.MenuActivity
import com.tombayley.activitycircularreveal.CircularReveal

class HomeFragment() : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClick()
        (activity as MainActivity).setBottomSelected(0)
    }

    private fun setClick() {
        binding.imgMenu.setOnClickListener(this)
        binding.lNewPost.setOnClickListener(this)
        binding.lReviewPost.setOnClickListener(this)
        binding.lQuotePost.setOnClickListener(this)
        binding.lFakePost.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgMenu -> {
                val builder = CircularReveal.Builder(
                    requireActivity(),
                    binding.imgMenu,
                    Intent(requireContext(), MenuActivity::class.java),
                    1600
                ).apply {
                    revealColor = ContextCompat.getColor(
                        requireContext(),
                        R.color.light_sky
                    )
                }
                CircularReveal.presentActivity(builder)
            }
            R.id.lNewPost -> {
//                val intent = Intent(requireContext(), CreatePostActivity::class.java)
//                startActivity(intent)
            }
            R.id.lReviewPost -> {
//                val intent = Intent(requireContext(), ReviewPostActivity::class.java)
//                startActivity(intent)
            }
            R.id.lQuotePost -> {
//                val intent = Intent(requireActivity(), QuotePostActivity::class.java)
//                startActivity(intent)
            }
            R.id.lFakePost -> {
                val intent = Intent(requireActivity(), FacebookPostActivity::class.java)
                startActivity(intent)
            }
        }
    }
}