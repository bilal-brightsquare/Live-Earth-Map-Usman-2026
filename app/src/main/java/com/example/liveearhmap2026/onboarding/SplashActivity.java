package com.example.liveearhmap2026.onboarding;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.RenderMode;
import com.example.liveearhmap2026.BillingClassByBilal;
import com.example.liveearhmap2026.BillingClassByBilalNew;
import com.example.liveearhmap2026.InAppPurchaseActivity;
import com.example.liveearhmap2026.PremiumActivity;
import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.ads.Adutills;
import com.example.liveearhmap2026.ads.AppOpenManager;
import com.example.liveearhmap2026.ads.RemoteConfig;
import com.example.liveearhmap2026.dashboard.MainActivity;
import com.example.liveearhmap2026.language.langActivity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LottieAnimationView lottie = findViewById(R.id.lottie);
        lottie.setAnimation(R.raw.loading);
        lottie.loop(true);
        lottie.playAnimation();
        setUpAds();
        startTimer();
    }

    public void startTimer(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(!adShown)
                AdManager.showInterstitialAd(this, "SPLASH_INTER", new Runnable() {
                    @Override
                    public void run() {
                        forward();
                    }
                });
        }, 9000);
    }

    private void forward() {
        startActivity(new Intent(this, InAppPurchaseActivity.class).putExtra("ActivityType",  "SplashAd"));
//        startActivity(new Intent(this, langActivity.class));
        finish();
    }

    public static boolean adShown = false;
    public void setUpAds() {

        new BillingClassByBilalNew(this).initializeBilling((isPremium) -> {
            AdManager.IS_PREMIUM = isPremium;
            Log.d("Billing", "Premium: " + isPremium);
            if (isPremium) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            return Unit.INSTANCE;
        });


            AdManager.initialize(getApplication(), () -> {
            AdManager.preLoadAds(getApplication(), this, null, null, new Runnable() {
                @Override
                public void run() {
                    adShown = true;
                    forward();
                }
            });
            RemoteConfig.getRemote(FirebaseRemoteConfig.getInstance(),null);
        });
    }
}