package com.example.liveearhmap2026.onboarding;

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroAdapter(
    val  activity: IntroActivity,
    val listener: IntroActivity.PagerItemClick,
    val showfullad: Boolean=false
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return if (showfullad) {
            4
        } else {
            3
        }
    }

    override fun createFragment(position: Int): Fragment {
        if (showfullad) {
            when (position) {
                0 -> {
                    val fragment = introfragment1.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }

                1 -> {
                    val fragment = introfragment2.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }

                2 -> {
                    val fragment = introfragment_fullscreen.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }

                3 -> {
                    val fragment = introfragment3.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }
                else -> {
                    val fragment = introfragment1.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }
            }
        }
        else {
            when (position) {
                0 -> {
                    val fragment = introfragment1.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }

                1 -> {
                    val fragment = introfragment2.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }

                2 -> {
                    val fragment = introfragment3.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }
                else -> {
                    val fragment = introfragment1.newInstance(position)
                    fragment.setListener(listener)
                    return fragment
                }
            }
        }
    }

}