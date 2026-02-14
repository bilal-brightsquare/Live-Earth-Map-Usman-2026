package com.example.liveearhmap2026.modules;

import static android.view.View.GONE;
import static com.example.liveearhmap2026.mapdata_forall.addressline;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.CarrierConfigManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import com.example.liveearhmap2026.InAppPurchaseActivity;
import com.example.liveearhmap2026.PremiumActivity;
import com.example.liveearhmap2026.SQLite_Class;
import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.dashboard.MainActivity;
import com.example.liveearhmap2026.map_buttons;
import com.example.liveearhmap2026.mapdata_forall;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GpsNavigationActivity extends AppCompatActivity implements OnMapReadyCallback {
    View customMarkerView;
    GoogleMap googleMap;
    LinearLayout enableLiveView;

    FragmentContainerView fragmentContainerView;
    boolean show = false;
    AppCompatEditText editText_input;
    TextView textView_suggest_header, getTextView_suggest_sub;
    ProgressBar progressBar;
    CardView layoutCompat_suggestion;
    AppCompatImageButton button_saveaddress, button_voiceinput, button_premium, button_back, button_shareaddress, button_night, button_traffic,
            button_3d, button_satellite, button_default, button_zoomin, button_zoomout, button_location, button_bike, button_car, button_cycle, button_walk, button_navigation;
    Toolbar toolbar;
    TextView textView_toolbar_title;
    String id;
    ConstraintLayout layout_markerdetail;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_navigation_module);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_gpsnavigation);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        fragmentContainerView = findViewById(R.id.map_gpsnavigation);
        customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        editText_input = findViewById(R.id.edittext_gpsnavigation);
        enableLiveView = findViewById(R.id.btn_live_view);

        layoutCompat_suggestion = findViewById(R.id.layout_suggest_address_gpsnavigation);
        textView_suggest_header = findViewById(R.id.suggestheader_gpsnavigation);
        textView_toolbar_title = findViewById(R.id.toolbar_gpsnavigation_title);
        getTextView_suggest_sub = findViewById(R.id.suggestsub_gpsnavigation);
        progressBar = findViewById(R.id.progressbar_suggestlayout_gpsnavigation);
        toolbar = findViewById(R.id.toolbar_gpsnavigation);
        button_back = findViewById(R.id.button_gpsnavigation_backpress);
        button_premium = findViewById(R.id.button_gpsnavigation_premium);
        button_saveaddress = findViewById(R.id.button_gpsnavigation_saveaddress);
        button_voiceinput = findViewById(R.id.button_gpsnavigation_voiceinput);
        button_shareaddress = findViewById(R.id.button_gpsnavigation_share);
        button_night = findViewById(R.id.button_gpsnavigation_night);
        button_traffic = findViewById(R.id.button_gpsnavigation_traffic);
        button_3d = findViewById(R.id.button_gpsnavigation_3d);
        button_satellite = findViewById(R.id.button_gpsnavigation_satellite);
        button_default = findViewById(R.id.button_gpsnavigation_default);
        button_zoomout = findViewById(R.id.button_gpsnavigation_zoomout);
        button_zoomin = findViewById(R.id.button_gpsnavigation_zoomin);
        button_location = findViewById(R.id.button_gpsnavigation_location);
        button_car = findViewById(R.id.button_gpsnavigation_car);
        button_bike = findViewById(R.id.button_gpsnavigation_bike);
        button_cycle = findViewById(R.id.button_gpsnavigation_bicycle);
        button_walk = findViewById(R.id.button_gpsnavigation_walk);
        button_navigation = findViewById(R.id.button_gpsnavigation_navigation);
        layout_markerdetail = findViewById(R.id.constraintLayout_markerdetail);
        try {
            Date date = new Date();
            id = (String) DateFormat.format("yyyyMMddHHmmss", date);
        } catch (Exception ignore) {
        }
        all_click_listener();

        enableLiveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GpsNavigationActivity.this, InAppPurchaseActivity.class));
            }
        });
        if (AdManager.IS_PREMIUM) {
            enableLiveView.setVisibility(GONE);
        }

    }

    public void detail_visibility() {
        if (googleMap != null)
            googleMap.clear();
        customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_map_marker, null);
        LatLng latLng = mapdata_forall.getlocation(GpsNavigationActivity.this);
        if (latLng != null) {
            mapdata_forall.Marker_and_Camera(googleMap, latLng, GpsNavigationActivity.this, customMarkerView);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        detail_visibility();
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapdata_forall.input_textchange_checker(editText_input, this, layoutCompat_suggestion, textView_suggest_header, getTextView_suggest_sub, progressBar, googleMap, customMarkerView);
            googleMap.setOnMapClickListener(latLng -> {
                layout_markerdetail.setVisibility(View.GONE);
            });
            googleMap.setOnMarkerClickListener(marker -> {
                layout_markerdetail.setVisibility(View.GONE);
                return true;
            });
        }
        assert googleMap != null;
        googleMap.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                layout_markerdetail.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void all_click_listener() {
        button_back.setOnClickListener(view -> {
            onBackPressed();
        });
        button_saveaddress.setOnClickListener(view -> {
            try {
                SQLite_Class.create_db(this);
                SQLite_Class.create_table(this);
                try {
                    SQLite_Class.insert_data("unknown", addressline, String.valueOf(mapdata_forall.getlatlong.latitude), String.valueOf(mapdata_forall.getlatlong.longitude), id);
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
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
            googleMap.animateCamera(CameraUpdateFactory.zoomOut());
            layout_markerdetail.setVisibility(View.GONE);

        });
        button_zoomin.setOnClickListener(view -> {
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            layout_markerdetail.setVisibility(View.GONE);
        });
        button_location.setOnClickListener(view -> {
            layout_markerdetail.setVisibility(View.VISIBLE);
            layout_markerdetail.setVisibility(View.GONE);
            detail_visibility();
        });
        button_car.setOnClickListener(view -> {
            set_default_vehicle_type();
            button_car.setImageResource(R.drawable.car_icon_gpsnavigation_selected);
            map_buttons.DRIVING_TYPE = "driving";
        });
        button_bike.setOnClickListener(view -> {
            set_default_vehicle_type();
            button_bike.setImageResource(R.drawable.bike_icon_gpsnavigation_selected);
            map_buttons.DRIVING_TYPE = "driving";
        });
        button_cycle.setOnClickListener(view -> {
            set_default_vehicle_type();
            button_cycle.setImageResource(R.drawable.bicycle_icon_gpsnavigation_selected);
            map_buttons.DRIVING_TYPE = "bicycling";
        });
        button_walk.setOnClickListener(view -> {
            set_default_vehicle_type();
            button_walk.setImageResource(R.drawable.walk_icon_gpsnavigation_selected);
            map_buttons.DRIVING_TYPE = "walking";
        });
        button_navigation.setOnClickListener(view -> {
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.googlemap_dialog);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.show();
            AppCompatButton button1=dialog.findViewById(R.id.btn_googlemap_continue);
            AppCompatButton button2=dialog.findViewById(R.id.btn_googlemap_cancel);
            button1.setOnClickListener(view1 -> {
                map_buttons.navigation(this);
                dialog.dismiss();
            });
              button2.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
        });
    }

    public void set_default_vehicle_type() {
        button_car.setImageResource(R.drawable.car_icon_gpsnavigation);
        button_bike.setImageResource(R.drawable.bike_icon_gpsnavigation);
        button_cycle.setImageResource(R.drawable.bicycle_icon_gpsnavigation);
        button_walk.setImageResource(R.drawable.padestrial_icon_gpsnavigation);
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
                mapdata_forall.Marker_and_Camera(googleMap, latLng, GpsNavigationActivity.this, customMarkerView);
            }
            // Do something with spokenText.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        AdManager.showInterstitialAd(this, "GPSNAV_BACKPRESS_STATUS", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(GpsNavigationActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}