package com.example.liveearhmap2026.ads;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.example.liveearhmap2026.ads.Adutills.adIdAppOpen;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.liveearhmap2026.onboarding.SplashActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.BuildConfig;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    public static AppOpenAd appOpenAd;

    private Activity currentActivity;
    public static boolean isShowingAd = false;
    public boolean isfirsttime = true;
    private final Application myApplication;
    public AppOpenManager(Application myApplication, Runnable runnable) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        fetchAdFirstTime(runnable);
    }

    public void fetchAdFirstTime(Runnable runnable) {
        String id = adIdAppOpen;
        if (BuildConfig.DEBUG) {
            id = "ca-app-pub-3940256099942544/9257395921";
        }
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                runnable.run();
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                super.onAdLoaded(ad);
                appOpenAd = ad;
                showAdIfAvailable(runnable);
            }
        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, id, request, loadCallback);
    }

    public void fetchAd() {
        String id = adIdAppOpen;
        if (BuildConfig.DEBUG) {
            id = "ca-app-pub-3940256099942544/9257395921";
        }
        AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                super.onAdLoaded(ad);
                appOpenAd = ad;
            }
        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, id, request, loadCallback);
    }

    public void showAdIfAvailable(Runnable runnable) {
        if (!isShowingAd) {
            try {
                if (appOpenAd!=null) {
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            if (runnable != null) {
                                runnable.run();
                            }
                            // Set the reference to null so isAdAvailable() returns false.
                            isShowingAd = false;
                            Log.d("AdmobAdFloor", "App open Ad Dismissed");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            if (runnable != null) {
                                runnable.run();
                            }
                            Log.d("AdmobAdFloor", "App open ad failed to show full screen, error: " + adError.getMessage());

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                            SplashActivity.adShown = true;
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                            Log.d("AdmobAdFloor", "App open Ad Impression counted");
                            fetchAd();
                        }
                    });
                    appOpenAd.show(currentActivity);
                }
                else fetchAd();
            } catch (Exception e) {
                if (runnable != null) {
                    runnable.run();
                }
            }
        } else {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {}

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {}

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        Log.d("AdmobAdFloor", "App Start");
        if (!isfirsttime) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> showAdIfAvailable(null),400);
        }
        else {
            isfirsttime=false;
        }
    }

}
