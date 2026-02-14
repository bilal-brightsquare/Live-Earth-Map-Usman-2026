package com.example.liveearhmap2026;

import static com.example.liveearhmap2026.mapdata_forall.addressline;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.modules.SaveAddressActivity;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class newaddress_dialog {
    public static String id;
    public static Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void new_address_dialog(Activity activity) {
        SQLite_Class.create_db(activity);
        SQLite_Class.create_table(activity);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.now();
        id = dtf.format(dateTime);
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.layout_dialog_newaddress);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        AppCompatButton button_save = dialog.findViewById(R.id.button_newaddress_save);
        AppCompatImageButton button_back = dialog.findViewById(R.id.button_newaddress_backpress);
        AppCompatImageButton button_premium = dialog.findViewById(R.id.button_newaddress_premium);
        AppCompatEditText label = dialog.findViewById(R.id.edittext_addnewaddress_label);
        AppCompatEditText address = dialog.findViewById(R.id.edittext_addnewaddress_address);
        CheckBox checkBox_current_loc = dialog.findViewById(R.id.chekbox_addnewaddress_current_location);
        button_back.setOnClickListener(view -> {
            cancellme();
        });
        checkBox_current_loc.setOnCheckedChangeListener((compoundButton, b) -> {
            if (checkBox_current_loc.isChecked()) {
                if (addressline != null) {
                    address.setText(addressline);
                    address.setEnabled(false);
                }
            } else {
                address.setText(null);
                address.setEnabled(true);
                address.setHint("New York, USA");
            }
        });
        button_premium.setOnClickListener(view -> {
            activity.startActivity(new Intent(activity, InAppPurchaseActivity.class));
        });
        button_save.setOnClickListener(view -> {
            if (Objects.requireNonNull(label.getText()).toString().isEmpty()) {
                label.setError("Please enter label");
            } else if (Objects.requireNonNull(address.getText()).toString().isEmpty()) {
                address.setError("Please enter address");
            } else {
                List<Address> addressListt;
                try {
                    Geocoder geocoder1 = new Geocoder(activity);
                    addressListt = geocoder1.getFromLocationName(address.getText().toString(), 1);
                    String addressline = addressListt.get(0).getAddressLine(0);
                    String lat = String.valueOf(addressListt.get(0).getLatitude());
                    String longi = String.valueOf(addressListt.get(0).getLongitude());
                    if (!label.getText().toString().isEmpty() && !addressline.isEmpty()) {

                        SQLite_Class.insert_data(label.getText().toString(), addressline, lat, longi, id);
                        Toast.makeText(activity, "Save successful", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        SQLite_Class.getall_addresses(activity);
                        SaveAddressActivity obj = new SaveAddressActivity();
                        SaveAddressActivity.setadapter(obj);
                    } else {
                        Toast.makeText(activity, "Please select valid address and label", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignore) {
                }
            }
        });
        dialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
            }
            return true;
        });
        dialog.show();
        if (AdManager.IS_PREMIUM){
            button_premium.setVisibility(View.GONE);
        }
    }
    public static void cancellme(){
        dialog.dismiss();
    }
}
