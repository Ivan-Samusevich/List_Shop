package com.example.listshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class ProfileActivity extends AppCompatActivity {

    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataManager = new DataManager(this);

        String[] menuItems = {"Настройки", "Обратная связь","Политика конфиденциальности и условия использования", "О нас", "Оформить подписку", "Отменить подписку"};

        ListView menuList = findViewById(R.id.menuList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                menuItems
        );
        menuList.setAdapter(adapter);

        menuList.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    showThemeSelectionDialog();
                    break;
                case 1:
                    sendFeedback();
                    break;
                case 2:
                    showPrivacyPolicy();
                    break;
                case 3:
                    showAboutDialog();
                    break;
                case 4:
                    buy();
                    break;
                case 5:
                    refuse();
                    break;
            }
        });

        setupBottomNavigation();
    }

    private void showThemeSelectionDialog() {
        String[] themes = {"Светлая", "Темная"};
        int currentTheme = getCurrentTheme();
        new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
                .setTitle("Выбор темы")
                .setSingleChoiceItems(themes, currentTheme, (dialog, which) -> {
                    applyTheme(which);
                    dialog.dismiss();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
    private int getCurrentTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case android.content.res.Configuration.UI_MODE_NIGHT_NO:
                return 0;
            case android.content.res.Configuration.UI_MODE_NIGHT_YES:
                return 1;
        }
        return 0;
    }
    private void applyTheme(int selectedTheme) {
        switch (selectedTheme) {
            case 0:
                dataManager.saveThemes("light");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1:
                dataManager.saveThemes("night");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        recreate();
    }

//    private void openSettings() {
//        new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
//                .setTitle("Настройки")
//                .setMessage("Вам ничего не нужно настраивать")
//                .setPositiveButton("OK", null)
//                .show();
//    }


    private void sendFeedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        builder.setTitle("Оставить отзыв");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        final EditText input = new EditText(this);
        input.setHint("Ваш отзыв или предложение");
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Отправить", (dialog, which) -> {
            String feedback = input.getText().toString();
            if(!feedback.isEmpty()) {
                sendEmailWithIntent(feedback);
            } else {
                Toast.makeText(this, "Пожалуйста, введите ваш отзыв", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void sendEmailWithIntent(String feedback) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"samusevich.ivan@list.ru"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв о приложении ShopEasy");
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);

        try {
            startActivity(Intent.createChooser(emailIntent, "Отправить отзыв через:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "На устройстве не найдено почтовых приложений",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showPrivacyPolicy() {
        String privacyPolicyText = "Политика конфиденциальности и условия использования\n\n" +
                "1. Общие положения\n" +
                "1.1. Приложение ShopEasy (далее - Приложение) предназначено для создания и управления списками покупок.\n" +
                "1.2. Используя Приложение, вы соглашаетесь с настоящими условиями.\n\n" +
                "2. Сбор и использование данных\n" +
                "2.1. Приложение ShopEasy собирает только необходимые данные для работы функций приложения.\n " +
                "2.2. Мы не передаем ваши персональные данные третьим лицам.\n\n" +
                "3. Безопасность данных\n" +
                "3.1. Все данные хранятся локально на вашем устройстве и защищены современными методами шифрования.\n\n" +
                "4. Гарантии и ответственность\n" +
                "4.1. Приложение предоставляется \"как есть\".\n" +
                "4.2. Мы не несем ответственность за:\n" +
                "- Потерю данных при отсутствии резервных копий\n" +
                "- Использование Приложения не по назначению\n\n" +
                "5. Условия использования\n" +
                "5.1. Приложение предназначено для личного некоммерческого использования.\n " +
                "5.2. Запрещается использовать приложение для незаконных целей.\n\n" +
                "6. Авторские права\n" +
                "6.1. Все права на Приложение принадлежат разработчикам.\n" +
                "6.2. Вы можете свободно использовать Приложение для личных нужд.\n\n" +
                "7. Изменения в политике\n" +
                "7.1. Мы можем периодически обновлять нашу политику конфиденциальности.\n " +
                "7.2. Все изменения будут отражены в этом разделе.\n\n" +
                "Дата последнего обновления: 07.04.2025";

        new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
                .setTitle("Политика конфиденциальности")
                .setMessage(privacyPolicyText)
                .setPositiveButton("Принять", (dialog, which) -> {
                    Toast.makeText(this, "Спасибо за ознакомление с политикой", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Закрыть", null)
                .show();
    }



    private void showAboutDialog() {
        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0)
                    .versionName;

            new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
                    .setTitle("О приложении")
                    .setMessage(getString(R.string.app_name) + "\nВерсия: " + versionName +
                            "\n\n© 2025 ShopEasy Team\nВсе права защищены")
                    .setPositiveButton("OK", null)
                    .show();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
                    .setTitle("О приложении")
                    .setMessage(getString(R.string.app_name) +
                            "\n\n© 2025 ShopEasy Team\nВсе права защищены")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_lists) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_profile);
    }

    private void buy(){
        new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
                .setTitle("Покупка подписки")
                .setMessage("Спасибо за приобретение подписки")
                .setNegativeButton("Закрыть", null)
                .show();
        dataManager.saveSub("yes");
    }

    private void refuse(){
        new MaterialAlertDialogBuilder(this, R.style.MyAlertDialogTheme)
                .setTitle("Отказ от подписки")
                .setMessage("Подписка отменена")
                .setNegativeButton("Закрыть", null)
                .show();
        dataManager.saveSub("no");
    }

}