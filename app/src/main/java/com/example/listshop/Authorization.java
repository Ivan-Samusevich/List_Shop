package com.example.listshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;

import java.sql.Connection;
import java.sql.SQLException;

import models.User;

public class Authorization extends AppCompatActivity {
    private EditText editUsername, editPassword;
    private Button registration, enter;

    private BannerAdView mAdView;

    DataManager dataManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.autorization);

        editUsername = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        enter = findViewById(R.id.button_Enter);
        registration = findViewById(R.id.buttonRegistration);


        mAdView = findViewById(R.id.banner);
        initializeYandexAds();
        showYandexAd();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //User user = dataManager.loadUser();
                if(editUsername.getText().toString().isEmpty()){
                    editUsername.setError("Поле должно быть заполнено!");
                }
                if(editPassword.getText().toString().length() < 8){
                    editPassword.setError("Длина пароля не менее 8 символов!");
                }
                if(editUsername.getText().toString().equals("1") && editPassword.getText().toString().equals("1")){
                    Intent intent = new Intent(Authorization.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else{
                    editPassword.setText("");
                    editUsername.setText("");
                    Toast toast = Toast.makeText(Authorization.this, "Нет пользователя с такими данными!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0); // Позиция по центру
                    toast.show();
                }
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Authorization.this, Registration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
