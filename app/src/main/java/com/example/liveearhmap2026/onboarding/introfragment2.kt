package com.example.liveearhmap2026.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hudspeedometer.speedalerts.gpsspeedometer.free.UI.onboarding.BaseFragment
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.databinding.FragmentIntrofragment2Binding

class introfragment2 : BaseFragment() {
    private var listener: IntroActivity.PagerItemClick?=null
    fun setListener(listener: IntroActivity.PagerItemClick) {
        this.listener = listener
    }
    lateinit var binding: FragmentIntrofragment2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntrofragment2Binding.inflate(inflater, container, false)

         binding.btnNext.setOnClickListener {
            listener?.onNextClick()
        }

        return binding.root
    }

    companion object {
        fun newInstance(position: Int): introfragment2 {
            val fragment = introfragment2()
            val args = Bundle()
            args.putInt("POSITION", position)
            fragment.arguments = args
            return fragment
        }
    }
}