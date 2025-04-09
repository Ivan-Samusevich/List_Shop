package com.example.listshop;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DataManager dataManager;
    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private ShoppingListAdapter adapter;
    private ImageButton deleteListButton;
    private int selectedListPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = new DataManager(this);
        shoppingLists = dataManager.loadLists();

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
        addButton.setOnClickListener(v -> showAddListDialog());

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
        builder.show();
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
    @Override
    protected void onResume() {
        super.onResume();
        shoppingLists = dataManager.loadLists();
        adapter.updateLists(shoppingLists);
        hideDeleteButton();
    }
}