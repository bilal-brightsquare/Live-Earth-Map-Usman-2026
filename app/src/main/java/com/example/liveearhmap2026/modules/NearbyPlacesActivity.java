package com.example.liveearhmap2026.modules;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.example.liveearhmap2026.InAppPurchaseActivity;
import com.example.liveearhmap2026.PremiumActivity;
import com.example.liveearhmap2026.ads.AdManager;
import com.example.liveearhmap2026.dashboard.MainActivity;
import com.example.liveearhmap2026.map_buttons;
import com.example.liveearhmap2026.mapdata_forall;
import com.liveearthmaphd.sharelocation.gpsnavigation.routeplanner.livesatellite.R;

import java.util.List;
import java.util.Objects;

public class NearbyPlacesActivity extends AppCompatActivity implements View.OnClickListener {
    AppCompatEditText input;
    ProgressBar progressBar;
    TextView textView_suggest;
    CardView layoutCompat_suggest;
    AppCompatImageButton button_premium, button_backpress, button_voiceinput;
    AppCompatImageView bakery, cloth, food, grocery, atm, bank, college, dental, filling, gym, hospital, hotel, saloon, school, shop, pharma,
            cafe, cinema, park, zoo, airport, bus, parking, taxi, church, library, mosque, police;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearbyplaces_module);

        input = findViewById(R.id.edittext_nearbyplaces);
        progressBar = findViewById(R.id.progressbar_suggestlayout_nearbyplaces);
        textView_suggest = findViewById(R.id.suggestheader_nearbyplaces);
        layoutCompat_suggest = findViewById(R.id.layout_suggest_address_nearbyplaces);
        button_backpress = findViewById(R.id.button_nearbyplaces_backpress);
        button_premium = findViewById(R.id.button_nearbyplaces_premium);
        button_voiceinput = findViewById(R.id.button_nearbyplaces_voiceinput);
        initialize_all_nearby();
        if (AdManager.IS_PREMIUM) {
            button_premium.setVisibility(View.GONE);
        }
        button_premium.setOnClickListener(view -> {
            startActivity(new Intent(this, InAppPurchaseActivity.class));
        });
        button_backpress.setOnClickListener(view -> {
            onBackPressed();
        });
        button_voiceinput.setOnClickListener(view -> {
            map_buttons.voiceInput(this);
        });

        input_textwatcher();
