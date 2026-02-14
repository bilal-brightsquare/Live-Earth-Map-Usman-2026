package com.example.liveearhmap2026.onboarding

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.liveearhmap2026.ads.AdManager
import com.example.liveearhmap2026.ads.Adutills
import com.example.liveearhmap2026.ads.RemoteConfig
import com.hudspeedometer.speedalerts.gpsspeedometer.free.UI.onboarding.BaseFragment
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.databinding.FullscreenadFragmentBinding

class introfragment_fullscreen : BaseFragment() {
    private var listener: IntroActivity.PagerItemClick?=null
    fun setListener(listener: IntroActivity.PagerItemClick) {
        this.listener = listener
    }
    private val handlerdelay = Handler(Looper.getMainLooper())

    lateinit var binding: FullscreenadFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FullscreenadFragmentBinding.inflate(inflater, container, false)

        AdManager.showNativeAd(activity, binding.nativelayout, "INTRO_FULL_SCREEN_AD",true)

        if (RemoteConfig.isOn("INTRO_FULL_SCREEN_AD")){
            handlerdelay.postDelayed({
                binding.btncross.visible()
            }, 2500)
        }
        else{
            handlerdelay.removeCallbacksAndMessages(null)
            listener?.onNextClick()
            binding.nativelayout.gone()
        }
         binding.btncross.setOnClickListener {
             handlerdelay.removeCallbacksAndMessages(null)
             listener?.onNextClick()
        }

        return binding.root
    }

    companion object {
        fun newInstance(position: Int): introfragment_fullscreen {
            val fragment = introfragment_fullscreen()
            val args = Bundle()
            args.putInt("POSITION", position)
            fragment.arguments = args
            return fragment
        }
    }
}