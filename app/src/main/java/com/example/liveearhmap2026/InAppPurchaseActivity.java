package com.example.liveearhmap2026;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.ads.Adutills;
import com.example.liveearhmap2026.dashboard.MainActivity;
import com.example.liveearhmap2026.language.langActivity;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

public class InAppPurchaseActivity extends AppCompatActivity {
//
    private BillingClassByBilalNew billing;

    private TextView btnClose;
    private TextView priceText;
    private CardView startTrialBtn;
    Intent myintent = null;

    boolean fromSplash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_in_app_purchase);

        String Type = getIntent().getStringExtra("ActivityType");
        Log.d("TYPEE", "onCreate: " + Type);

        if (Type != null) {
            if (Type.equals("SplashAd")) {
                fromSplash = true;
                myintent = new Intent(this, langActivity.class);
            }  else {
                myintent = null;
            }
        }

        initViews();
        setupBilling();
        setupClicks();
    }

    private void initViews() {
        btnClose = findViewById(R.id.btn_close);
        priceText = findViewById(R.id.price);
        startTrialBtn = findViewById(R.id.btn_start);
    }

    private void setupBilling() {
        billing = new BillingClassByBilalNew(this);

        billing.initializeBilling(isPremium -> {
            if (isPremium || BillingClassByBilalNew.Companion.isPurchased()) {
                finish(); // already premium
                return null;
            }

            // Fetch Weekly Price
            billing.fetchWeeklyPrice(price -> {
                priceText.setText(price + " / week after FREE trial");
                return null;
            });

            return null;
        });
    }

    private void setupClicks() {
        btnClose.setOnClickListener(v -> {
            if (myintent != null) {
                forward();
            } else {
                finish();
            }
        });

        startTrialBtn.setOnClickListener(v -> {
            billing.purchasePremium(success -> {
                if (success) {
                    Toast.makeText(InAppPurchaseActivity.this, "Premium Activated 🎉", Toast.LENGTH_SHORT).show();
                    BillingClassByBilalNew.Companion.setPurchased(success);
                    AdManager.IS_PREMIUM = success;
                    Intent intent = new Intent(InAppPurchaseActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                            InAppPurchaseActivity.this,
                            "Purchase Failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                return null;
            });

        });

    }

    void forward(){
        if (!fromSplash ) {
            AdManager.showInterstitialAd(this, "PREMIUM_INTER", new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
        else {
            startActivity(myintent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        forward();
    }
}
