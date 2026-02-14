package com.example.liveearhmap2026;

import static com.example.liveearhmap2026.mapdata_forall.addressline;
import static com.example.liveearhmap2026.mapdata_forall.getlatlong_navend;
import static com.example.liveearhmap2026.mapdata_forall.getlatlong_navstart;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

public class map_buttons extends Activity {
    public static boolean default_status = false, sattelite_status = false, night_status = false, traffic_status = false, three_d_status = false;
    public static final String SHAREURL = "https://www.google.com/maps/search/?api=1&query=";
    public static final String NAVIGATE_URL_FIRAST = "https://www.google.com/maps/dir/?api=1&origin=";
    public static final String NAVIGATE_URL_SECOND = "&travelmode=";
    public static String DRIVING_TYPE = "";
    public static String LATITUDE_SAVEADDRESS = "";
    public static String LONGITUDE_SAVEADDRESS = "";
    public static String ADDRESSLINE_SAVEADDRESS = "";
    public static String APPurl = "https://play.google.com/store/apps/details?id=com.liveearthmap.livestreetview.explore.worldmap3D.realtime";
    public static final String HORIZONTAL_LINE = "------------------------------\n";
    public static int SPEECH_REQUEST_CODE = 111;

    public static void setSattelite_status(GoogleMap map, Activity activity) {
        if (sattelite_status) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            sattelite_status = true;
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    public static void setDefault_status(GoogleMap mMap, Activity activity) {
        if (default_status) {
            mMap.setBuildingsEnabled(false);
            mMap.setTrafficEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(null);
        } else {
            mMap.setBuildingsEnabled(false);
            default_status = true;
            mMap.setBuildingsEnabled(false);
            mMap.setTrafficEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(null);
        }
    }

    public static void setNight_status(GoogleMap mMap, Activity activity) {
        if (night_status) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_in_night));
        } else {
            night_status = true;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_in_night));
        }
    }

    public static void setThree_d_status(GoogleMap mMap, Activity activity) {
        if (three_d_status) {
            mMap.setBuildingsEnabled(true);
        } else {
            three_d_status = true;
            mMap.setBuildingsEnabled(true);
        }
    }

    public static void setTraffic_status(GoogleMap mMap, Activity activity) {
        if (traffic_status) {
            mMap.setTrafficEnabled(true);
        } else {
            traffic_status = true;
            mMap.setTrafficEnabled(true);
        }
    }

    public static void share_address(Activity activity) {
        try {
            if (mapdata_forall.getlatlong != null) {
                String addrs = SHAREURL + mapdata_forall.getlatlong.latitude + "," + mapdata_forall.getlatlong.longitude;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Address:\n" + addressline + "\n" + HORIZONTAL_LINE +
                                "Click to navigate:\n" + addrs + "\n" + HORIZONTAL_LINE + "Let me recommend you this application\n" +
                                APPurl);
                sendIntent.setType("text/plain");
                activity.startActivity(Intent.createChooser(sendIntent, "Choose one"));
            }
        }catch (Exception e){}
    }

    public static void share_address_savemodule(Activity activity) {
        try {
            String addrs = SHAREURL + LATITUDE_SAVEADDRESS + "," +
                    LONGITUDE_SAVEADDRESS;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Address:\n" + ADDRESSLINE_SAVEADDRESS + "\n" + HORIZONTAL_LINE +
                            "Click to navigate:\n" + addrs + "\n" + HORIZONTAL_LINE + "Let me recommend you this application\n" +
                            APPurl);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, "Choose one"));
        }catch (Exception e){}
    }

    public static void voiceInput(Activity activity) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            activity.startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void navigation(Activity activity) {
        try {
            if (getlatlong_navend != null) {
                String uri = NAVIGATE_URL_FIRAST + getlatlong_navstart.latitude + "," + getlatlong_navstart.longitude +
                        "&destination=" + getlatlong_navend.latitude + "," + getlatlong_navend.longitude + map_buttons.NAVIGATE_URL_SECOND + "driving";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(intent);
            } else {
                String lat = String.valueOf(mapdata_forall.getlatlong.latitude);
                String longi = String.valueOf(mapdata_forall.getlatlong.longitude);
                String uri = NAVIGATE_URL_FIRAST + lat + "," + longi + NAVIGATE_URL_SECOND + DRIVING_TYPE;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(activity, "Cannot navigate", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void navigation_savemodule(Activity activity) {
        try {
            String uri = NAVIGATE_URL_FIRAST + LATITUDE_SAVEADDRESS + "," + LONGITUDE_SAVEADDRESS + NAVIGATE_URL_SECOND + DRIVING_TYPE;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            activity.startActivity(intent);
        }catch (Exception e){}
    }

    public static void navigation_nearby(Activity activity, String text) {
        try {
            String uri = SHAREURL + text;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            activity.startActivity(intent);
        }catch (Exception e){}
    }

    public static void shareaddress_pin(Activity activity) {
        try {
            if (mapdata_forall.getlatlong != null) {
                String addrs = "Click to navigate:\n" + SHAREURL + mapdata_forall.getlatlong.latitude + "," +
                        mapdata_forall.getlatlong.longitude;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, addrs);
                sendIntent.setType("text/plain");
                activity.startActivity(Intent.createChooser(sendIntent, "Choose one"));
            }
            else Toast.makeText(activity, "Address Not Found", Toast.LENGTH_SHORT).show();
        }catch (Exception e){}
    }

    public static void shareaddress_pin_savemodule(Activity activity) {
        try {
            if (LATITUDE_SAVEADDRESS != null) {
                String addrs = "Click to navigate:\n" + SHAREURL + LATITUDE_SAVEADDRESS + "," +
                        LONGITUDE_SAVEADDRESS;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, addrs);
                sendIntent.setType("text/plain");
                activity.startActivity(Intent.createChooser(sendIntent, "Choose one"));
            }
        }catch (Exception e){}
    }

    public static void copy_address(Activity activity) {
        try {
            if (mapdata_forall.get_address != null) {
                String addrs = mapdata_forall.get_address.getAddressLine(0);
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Address", addrs);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, "Address copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }

    public static void copy_address_savemodule(Activity activity) {
        try {
            if (ADDRESSLINE_SAVEADDRESS != null) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Address", ADDRESSLINE_SAVEADDRESS);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, "Address copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }

}
