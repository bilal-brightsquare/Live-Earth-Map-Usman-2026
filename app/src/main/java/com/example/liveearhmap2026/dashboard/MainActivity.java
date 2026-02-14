package com.example.liveearhmap2026.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liveearhmap2026.ExitActivity;
import com.example.liveearhmap2026.InAppPurchaseActivity;
import com.example.liveearhmap2026.PremiumActivity;
import com.example.liveearhmap2026.ShowRatingAndroidDialog;
import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.modules.GpsNavigationActivity;
import com.example.liveearhmap2026.modules.LiveEarthMapActivity;
import com.example.liveearhmap2026.modules.NearbyPlacesActivity;
import com.example.liveearhmap2026.modules.SaveAddressActivity;
import com.example.liveearhmap2026.modules.ShareAddressActivity;
import com.google.android.material.button.MaterialButton;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.BuildConfig;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawer;
    ImageView button_premium;
    AppCompatImageButton button_menu, button_close;
    TextView button_shareapp, button_rateus, button_privacy, button_removeads;
    Dialog dialog;
    ConstraintLayout layout_contentmain;
    private RecyclerView dashboardRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing all attributes
        dialog = new Dialog(this);
        button_menu = findViewById(R.id.dashboard_menu_button);
        button_premium = findViewById(R.id.dashbord_premium_button);
        button_close = findViewById(R.id.button_menu_close);
        button_shareapp = findViewById(R.id.button_menu_shareapp);
        button_rateus = findViewById(R.id.button_menu_rateus);
        button_privacy = findViewById(R.id.button_menu_privacy);
        button_removeads = findViewById(R.id.button_menu_removeads);
        button_menu.setOnClickListener(this);
        button_premium.setOnClickListener(this);
        button_close.setOnClickListener(this);
        button_shareapp.setOnClickListener(this);
        button_rateus.setOnClickListener(this);
        button_privacy.setOnClickListener(this);
        button_removeads.setOnClickListener(this);
        drawer = findViewById(R.id.drawer_layout);
        layout_contentmain = findViewById(R.id.layout_content_main);

        check_permission_granted();

        ShowRatingAndroidDialog.INSTANCE.showRateDialog(this, new Runnable() {
            @Override
            public void run() {
                ratingComnpleted = true;
            }
        });

        dashboardRecycler = findViewById(R.id.dashboardRecycler);

        dashboardRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        dashboardRecycler.setHasFixedSize(true);

        List<DashboardItem> dashboardItems = new ArrayList<>();

        dashboardItems.add(new DashboardItem(R.drawable.live_earth_map, "Live Earth Map"));
        dashboardItems.add(new DashboardItem(R.drawable.nearby_places, "Nearby Places"));
        dashboardItems.add(new DashboardItem(R.drawable.gps_navigation, "GPS Navigation"));
        dashboardItems.add(new DashboardItem(R.drawable.share_location, "Share Location"));
        dashboardItems.add(new DashboardItem(R.drawable.saved_places, "Saved Places"));
        dashboardItems.add(new DashboardItem(R.drawable.weather_update, "Share App"));

        DashboardAdapter adapter = new DashboardAdapter(this, dashboardItems, position -> {
            switch (position) {
                case 0:
                    launchFrontScreen(new Intent(this, LiveEarthMapActivity.class));
                    break;
                case 1:
                    launchFrontScreen(new Intent(this, NearbyPlacesActivity.class));
                    break;
                case 2:
                    launchFrontScreen(new Intent(this, GpsNavigationActivity.class));
                    break;
                case 3:
                    launchFrontScreen(new Intent(this, ShareAddressActivity.class));
                    break;
                case 4:
                    launchFrontScreen(new Intent(this, SaveAddressActivity.class));
                    break;
                case 5:
                    shareApp();
                    break;
            }
        });

        dashboardRecycler.setAdapter(adapter);
    }

    void launchFrontScreen(Intent intent){
        AdManager.showInterstitialAd(this, "FRONTSCREEN_INTER_PLACEMENT", new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        });
    }

    boolean ratingComnpleted = false;

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.dashboard_menu_button) {
            drawer.openDrawer(GravityCompat.START);
        } else if (viewId == R.id.button_menu_shareapp) {
            drawer.closeDrawer(GravityCompat.START);
            shareApp();
        } else if (viewId == R.id.button_menu_rateus) {
            drawer.closeDrawer(GravityCompat.START);
            dialog.setContentView(R.layout.rateus_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setCancelable(false);
            dialog.show();
            AppCompatButton button_submit = dialog.findViewById(R.id.btn_submit_rateus);
            AppCompatButton button_skip = dialog.findViewById(R.id.btn_skip_rateus);
            AppCompatImageButton button_closedialog = dialog.findViewById(R.id.btn_closedialoge);
            RatingBar ratingBar = dialog.findViewById(R.id.ratingBar_rateus);
            button_skip.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            button_closedialog.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            button_submit.setOnClickListener(view1 -> {
                Toast.makeText(this, "Thank you for Rating", Toast.LENGTH_SHORT).show();
                if (ratingBar.getRating() > 2) {
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
            });
        } else if (viewId == R.id.button_menu_privacy) {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent11 = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_link)));
            startActivity(intent11);
        }
        else if (viewId == R.id.button_menu_removeads) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, InAppPurchaseActivity.class));
        }
        else if (viewId == R.id.dashbord_premium_button) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this,InAppPurchaseActivity.class));
        }
        else if (viewId == R.id.button_menu_close) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    private void shareApp() {
        final String appPackageName = getPackageName();
        String shareUrl = "https://play.google.com/store/apps/details?id=" + appPackageName;

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getResources().getString(R.string.app_name), shareUrl);
        clipboard.setPrimaryClip(clip);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
        startActivity(Intent.createChooser(sendIntent, "Share with"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AdManager.showInterstitialAd(this, "EXIT_INTER_PLACEMENT", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, ExitActivity.class));
            }
        });
    }

    public void check_permission_granted() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                   1);
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                String message = "You need to grant permission to continue";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
