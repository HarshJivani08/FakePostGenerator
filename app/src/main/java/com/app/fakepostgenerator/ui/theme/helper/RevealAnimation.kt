package com.app.fakepostgenerator.ui.theme.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter

import android.view.ViewAnimationUtils

import android.os.Build

import android.view.animation.AccelerateInterpolator
import android.app.Activity

import android.content.Intent
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener


class RevealAnimation {

    val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
    val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"

    private var mView: View? = null
    private var mActivity: Activity? = null

    private var revealX = 0
    private var revealY = 0

    fun RevealAnimation(view: View, intent: Intent, activity: Activity?) {
        mView = view
        mActivity = activity


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
            intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)
        ) {
            view.visibility = View.INVISIBLE
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
            val viewTreeObserver = view.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        revealActivity(revealX, revealY)
                        mView!!.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    }
                })
            }
        } else {

            //if you are below android 5 it jist shows the activity
            view.visibility = View.VISIBLE
        }
    }

    fun revealActivity(x: Int, y: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = (Math.max(mView!!.width, mView!!.height) * 1.1).toFloat()

            // create the animator for this view (the start radius is zero)
            val circularReveal =
                ViewAnimationUtils.createCircularReveal(mView, x, y, 0f, finalRadius)
            circularReveal.duration = 300
            circularReveal.interpolator = AccelerateInterpolator()

            // make the view visible and start the animation
            mView!!.visibility = View.VISIBLE
            circularReveal.start()
        } else {
            mActivity!!.finish()
        }
    }

    fun unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mActivity!!.finish()
        } else {
            val finalRadius = (Math.max(mView!!.width, mView!!.height) * 1.1).toFloat()
            val circularReveal = ViewAnimationUtils.createCircularReveal(
                mView, revealX, revealY, finalRadius, 0f
            )
            circularReveal.duration = 300
            circularReveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mView!!.visibility = View.INVISIBLE
                    mActivity!!.finish()
                    mActivity!!.overridePendingTransition(0, 0)
                }
            })
            circularReveal.start()
        }
    }


}