package com.example.liveearhmap2026

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.liveearhmap2026.BillingClassByBilalNew.Companion.isPurchased
import com.example.liveearhmap2026.BillingClassByBilal.Companion.productID
import com.example.liveearhmap2026.BillingClassByBilalNew.Companion.isPurchased
import com.example.liveearhmap2026.ads.AdManager
import com.example.liveearhmap2026.ads.Adutills
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.databinding.ActivityPremiumBinding
import kotlin.jvm.java

class PremiumActivity : AppCompatActivity() {
    val bining: ActivityPremiumBinding by lazy {
        ActivityPremiumBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(bining.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        try {
            if (BillingClassByBilal.priceslist.isNotEmpty() && BillingClassByBilal.titlelist.isNotEmpty()
            ) {
                bining.monthlyTitle.setText(BillingClassByBilal.titlelist.get(0))
                bining.weeklyTitle.setText(BillingClassByBilal.titlelist.get(1))
                bining.monthlyPrice.setText(BillingClassByBilal.priceslist.get(0))
                bining.weeklyPrice.setText(BillingClassByBilal.priceslist.get(1))
            } else {
                Log.e("BillingClassByBilal", "list null")
            }
        } catch (exc: Exception) {
            Log.e("BillingClassByBilal", "list exc$exc")
        }

        bining.premiumMonthlybtn.setOnClickListener {
            productID = "monthly"
            bining.premiumCheckmonth.isChecked = true
            bining.premiumCheckweek.isChecked = false
        }

        bining.premiumWeeklybtn.setOnClickListener {
            bining.premiumCheckweek.isChecked = true
            bining.premiumCheckmonth.isChecked = false
            productID = "weekly"
        }

        bining.next.setOnClickListener {
            BillingClassByBilal(this).purchasePremium {
                isPurchased = it
                if (it) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("key", "SomeResult")
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            }
        }

        bining.btncross.setOnClickListener {
            movenext()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                movenext()
            }
        })

    }

    fun movenext() {
        AdManager.showInterstitialAd(this,"PREMIUM_INTER", {
            finish()
        })
    }

}