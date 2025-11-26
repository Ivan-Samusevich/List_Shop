package com.example.listshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EditListActivity extends AppCompatActivity {
    private ShoppingList currentList;
    private int listPosition;
    private ItemAdapter adapter;
    private DataManager dataManager;
    private EditText itemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);


        listPosition = getIntent().getIntExtra("list_position", -1);
        if (listPosition == -1) finish();


        dataManager = new DataManager(this);
        ArrayList<ShoppingList> allLists = (ArrayList<ShoppingList>) dataManager.loadLists();
        currentList = allLists.get(listPosition);


        itemEditText = findViewById(R.id.itemEditText);
        Button addButton = findViewById(R.id.addItemButton);
        Button deleteButton = findViewById(R.id.deleteItemButton);
        RecyclerView recyclerView = findViewById(R.id.itemsRecyclerView);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView listTitleTextView = findViewById(R.id.listTitleTextView);


        adapter = new ItemAdapter(currentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        addButton.setOnClickListener(v -> {
            String newItem = ((EditText)findViewById(R.id.itemEditText)).getText().toString();
            if (!newItem.isEmpty()) {
                currentList.addItem(newItem);
                adapter.notifyItemInserted(currentList.getItemCount() - 1);
                saveData();
                showInfo("Элемент '" + itemEditText.getText().toString()+"' был добавлен в список");
                itemEditText.setText("");
            }
        });
        deleteButton.setOnClickListener(v -> deleteSelectedItems());


        backButton.setOnClickListener(v -> finish());
        listTitleTextView.setText(currentList.getTitle());


    }

    private void deleteSelectedItems() {
        boolean hasSelected = false;
        for (int i = 0; i < currentList.getItemCount(); i++) {
            if (currentList.isPurchased(i)) {
                hasSelected = true;
                break;
            }
        }

        if (!hasSelected) {
            showInfo("Нет выбранных товаров");
            return;
        }

        new AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
                .setTitle("Удаление")
                .setMessage("Удалить все выбранные товары?")
                .setPositiveButton("Да", (dialog, which) -> performDeletion())
                .setNegativeButton("Отмена", null)
                .show();
    }
    private void performDeletion() {
        int count = 0;
        for (int i = currentList.getItemCount() - 1; i >= 0; i--) {
            if (currentList.isPurchased(i)) {
                currentList.removeItem(i);
                adapter.notifyItemRemoved(i);
                count++;
            }
        }

        saveData();
        showInfo("Удалено товаров: " + count);
    }

    private void saveData() {
        ArrayList<ShoppingList> allLists = (ArrayList<ShoppingList>) dataManager.loadLists();
        allLists.set(listPosition, currentList);
        dataManager.saveLists(allLists);
    }
    private void showInfo(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}