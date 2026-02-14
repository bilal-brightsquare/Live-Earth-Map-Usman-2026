package com.example.liveearhmap2026.ads;

import static com.example.liveearhmap2026.ads.AdManager.IS_PREMIUM;
import static com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_LEFT;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.BuildConfig;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.Objects;


public class Adutills {

    public static final String adIdBanner = "";
    public static final String adIdAppOpen = "ca-app-pub-9939755733300382/9597870873";
    public static final String adIdNative = "ca-app-pub-9939755733300382/9757652973";
    public static final String adIdInter = "ca-app-pub-9939755733300382/2262306334";
    public static NativeAd preLoadedAdmobNative = null;
    public static InterstitialAd preLoadedAdmobInter = null;

    public static void loadNativeAdRuntime(FrameLayout native_ad_frame, Activity activity, boolean showMedia) {
        if (!IS_PREMIUM) {
            String id = adIdNative;
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/2247696110";
            }
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_LEFT).build();

            AdLoader adLoader = new AdLoader.Builder(activity, id)
                    .forNativeAd(NativeAd -> {
                        inflateNativeAd(NativeAd, native_ad_frame, activity, showMedia);
                    })
                    .withAdListener(new com.google.android.gms.ads.AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            try {
                                if (native_ad_frame!=null)
                                    native_ad_frame.setVisibility(View.GONE);
                            }catch (Exception e){}
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                            Log.i("AdmobAdFloor", "native ad showed and impression counted");
                        }
                    })
                    .withNativeAdOptions(adOptions)
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public static void preLoadNativeAd(Activity activity, Runnable runnable) {
        String id = adIdNative;
        if (preLoadedAdmobNative == null) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/2247696110";
            }
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).setAdChoicesPlacement(ADCHOICES_TOP_LEFT).build();
            String finalId = id;
            AdLoader adLoader = new AdLoader.Builder(activity, id)
                    .forNativeAd(NativeAd -> {
                        preLoadedAdmobNative = NativeAd;
                        if (runnable != null) {
                            runnable.run();
                        }
                    })
                    .withAdListener(new com.google.android.gms.ads.AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                            Log.i("AdmobAdFloor", "Native failed, error: " + adError.getMessage() + "\nUnit Id: " + finalId);
                            if (runnable != null) {
                                runnable.run();
                            }

                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                            Log.i("AdmobAdFloor", "native ad showed and impression counted");
                        }
                    })
                    .withNativeAdOptions(adOptions)
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public static void showNativeAd(FrameLayout native_ad_frame, Activity activity, boolean showmedia) {
        if (!IS_PREMIUM) {
            NativeAd nativeAd = preLoadedAdmobNative;
            if (nativeAd != null) {
                inflateNativeAd(nativeAd, native_ad_frame, activity, showmedia);
                preLoadNativeAd(activity,null);
            }
            else
                loadNativeAdRuntime(native_ad_frame, activity, showmedia);
        }
        else {
            native_ad_frame.setVisibility(View.GONE);
        }

    }

    public static void inflateNativeAd(NativeAd nativeAd,FrameLayout native_ad_frame, Activity activity, boolean showmedia){
        if (nativeAd != null) {
            NativeAdView adView;
            adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_view, null);
            native_ad_frame.setVisibility(View.VISIBLE);

            adView.setBodyView(adView.findViewById(R.id.tv_body));
            if (showmedia) {
                adView.setMediaView(adView.findViewById(R.id.mediaview));
                adView.getMediaView().setVisibility(View.VISIBLE);
                if (nativeAd.getMediaContent() != null) {
                    Objects.requireNonNull(adView.getMediaView()).setMediaContent(nativeAd.getMediaContent());
                    adView.getMediaView().setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    adView.getMediaView().setVisibility(View.VISIBLE);
                }
            }

            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.GONE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
            adView.setHeadlineView(adView.findViewById(R.id.ad_advertiser));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));

            try {
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            }catch (Exception e){}

            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.GONE);
            } else {
                try {
                    Animation shake = AnimationUtils.loadAnimation(activity, R.anim.zoom_in_out);
                    adView.findViewById(R.id.ad_call_to_action).startAnimation(shake);
                }catch (Exception e){}
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
            adView.setNativeAd(nativeAd);

            if (showmedia) {
                VideoController vc = nativeAd.getMediaContent().getVideoController();
                if (vc.hasVideoContent()) {
                    vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                        @Override
                        public void onVideoEnd() {
                            super.onVideoEnd();
                        }
                    });
                }
            }
            native_ad_frame.removeAllViews();
            native_ad_frame.addView(adView);
            native_ad_frame.setVisibility(View.VISIBLE);
        }
        else  try {
            if (native_ad_frame!=null)
                native_ad_frame.setVisibility(View.GONE);
        }catch (Exception e){}
    }

    public static void loadInterstitialAdRuntime(Runnable runnable, Activity activity) {
        String id = adIdInter;
        if (BuildConfig.DEBUG) {
            id = "ca-app-pub-3940256099942544/1033173712";
        }
        if (!IS_PREMIUM) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, id, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    if (runnable != null) {
                                        runnable.run();
                                    }
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    if (runnable != null) {
                                        runnable.run();
                                    }
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                }
                            });
                            interstitialAd.show(activity);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        }
                    });
        }
        else if (runnable!=null)
            runnable.run();
    }

    public static void preLoadInterstitialAd(Context activity, Runnable runnable) {
        String id = adIdInter;
        if (preLoadedAdmobInter == null) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/1033173712";
            }
            AdRequest adRequest = new AdRequest.Builder().build();
            String finalId = id;
            InterstitialAd.load(activity, id, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            preLoadedAdmobInter = interstitialAd;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    });
        }
        else if (runnable!=null)
            runnable.run();
    }

    public static void showInterstitialAd(Runnable runnable, Activity activity) {
        InterstitialAd interstitialAd = preLoadedAdmobInter;
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    preLoadedAdmobInter = null;
                     preLoadInterstitialAd(activity,null);
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                }
            });
            interstitialAd.show(activity);
        }
        else {
            loadInterstitialAdRuntime(runnable,activity);
        }
    }

}