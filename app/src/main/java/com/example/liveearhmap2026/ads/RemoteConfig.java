package com.example.liveearhmap2026.ads;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jetbrains.annotations.NotNull;

public class RemoteConfig {

    public static FirebaseRemoteConfig REMOTE_CONFIG;

    public static void getRemote(FirebaseRemoteConfig fbRemoteConfig, Runnable runnable){
        FirebaseRemoteConfigSettings.Builder configBuilder = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1);
        fbRemoteConfig.setConfigSettingsAsync(configBuilder.build());
        fbRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Boolean> task) {
                RemoteConfig.REMOTE_CONFIG = fbRemoteConfig;
                if (runnable!=null)
                    runnable.run();
            }
        });
        fbRemoteConfig.fetchAndActivate().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (runnable!=null)
                    runnable.run();
            }
        });
    }

    public static boolean isOn(String name){
        if (REMOTE_CONFIG==null)
            return true;
        else if (REMOTE_CONFIG.getString(name).equalsIgnoreCase("off"))
            return false;
        else
            return true;
    }

}