//        FrameLayout frameLayout = findViewById(R.id.framelayout_nearby_bannerad);
//        AdManager.showBannerAd(this,frameLayout,"NEARBY_BANNER_PLACEMENT");
    }

    public void initialize_all_nearby() {
        bakery = findViewById(R.id.nearbyplace_bakery);
        cloth = findViewById(R.id.nearbyplace_cloth);
        food = findViewById(R.id.nearbyplace_foood);
        grocery = findViewById(R.id.nearbyplace_grocery);
        atm = findViewById(R.id.nearbyplace_atm);
        bank = findViewById(R.id.nearbyplace_bank);
        college = findViewById(R.id.nearbyplace_college);
        dental = findViewById(R.id.nearbyplace_dental);
        filling = findViewById(R.id.nearbyplace_filling);
        gym = findViewById(R.id.nearbyplace_gym);
        hospital = findViewById(R.id.nearbyplace_hospital);
        hotel = findViewById(R.id.nearbyplace_hotel);
        saloon = findViewById(R.id.nearbyplace_saloon);
        school = findViewById(R.id.nearbyplace_school);
        shop = findViewById(R.id.nearbyplace_shop);
        pharma = findViewById(R.id.nearbyplace_pharma);
        cafe = findViewById(R.id.nearbyplace_cafe);
        cinema = findViewById(R.id.nearbyplace_cinema);
        park = findViewById(R.id.nearbyplace_park);
        zoo = findViewById(R.id.nearbyplace_zoo);
        airport = findViewById(R.id.nearbyplace_airport);
        bus = findViewById(R.id.nearbyplace_bus);
        parking = findViewById(R.id.nearbyplace_parking);
        taxi = findViewById(R.id.nearbyplace_taxi);
        church = findViewById(R.id.nearbyplace_church);
        library = findViewById(R.id.nearbyplace_library);
        mosque = findViewById(R.id.nearbyplace_mosque);
        police = findViewById(R.id.nearbyplace_police);


        bakery.setOnClickListener(this);
        cloth.setOnClickListener(this);
        food.setOnClickListener(this);
        grocery.setOnClickListener(this);
        atm.setOnClickListener(this);
        bank.setOnClickListener(this);
        college.setOnClickListener(this);
        dental.setOnClickListener(this);
        filling.setOnClickListener(this);
        gym.setOnClickListener(this);
        hospital.setOnClickListener(this);
        hotel.setOnClickListener(this);
        saloon.setOnClickListener(this);
        school.setOnClickListener(this);
        shop.setOnClickListener(this);
        pharma.setOnClickListener(this);
        cafe.setOnClickListener(this);
        cinema.setOnClickListener(this);
        park.setOnClickListener(this);
        zoo.setOnClickListener(this);
        airport.setOnClickListener(this);
        bus.setOnClickListener(this);
        parking.setOnClickListener(this);
        taxi.setOnClickListener(this);
        church.setOnClickListener(this);
        library.setOnClickListener(this);
        mosque.setOnClickListener(this);
        police.setOnClickListener(this);
    }

    public void input_textwatcher() {
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
                layoutCompat_suggest.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    try {
                        textView_suggest.setText(Objects.requireNonNull(input.getText()).toString());
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                    layoutCompat_suggest.setOnClickListener(view -> {
                        mapdata_forall.hide_keyboard(NearbyPlacesActivity.this);
                        layoutCompat_suggest.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        map_buttons.navigation_nearby(NearbyPlacesActivity.this,
                                Objects.requireNonNull(input.getText()).toString());
                    });

                }, 5000);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == map_buttons.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            input.setText(spokenText);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.nearbyplace_bakery) {
            map_buttons.navigation_nearby(this, "bakery");

        } else if (id == R.id.nearbyplace_cloth) {
            map_buttons.navigation_nearby(this, "cloth");

        } else if (id == R.id.nearbyplace_foood) {
            map_buttons.navigation_nearby(this, "food");

        } else if (id == R.id.nearbyplace_atm) {
            map_buttons.navigation_nearby(this, "atm");

        } else if (id == R.id.nearbyplace_bank) {
            map_buttons.navigation_nearby(this, "bank");

        } else if (id == R.id.nearbyplace_college) {
            map_buttons.navigation_nearby(this, "college");

        } else if (id == R.id.nearbyplace_dental) {
            map_buttons.navigation_nearby(this, "dental");

        } else if (id == R.id.nearbyplace_filling) {
            map_buttons.navigation_nearby(this, "filling");

        } else if (id == R.id.nearbyplace_gym) {
            map_buttons.navigation_nearby(this, "gym");

        } else if (id == R.id.nearbyplace_hospital) {
            map_buttons.navigation_nearby(this, "hospital");

        } else if (id == R.id.nearbyplace_hotel) {
            map_buttons.navigation_nearby(this, "hotel");

        } else if (id == R.id.nearbyplace_saloon) {
            map_buttons.navigation_nearby(this, "saloon");

        } else if (id == R.id.nearbyplace_school) {
            map_buttons.navigation_nearby(this, "school");

        } else if (id == R.id.nearbyplace_shop) {
            map_buttons.navigation_nearby(this, "shop");

        } else if (id == R.id.nearbyplace_pharma) {
            map_buttons.navigation_nearby(this, "pharma");

        } else if (id == R.id.nearbyplace_cafe) {
            map_buttons.navigation_nearby(this, "cafe");

        } else if (id == R.id.nearbyplace_cinema) {
            map_buttons.navigation_nearby(this, "cinema");

        } else if (id == R.id.nearbyplace_park) {
            map_buttons.navigation_nearby(this, "park");

        } else if (id == R.id.nearbyplace_zoo) {
            map_buttons.navigation_nearby(this, "zoo");

        } else if (id == R.id.nearbyplace_airport) {
            map_buttons.navigation_nearby(this, "airport");

        } else if (id == R.id.nearbyplace_bus) {
            map_buttons.navigation_nearby(this, "bus");

        } else if (id == R.id.nearbyplace_parking) {
            map_buttons.navigation_nearby(this, "parking");

        } else if (id == R.id.nearbyplace_taxi) {
            map_buttons.navigation_nearby(this, "taxi");

        } else if (id == R.id.nearbyplace_church) {
            map_buttons.navigation_nearby(this, "church");

        } else if (id == R.id.nearbyplace_library) {
            map_buttons.navigation_nearby(this, "library");

        } else if (id == R.id.nearbyplace_mosque) {
            map_buttons.navigation_nearby(this, "mosque");

        } else if (id == R.id.nearbyplace_police) {
            map_buttons.navigation_nearby(this, "police");
        }

    }

    @Override
    public void onBackPressed() {
        AdManager.showInterstitialAd(this, "NEARBY_BACKPRESS_STATUS", new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(NearbyPlacesActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}