package com.example.liveearhmap2026.language;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.onboarding.IntroActivity;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.ArrayList;
import java.util.List;

public class langActivity extends AppCompatActivity {
    AppLangAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang);

        recyclerView = findViewById(R.id.recyclerview);
        findViewById(R.id.btn_continue).setOnClickListener(v -> {
            AdManager.showInterstitialAd(this, "LANG_INTER", new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(langActivity.this, IntroActivity.class));
                    finish();
                }
            });
        });

        findViewById(R.id.backpress).setOnClickListener(v -> {
            AdManager.showInterstitialAd(this, "LANG_INTER", new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(langActivity.this, IntroActivity.class));
                    finish();
                }
            });
        });

        initLangData();
        adapter = new AppLangAdapter(this, langList);
        recyclerView.setAdapter(adapter);
        AdManager.showNativeAd(langActivity.this, findViewById(R.id.framelayout),"LANG_NATIVE_PLACEMENT",false);
    }


    List<Lang_Model2> langList;
    public void initLangData() {
        langList = new ArrayList<>();
        langList.add(new Lang_Model2("English (US/UK)", "us", R.drawable.ic_flag_us, true));
        langList.add(new Lang_Model2("Arabic (عربي)", "ar", R.drawable.ic_saudi_arabia, false));
        langList.add(new Lang_Model2("Chinese (中國人)", "zh", R.drawable.ic_china, false));
        langList.add(new Lang_Model2("French(Français)", "fr", R.drawable.ic_france, false));
        langList.add(new Lang_Model2("German (Deutsch)", "de", R.drawable.ic_germany, false));
        langList.add(new Lang_Model2("Hindi (हिंदी)", "hi", R.drawable.ic_india, false));
        langList.add(new Lang_Model2("Japanese (日本)", "ja", R.drawable.ic_japan, false));
        langList.add(new Lang_Model2("Spanish (Español) ", "es", R.drawable.ic_spain, false));
        langList.add(new Lang_Model2("Turkish (Türkçe)", "tr", R.drawable.ic_turkey, false));
        langList.add(new Lang_Model2("Urdu (اردو)", "ur", R.drawable.ic_pakistan, false));
    }
}