package com.example.listshop;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DataManager dataManager;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private ShoppingListAdapter adapter;
    private ImageButton deleteListButton;
    private int selectedListPosition = -1;

    private String themes;

    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationHelper = new NotificationHelper(this);
        sendAppLaunchNotification();

        dataManager = new DataManager(this);
        shoppingLists = dataManager.loadLists();
        themes = dataManager.loadThemes();
        System.out.println(themes);
        applyTheme(themes);

        RecyclerView recyclerView = findViewById(R.id.listsRecyclerView);
        adapter = new ShoppingListAdapter(shoppingLists, new ShoppingListAdapter.OnListClickListener() {
            @Override
            public void onListClick(int position) {
                openList(position);
            }

            @Override
            public void onListLongClick(int position) {
                showDeleteButton(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        deleteListButton = findViewById(R.id.deleteListButton);
        deleteListButton.setOnClickListener(v -> showDeleteDialog());


        Button addButton = findViewById(R.id.addNewListButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shoppingLists.size() >= 5 && dataManager.loadSub().equals("no")){

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                    builder.setTitle("Системное оповещение");
                    builder.setMessage("Ваш лимит количества списков - 5. Для снятия ограничения, купите подписку");
                    builder.setPositiveButton("Ok", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
                }
                else{
                    showAddListDialog();
                }
            }
        });
        setupBottomNavigation();
    }
    private void openList(int position) {
        Intent intent = new Intent(MainActivity.this, EditListActivity.class);
        intent.putExtra("list_position", position);
        startActivity(intent);
    }
    private void showDeleteButton(int position) {
        selectedListPosition = position;
        deleteListButton.setVisibility(View.VISIBLE);
    }
    private void showDeleteDialog() {
        if (selectedListPosition == -1) return;

        new AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
                .setTitle("Удаление списка")
                .setMessage("Вы уверены, что хотите удалить этот список? Все товары будут потеряны.")
                .setPositiveButton("Удалить", (dialog, which) -> deleteSelectedList())
                .setNegativeButton("Отмена", (dialog, which) -> hideDeleteButton())
                .setOnDismissListener(dialog -> hideDeleteButton())
                .show();
    }

    private void deleteSelectedList() {
        if (selectedListPosition >= 0 && selectedListPosition < shoppingLists.size()) {
            shoppingLists.remove(selectedListPosition);
            dataManager.saveLists(shoppingLists);
            adapter.notifyItemRemoved(selectedListPosition);
            Toast.makeText(this, "Список удален", Toast.LENGTH_SHORT).show();
        }
        hideDeleteButton();
    }
    private void hideDeleteButton() {
        deleteListButton.setVisibility(View.GONE);
        selectedListPosition = -1;
    }
    private void showAddListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
        builder.setTitle("Новый список покупок");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Введите название списка");
        builder.setView(input);

        builder.setPositiveButton("Создать", (dialog, which) -> {
            String listName = input.getText().toString().trim();
            if (!listName.isEmpty()) {
                ShoppingList newList = new ShoppingList(listName, new ArrayList<>());
                shoppingLists.add(newList);
                dataManager.saveLists(shoppingLists);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Отмена", null);

        Dialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            input.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_lists) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
        bottomNavigation.setSelectedItemId(R.id.nav_lists);
    }
    private void applyTheme(String selectedTheme) {
        switch (selectedTheme) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "night":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void sendAppLaunchNotification() {
        String title = "Приложение открыто";
        String message = "Добро пожаловать в наше приложение!";
        notificationHelper.sendNotification(title, message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingLists = dataManager.loadLists();
        adapter.updateLists(shoppingLists);
        hideDeleteButton();
    }
}