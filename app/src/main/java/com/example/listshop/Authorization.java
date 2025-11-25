package com.example.listshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Authorization extends AppCompatActivity {
    private EditText editUsername, editPassword;
    private ImageView imageYandex;
    private Button registration, enter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.autorization);

        editUsername = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        imageYandex = findViewById(R.id.imageYandex);
        enter = findViewById(R.id.button_Enter);
        registration = findViewById(R.id.buttonRegistration);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        imageYandex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite();
            }
        });
    }

    private void openWebsite(){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eda.yandex.by/"));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при открытии сайта", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
