package com.example.liveearhmap2026.modules;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.liveearhmap2026.InAppPurchaseActivity;
import com.example.liveearhmap2026.PremiumActivity;
import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.dashboard.MainActivity;
import com.example.liveearhmap2026.map_buttons;
import com.example.liveearhmap2026.mapdata_forall;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.List;
import java.util.Objects;

public class LiveEarthMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    View customMarkerView;
    LinearLayout linearLayout_markerdetail, enableLiveView;
    GoogleMap googleMap;
    boolean show = false;
    AppCompatEditText editText_input;
    TextView textView_suggest_header, getTextView_suggest_sub;
    ProgressBar progressBar;
    CardView layoutCompat_suggestion;
    AppCompatImageButton button_voiceinput, button_premium, button_back, button_shareaddress, button_night, button_traffic, button_3d, button_satellite, button_default, button_zoomin, button_zoomout, button_location;
    Toolbar toolbar;
    TextView textView_toolbar_title;
    ConstraintLayout layout_markerdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_earth_map_module);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_liveearthmap);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        editText_input = findViewById(R.id.edittext_liveearthmap);
        enableLiveView = findViewById(R.id.btn_live_view);
        toolbar = findViewById(R.id.toolbar_liveearthmap);
        layoutCompat_suggestion = findViewById(R.id.layout_suggest_address_liveearthmap);
        textView_suggest_header = findViewById(R.id.suggestheader_liveearthmap);
        getTextView_suggest_sub = findViewById(R.id.suggestsub_liveearthmap);
        progressBar = findViewById(R.id.progressbar_suggestlayout_liveearthmap);
        button_back = findViewById(R.id.button_liveearthmap_backpress);
        button_premium = findViewById(R.id.button_liveearthmap_premium);
        button_voiceinput = findViewById(R.id.button_liveearthmap_voiceinput);
        button_shareaddress = findViewById(R.id.button_liveearthmap_share);
        button_night = findViewById(R.id.button_liveearthmap_night);
        layout_markerdetail = findViewById(R.id.constraintLayout_markerdetail);

        button_traffic = findViewById(R.id.button_liveearthmap_traffic);
        button_3d = findViewById(R.id.button_liveearthmap_3d);
        button_satellite = findViewById(R.id.button_liveearthmap_satellite);
        button_default = findViewById(R.id.button_liveearthmap_default);
        button_zoomout = findViewById(R.id.button_liveearthmap_zoom_out);
        button_zoomin = findViewById(R.id.button_liveearthmap_zoom_in);
        button_location = findViewById(R.id.button_liveearthmap_location);
        textView_toolbar_title = findViewById(R.id.toolbar_liveearthmap_title);
        all_click_listener();

        if (AdManager.IS_PREMIUM) {
            button_premium.setVisibility(GONE);
            enableLiveView.setVisibility(GONE);
        }

        enableLiveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LiveEarthMapActivity.this, InAppPurchaseActivity.class));
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser(this);
        }

    }

    public void showGPSDisabledAlertToUser(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_gpspermission);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



        ((View) dialog.findViewById(R.id.gps_setting_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(callGPSSettingIntent);
            }
        });
        ((View) dialog.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void detail_visibility() {
        if (googleMap != null) {
            googleMap.clear();
            customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
            linearLayout_markerdetail = customMarkerView.findViewById(R.id.layout_marker_detail);
            show = !show;
            if (show)
                linearLayout_markerdetail.setVisibility(VISIBLE);
            else linearLayout_markerdetail.setVisibility(INVISIBLE);
            LatLng latLng = mapdata_forall.getlocation(LiveEarthMapActivity.this);
            if (latLng != null) {
                mapdata_forall.Marker_and_Camera(googleMap, latLng, LiveEarthMapActivity.this, customMarkerView);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        detail_visibility();
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(40.7363124, -73.9748107), 17));
            layout_markerdetail.setVisibility(GONE);
            mapdata_forall.getlocation(LiveEarthMapActivity.this);
            googleMap.setPadding(0, 0, 0, 0);
            mapdata_forall.input_textchange_checker(editText_input, this, layoutCompat_suggestion, textView_suggest_header, getTextView_suggest_sub, progressBar, googleMap, customMarkerView);

            googleMap.setOnMapClickListener(latLng -> {
                layout_markerdetail.setVisibility(GONE);
            });
            googleMap.setOnCameraMoveStartedListener(reason -> {
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    layout_markerdetail.setVisibility(GONE);
                }
            });
            googleMap.setOnMarkerClickListener(marker -> {
                layout_markerdetail.setVisibility(GONE);
                return true;
            });
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void all_click_listener() {
        button_back.setOnClickListener(view -> {
            onBackPressed();
        });
        button_voiceinput.setOnClickListener(view -> {
            map_buttons.voiceInput(this);
        });
        button_premium.setOnClickListener(view -> {
            startActivity(new Intent(this, InAppPurchaseActivity.class));

        });
        button_shareaddress.setOnClickListener(view -> {
            map_buttons.share_address(this);
        });
        button_night.setOnClickListener(view -> {
            set_default_icons();
            button_night.setImageResource(R.drawable.map_night_icon_selected);
            map_buttons.setNight_status(googleMap, this);
        });
        button_traffic.setOnClickListener(view -> {
            set_default_icons();
            button_traffic.setImageResource(R.drawable.map_traffic_icon_selected);
            map_buttons.setTraffic_status(googleMap, this);
        });
        button_3d.setOnClickListener(view -> {
            set_default_icons();
            button_3d.setImageResource(R.drawable.map_three_d_icon_selected);
            map_buttons.setThree_d_status(googleMap, this);

        });
        button_satellite.setOnClickListener(view -> {
            set_default_icons();
            button_satellite.setImageResource(R.drawable.map_satellite_icon_selected);
            map_buttons.setSattelite_status(googleMap, this);
        });
        button_default.setOnClickListener(view -> {
            set_default_icons();
            button_default.setImageResource(R.drawable.map_default_icon_selected);
            map_buttons.setDefault_status(googleMap, this);
        });
        button_zoomout.setOnClickListener(view -> {
            layout_markerdetail.setVisibility(GONE);
            googleMap.animateCamera(CameraUpdateFactory.zoomOut());
        });
        button_zoomin.setOnClickListener(view -> {
            layout_markerdetail.setVisibility(GONE);
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        });

        button_location.setOnClickListener(view -> {
            detail_visibility();
            layout_markerdetail.setVisibility(View.VISIBLE);
        });

    }

    public void set_default_icons() {
        button_night.setImageResource(R.drawable.map_night_icon);
        button_traffic.setImageResource(R.drawable.map_traffic_icon);
        button_3d.setImageResource(R.drawable.map_three_d_icon);
        button_satellite.setImageResource(R.drawable.map_satellite_icon);
        button_default.setImageResource(R.drawable.map_default_icon);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == map_buttons.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            editText_input.setText(spokenText);
            mapdata_forall.hide_keyboard(this);
            mapdata_forall.set_geocoder_with_location_name(this, spokenText);
            LatLng latLng = mapdata_forall.getlatlong;
            if (latLng != null) {
                mapdata_forall.Marker_and_Camera(googleMap, latLng, LiveEarthMapActivity.this, customMarkerView);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        AdManager.showInterstitialAd(this, "EARTHMAP_BACKPRESS_STATUS", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LiveEarthMapActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}