package com.example.listshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;

import models.User;

public class Registration extends AppCompatActivity {

    private BannerAdView mAdView;

    EditText userName, password, checkPassword;

    Button registration, back;

    DataManager dataManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration);

        userName = findViewById(R.id.editCreateUserName);
        password = findViewById(R.id.editCreatePassword);
        checkPassword = findViewById(R.id.editCheckPassword);
        registration = findViewById(R.id.Registration);
        back = findViewById(R.id.buttonReturn);
        mAdView = findViewById(R.id.banner);

        initializeYandexAds();
        showYandexAd();

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty()) {
                    userName.setError("Поле должно быть заполнено!");
                }
                else if (password.getText().toString().length() < 8) {
                    password.setError("Пароль должен быть не менее 8 символов!");
                }
                else if(!checkPassword.getText().toString().equals(password.getText().toString())){
                    checkPassword.setError("Пароли не совпадают!");
                }
                else{
                    User user = new User();
                    user.setUsername(userName.getText().toString());
                    user.setPassword(password.getText().toString());

                    Log.d("APP", "Перед вызовом saveUser");
                    //dataManager.saveUser(user);
                    Log.d("APP", "После вызова saveUser");
                    Snackbar.make(view, "Регистрация прошла успешна!", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(Registration.this, Authorization.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            finish();
                        }
                    }, 1000);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Authorization.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                finish();
            }
        });
    }

    private void showYandexAd() {
        if (mAdView != null) {
            mAdView.setAdSize(AdSize.BANNER_320x50);

            mAdView.setAdUnitId("R-M-17689147-1");

            final AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.setBannerAdEventListener(new BannerAdEventListener() {
                @Override
                public void onAdLoaded() {
                    Log.d("YandexAds", "Ad loaded successfully");
                    mAdView.setVisibility(android.view.View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                    Log.e("YandexAds", "Ad failed to load: " + adRequestError.getDescription());
                    mAdView.setVisibility(android.view.View.GONE);
                }


                @Override
                public void onAdClicked() {
                    Log.d("YandexAds", "Ad clicked");
                }

                @Override
                public void onLeftApplication() {
                    Log.d("YandexAds", "Left application");
                }

                @Override
                public void onReturnedToApplication() {
                    Log.d("YandexAds", "Returned to application");
                }

                @Override
                public void onImpression(@NonNull ImpressionData impressionData) {
                    Log.d("YandexAds", "Impression recorded");
                }
            });


            mAdView.loadAd(adRequest);
        }
    }

    private void initializeYandexAds() {
        MobileAds.initialize(this, () -> {
            Log.d("YandexAds", "SDK initialized successfully");
        });
    }

}
