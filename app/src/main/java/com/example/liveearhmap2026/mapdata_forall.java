package com.example.liveearhmap2026;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class mapdata_forall {
    public static String addressline;
    public static String citycountry;
    public static String marker_addres;
    public static final int REQUEST_LOCATION = 10;
    public static LocationManager locationManager;
    public static LatLng getlatlong;
    public static LatLng getlatlong_navstart;
    public static LatLng getlatlong_navend;
    public static Address get_address;

    public static LatLng getlocation(Activity activity) {
        try {
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                OnGPS(activity);
            } else {
                if (ActivityCompat.checkSelfPermission(
                        activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, mapdata_forall.REQUEST_LOCATION);
                } else {
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (lastKnownLocation != null) {
                        getlatlong = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        getlatlong_navstart = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        List<Address> addressListt;
                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        try {
                            addressListt = geocoder.getFromLocation(getlatlong.latitude, getlatlong.longitude, 1);
                            addressline = addressListt.get(0).getAddressLine(0);
                            citycountry = addressListt.get(0).getSubLocality() + ", " + addressListt.get(0).getSubAdminArea();
                            marker_addres = addressListt.get(0).getFeatureName() + " " + addressListt.get(0).getSubLocality() + ", " + addressListt.get(0).getSubAdminArea();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (Exception e){}
        return getlatlong;
    }

    public static void Marker_and_Camera(GoogleMap googleMap, LatLng latLng, Activity activity, View customMarkerView) {
        try {
            if (googleMap == null) {
                return;
            }
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.clear();
            googleMap.setPadding(0, 120, 0, 0);

            try {
                Objects.requireNonNull(googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapFromVector(activity.getApplicationContext(), R.drawable.marker_icon))))
                ;
            }catch (Exception e){}
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latLng.latitude, latLng.longitude), 18));
        }catch (Exception e){}
    }

    private static BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void OnGPS(Activity activity) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Your GPS seems to be disabled, you may not be able to use all features.\nDo you want to enable it?")
                    .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    activity.finish();
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
        }
    }

    public static void set_geocoder_with_latlong(Activity activity, LatLng latLng, TextView textView) {
        List<Address> addressListt;
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addressListt = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            get_address = addressListt.get(0);
            textView.setText(get_address.getSubLocality() + ", " + get_address.getSubAdminArea());
            addressline = get_address.getAddressLine(0);
        } catch (Exception e) {
        }
    }

    public static void set_geocoder_with_location_name(Activity activity, String text) {
        List<Address> addressListt;
        try {
            Geocoder geocoder1 = new Geocoder(activity);
            addressListt = geocoder1.getFromLocationName(text, 1);
            get_address = addressListt.get(0);
            addressline = get_address.getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void input_textchange_checker(AppCompatEditText input, Activity activity, CardView layoutCompat_suggestion, TextView header, TextView sub, ProgressBar progressBar, GoogleMap googleMap, View customMarkerView) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                progressBar.setVisibility(View.VISIBLE);
                layoutCompat_suggestion.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    try {
                        List<Address> addressListt;
                        String loc1 = Objects.requireNonNull(input.getText()).toString();
                        Geocoder geocoder1 = new Geocoder(activity);
                        addressListt = geocoder1.getFromLocationName(loc1, 1);
                        get_address = addressListt.get(0);
                        header.setText(get_address.getFeatureName());
                        sub.setText(get_address.getAddressLine(0));
                        addressline = get_address.getAddressLine(0);
                        marker_addres = get_address.getFeatureName() + " " + get_address.getSubLocality()
                                + ", " + get_address.getSubAdminArea();
                        try {
                            progressBar.setVisibility(View.GONE);
                        }catch (Exception e){}
                    } catch (Exception e) {
                        header.setText("No Address Found");
                        sub.setText(null);
                        try {
                            progressBar.setVisibility(View.GONE);
                        }catch (Exception e1){}
                    }
                    layoutCompat_suggestion.setOnClickListener(view -> {
                        hide_keyboard(activity);
                        layoutCompat_suggestion.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        try {
                            LatLng latLng = new LatLng(get_address.getLatitude(), get_address.getLongitude());
                            Marker_and_Camera(googleMap, latLng, activity, customMarkerView);
                            getlatlong = latLng;
//                            if (activity.getClass()== gps_navigation_module.class)
//                                getlatlong_navend = latLng;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }, 3000);
            }
        });
    }

    public static void hide_keyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
