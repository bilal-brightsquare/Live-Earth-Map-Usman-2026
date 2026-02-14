package com.example.liveearhmap2026.modules;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.example.liveearhmap2026.newaddress_dialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.List;
import java.util.Objects;

public class ShareAddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    View customMarkerView;
    LinearLayout linearLayout_markerdetail;
    GoogleMap googleMap;
    boolean show = false;
    TextView textView_toolbar_title;
    AppCompatEditText editText_input;
    TextView textView_suggest_header, getTextView_suggest_sub;
    ProgressBar progressBar;
    CardView layoutCompat_suggestion;
    AppCompatImageButton button_voiceinput, button_premium, button_back, button_night, button_traffic,
            button_3d, button_satellite, button_default, button_zoomin, button_zoomout, button_location,
            button_bottomsheet_pin, button_bottomsheet_copy, button_bottomsheet_save, button_bottomsheet_share;
    Toolbar toolbar;
    ConstraintLayout layout_markerdetail;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareaddress_module);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_shareaddress);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        editText_input = findViewById(R.id.edittext_shareaddress);
        layoutCompat_suggestion = findViewById(R.id.layout_suggest_address_shareaddress);
        textView_suggest_header = findViewById(R.id.suggestheader_shareaddress);
        getTextView_suggest_sub = findViewById(R.id.suggestsub_shareaddress);
        progressBar = findViewById(R.id.progressbar_suggestlayout_shareaddress);
        toolbar = findViewById(R.id.toolbar_shareaddress);
        button_back = findViewById(R.id.button_shareaddress_backpress);
        button_premium = findViewById(R.id.button_shareaddress_premium);
        textView_toolbar_title = findViewById(R.id.toolbar_shareaddress_title);
        layout_markerdetail = findViewById(R.id.constraintLayout_markerdetail);

        button_voiceinput = findViewById(R.id.button_shareaddress_voiceinput);
        button_night = findViewById(R.id.button_shareaddress_night);
        button_traffic = findViewById(R.id.button_shareaddress_traffic);
        button_3d = findViewById(R.id.button_shareaddress_3d);
        button_satellite = findViewById(R.id.button_shareaddress_satellite);
        button_default = findViewById(R.id.button_shareaddress_default);
        button_zoomout = findViewById(R.id.button_shareaddress_zoom_out);
        button_zoomin = findViewById(R.id.button_shareaddress_zoom_in);
        button_location = findViewById(R.id.button_shareaddress_location);
        button_bottomsheet_pin = findViewById(R.id.bottomsheet_shareaddress_pin);
        button_bottomsheet_copy = findViewById(R.id.bottomsheet_shareaddress_copy);
        button_bottomsheet_save = findViewById(R.id.bottomsheet_shareaddress_save);
        button_bottomsheet_share = findViewById(R.id.bottomsheet_shareaddress_share);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_share_address);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bottomSheetDialog.show();

        all_click_listener();
        if (AdManager.IS_PREMIUM) {
            button_premium.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        detail_visibility();
        if (googleMap != null) {
            googleMap.setPadding(110, 0, 0, 0);
            mapdata_forall.input_textchange_checker(editText_input, this, layoutCompat_suggestion, textView_suggest_header, getTextView_suggest_sub, progressBar, googleMap, customMarkerView);
            googleMap.setOnMapClickListener(latLng -> {
                layout_markerdetail.setVisibility(View.GONE);
            });
            googleMap.setOnCameraMoveStartedListener(reason -> {
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    layout_markerdetail.setVisibility(View.GONE);
                }
            });
            googleMap.setOnMarkerClickListener(marker -> {
                layout_markerdetail.setVisibility(View.GONE);
                return true;
            });
        }
        map.setOnMarkerClickListener(marker -> {
            detail_visibility();
            return true;
        });
    }

    public void detail_visibility() {
        if (googleMap != null)
            googleMap.clear();
        customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        linearLayout_markerdetail = customMarkerView.findViewById(R.id.layout_marker_detail);
        show = !show;
        if (show)
            linearLayout_markerdetail.setVisibility(VISIBLE);
        else linearLayout_markerdetail.setVisibility(INVISIBLE);
        LatLng latLng = mapdata_forall.getlocation(ShareAddressActivity.this);
        if (latLng != null) {
            mapdata_forall.Marker_and_Camera(googleMap, latLng, ShareAddressActivity.this, customMarkerView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void all_click_listener() {
        button_back.setOnClickListener(view -> {
            onBackPressed();
        });
        button_voiceinput.setOnClickListener(view -> {
            map_buttons.voiceInput(this);
        });
        button_premium.setOnClickListener(view -> {
            startActivity(new Intent(this, InAppPurchaseActivity.class));

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
//            toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_shape));
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
            layout_markerdetail.setVisibility(View.GONE);
            googleMap.animateCamera(CameraUpdateFactory.zoomOut());
        });
        button_zoomin.setOnClickListener(view -> {
            layout_markerdetail.setVisibility(View.GONE);
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        });
        button_location.setOnClickListener(view -> {
            layout_markerdetail.setVisibility(VISIBLE);
            detail_visibility();
        });
        button_bottomsheet_pin.setOnClickListener(view -> {
            map_buttons.shareaddress_pin(this);
        });
        button_bottomsheet_copy.setOnClickListener(view -> {
            map_buttons.copy_address(this);
        });
        button_bottomsheet_save.setOnClickListener(view -> {
            newaddress_dialog.new_address_dialog(this);
        });
        button_bottomsheet_share.setOnClickListener(view -> {
            map_buttons.share_address(this);

        });
    }

    public void set_default_icons() {
        button_night.setImageResource(R.drawable.map_night_icon);
        button_traffic.setImageResource(R.drawable.map_traffic_icon);
        button_3d.setImageResource(R.drawable.map_three_d_icon);
        button_satellite.setImageResource(R.drawable.map_satellite_icon);
        button_default.setImageResource(R.drawable.map_default_icon);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == map_buttons.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            editText_input.setText(spokenText);
            mapdata_forall.hide_keyboard(this);
            mapdata_forall.set_geocoder_with_location_name(this, spokenText);
            LatLng latLng = mapdata_forall.getlatlong;
            if (latLng != null) {
                mapdata_forall.Marker_and_Camera(googleMap, latLng, ShareAddressActivity.this, customMarkerView);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        AdManager.showInterstitialAd(this, "SHAREADDRESS_BACKPRESS_STATUS", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ShareAddressActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}