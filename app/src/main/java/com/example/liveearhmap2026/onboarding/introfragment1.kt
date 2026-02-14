package com.example.liveearhmap2026.onboarding

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.liveearhmap2026.ads.AdManager
import com.example.liveearhmap2026.ads.Adutills
import com.example.liveearhmap2026.ads.RemoteConfig
import com.hudspeedometer.speedalerts.gpsspeedometer.free.UI.onboarding.BaseFragment
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.databinding.FragmentIntrofragment1Binding

class introfragment1 : BaseFragment() {
    private var listener: IntroActivity.PagerItemClick?=null
    fun setListener(listener: IntroActivity.PagerItemClick) {
        this.listener = listener
    }
    lateinit var binding: FragmentIntrofragment1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntrofragment1Binding.inflate(inflater, container, false)
        AdManager.showNativeAd(activity, binding.framelayoutnative, "INTRO_NATIVE_PLACEMENT",true)

         binding.btnNext.setOnClickListener {
            listener?.onNextClick()
        }

        return binding.root
    }

    companion object {
        fun newInstance(position: Int): introfragment1 {
            val fragment = introfragment1()
            val args = Bundle()
            args.putInt("POSITION", position)
            fragment.arguments = args
            return fragment
        }
    }
}