package com.example.liveearhmap2026.onboarding;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.liveearhmap2026.dashboard.MainActivity
import com.example.liveearhmap2026.ads.AdManager
import com.example.liveearhmap2026.ads.RemoteConfig
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.databinding.ActivityIntroNewBinding

class IntroActivity : AppCompatActivity() {
    val binding : ActivityIntroNewBinding by lazy {
        ActivityIntroNewBinding.inflate(layoutInflater)
    }
    private lateinit var introadpater: IntroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        introadpater = IntroAdapter(this, object : PagerItemClick {
            override fun onNextClick() {
                Log.d("ITEEMCLICK", "next click")
                if (binding!!.viewPager.currentItem == introadpater.itemCount-1) {
                    movennext()
                }
                else {
                    binding!!.viewPager.currentItem++
                }
                Log.d("ITEEMCLICK", "currentItem " + binding.viewPager.currentItem)
            }

            override fun onSkipClick() {
                Log.d("ITEEMCLICK", "skip click")
                movennext()
            }

        }, RemoteConfig.isOn("INTRO_FULL_SCREEN_AD"))
        binding.viewPager.adapter = introadpater
    }

    fun movennext() {
        AdManager.showInterstitialAd(this, "INTRO_INTER") {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isFromSplash", true)
            startActivity(intent)
            finish()
        }
    }

    interface PagerItemClick {
        fun onNextClick()
        fun onSkipClick()
    }
}