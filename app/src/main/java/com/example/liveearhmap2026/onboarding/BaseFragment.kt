package com.hudspeedometer.speedalerts.gpsspeedometer.free.UI.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    fun showAd(placement: String): Boolean {
        return placement.equals("ON", ignoreCase = true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("BaseFragmentLOG", "BASEFRAGMENTONDESTROY")
    }

}