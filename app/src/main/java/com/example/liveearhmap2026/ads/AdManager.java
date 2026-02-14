package com.example.liveearhmap2026.ads;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;

public class AdManager {

    public static boolean IS_PREMIUM = false;

    public static void initialize(Application application, Runnable runnable){
        FirebaseApp.initializeApp(application);
        MobileAds.initialize(application);
        runnable.run();
    }
    public static void preLoadAds(Application application, Activity activity, Runnable interRunnable, Runnable nativeRunnable, Runnable appOpenRunnable) {
        Adutills.preLoadNativeAd(activity,nativeRunnable);
        Adutills.preLoadInterstitialAd(activity,interRunnable);
        new AppOpenManager(application, appOpenRunnable);
    }

    public static void showInterstitialAd(Activity activity, String placementName, Runnable runnable) {
        boolean isOn = RemoteConfig.isOn(placementName);
        if (isOn && !IS_PREMIUM)
            Adutills.showInterstitialAd(runnable,activity);
        else if (runnable != null)
            runnable.run();
    }
    public static void showNativeAd(Activity activity, FrameLayout frameLayout, String placementName, boolean showMedia) {
        boolean isOn = RemoteConfig.isOn(placementName);
        if (isOn && !IS_PREMIUM)
            Adutills.showNativeAd(frameLayout,activity,showMedia);
        else frameLayout.setVisibility(View.GONE);
    }

}
