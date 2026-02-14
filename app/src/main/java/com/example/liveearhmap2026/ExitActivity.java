package com.example.liveearhmap2026;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.language.AppLangAdapter;
import com.example.liveearhmap2026.language.Lang_Model2;
import com.google.android.material.button.MaterialButton;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.BuildConfig;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_dialog);

        Button button_yes = findViewById(R.id.btn_yes_exitdialog);
        AppCompatButton button_no = findViewById(R.id.btn_no_exitdialog);
        RatingBar ratingBar = findViewById(R.id.ratingBar_exitdialog);
        AppCompatImageButton button_closedialog = findViewById(R.id.btn_closedialoge);
        button_yes.setOnClickListener(view1 -> {
            finishAffinity();
        });
        button_no.setOnClickListener(view1 -> {
           onBackPressed();
        });
        button_closedialog.setOnClickListener(view1 -> {
            onBackPressed();
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (ratingBar.getRating() > 2) {
                    Toast.makeText(ExitActivity.this, "Thank you for Rating", Toast.LENGTH_SHORT).show();
                    String url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
                        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                        intent.addFlags(flags);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        url = "https://play.google.com/store/apps/details";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
                        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                        intent.addFlags(flags);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    private Boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
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