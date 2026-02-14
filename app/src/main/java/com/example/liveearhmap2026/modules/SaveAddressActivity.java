package com.example.liveearhmap2026.modules;

import static com.example.liveearhmap2026.SQLite_Class.list_detail;
import static com.example.liveearhmap2026.SQLite_Class.list_ids;
import static com.example.liveearhmap2026.SQLite_Class.list_latitude;
import static com.example.liveearhmap2026.SQLite_Class.list_longitude;
import static com.example.liveearhmap2026.SQLite_Class.list_title;
import static com.example.liveearhmap2026.mapdata_forall.addressline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liveearhmap2026.InAppPurchaseActivity;
import com.example.liveearhmap2026.PremiumActivity;
import com.example.liveearhmap2026.SQLite_Class;
import com.example.liveearhmap2026.adapter_saveaddress;
import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.dashboard.MainActivity;
import com.example.liveearhmap2026.map_buttons;
import com.example.liveearhmap2026.mapdata_forall;
import com.example.liveearhmap2026.newaddress_dialog;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.List;
import java.util.Locale;

public class SaveAddressActivity extends AppCompatActivity {
    public static RecyclerView recyclerView;
    AppCompatImageButton button_back, button_premium, button_bottomsheet_pin, button_bottomsheet_copy, button_bottomsheet_navigate, button_bottomsheet_share;
    public static boolean itemclick_check = false;

    //    public static String addressline;
    FloatingActionButton button_addnew;
    BottomSheetDialog bottomSheetDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveaddress_module);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_save_address);
        recyclerView = findViewById(R.id.saveaddress_recyclerview);
        button_back = findViewById(R.id.button_saveaddress_backpress);
        button_premium = findViewById(R.id.button_saveaddress_premium);
        button_bottomsheet_pin = findViewById(R.id.bottomsheet_saveaddress_pin);
        button_bottomsheet_copy = findViewById(R.id.bottomsheet_saveaddress_copy);
        button_bottomsheet_navigate = findViewById(R.id.bottomsheet_saveaddress_navigate);
        button_bottomsheet_share = findViewById(R.id.bottomsheet_saveaddress_share);
        button_addnew = findViewById(R.id.button_saveaddress_addnew_address);
        recyclerView.setLayoutManager(new LinearLayoutManager(SaveAddressActivity.this));
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(
                bottomSheetDialog::cancel, 2000);
        allclick_listner();
        if (AdManager.IS_PREMIUM) {
            button_premium.setVisibility(View.GONE);
        }
        get_addressline();
        SQLite_Class.create_db(this);
        SQLite_Class.create_table(this);
        SQLite_Class.getall_addresses(this);
        setadapter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void allclick_listner() {
        button_back.setOnClickListener(view -> {
            onBackPressed();
        });
        button_premium.setOnClickListener(view -> {
            startActivity(new Intent(this, InAppPurchaseActivity.class));
        });
        button_bottomsheet_pin.setOnClickListener(view -> {
            if (itemclick_check)
                map_buttons.shareaddress_pin_savemodule(this);
            else show_toast_error();
        });
        button_bottomsheet_copy.setOnClickListener(view -> {
            if (itemclick_check)
                map_buttons.copy_address_savemodule(this);
            else show_toast_error();

        });
        button_bottomsheet_navigate.setOnClickListener(view -> {
            if (itemclick_check)
                map_buttons.navigation_savemodule(this);
            else show_toast_error();
        });
        button_bottomsheet_share.setOnClickListener(view -> {
            if (itemclick_check)
                map_buttons.share_address_savemodule(this);
            else show_toast_error();
        });
        button_addnew.setOnClickListener(view -> {
            AdManager.showInterstitialAd(this, "SAVEADDRESS_ADDNEW_STATUS", new Runnable() {
                @Override
                public void run() {
                    newaddress_dialog.new_address_dialog(SaveAddressActivity.this);
                }
            });
        });
    }

    public void get_addressline() {
        try {
            LatLng latLng = mapdata_forall.getlocation(this);
            List<Address> addressListt;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            addressListt = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            addressline = addressListt.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show_toast_error() {
        Toast.makeText(this, "Please choose an address first", Toast.LENGTH_SHORT).show();
    }

    public static void toast_error_itemdelete(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void setadapter(SaveAddressActivity saveaddress_module) {
        if (!list_title.isEmpty() && !list_detail.isEmpty()) {
            adapter_saveaddress adapter_saveaddress = new adapter_saveaddress(saveaddress_module,
                    list_title, list_detail, list_latitude, list_longitude, list_ids);
            recyclerView.setAdapter(adapter_saveaddress);
        }
    }

    @Override
    public void onBackPressed() {
        AdManager.showInterstitialAd(this, "SAVEADDRESS_BACKPRESS_STATUS", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SaveAddressActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}