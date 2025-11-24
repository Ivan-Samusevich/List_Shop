package com.example.listshop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class Registration extends AppCompatActivity {

    EditText userName, password, checkPassword;

    Button registration, back;

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
                    Snackbar.make(view, "Регистрация прошла успешна!", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Registration.this, Authorization.class);
                            startActivity(intent);
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
                finish();
            }
        });
    }

}
