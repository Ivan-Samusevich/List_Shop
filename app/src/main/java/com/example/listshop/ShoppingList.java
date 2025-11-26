package com.example.listshop;

import android.app.Activity;
import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class ShoppingList {
    private String title;
    private ArrayList<String> items;
    private ArrayList<Boolean> purchasedStatus;

    private DataManager dataManager;
    public ShoppingList(String title) {
        this(title, new ArrayList<>());
    }

    public ShoppingList(String title, ArrayList<String> items) {
        this.title = title;
        this.items = new ArrayList<>();
        this.purchasedStatus = new ArrayList<>();

        for (String item : items) {
            addItem(item);
        }
    }

    public void addItem(String item) {
        items.add(item);
        String subscribe = dataManager.loadSub();
        if(items.size() > 5 && subscribe.equals("no")){
        }
        purchasedStatus.add(false);
    }

    public void removeItem(int position) {
        if (isValidPosition(position)) {
            items.remove(position);
            purchasedStatus.remove(position);
        }
    }

    public void setPurchasedStatus(int position, boolean isPurchased) {
        if (isValidPosition(position)) {
            purchasedStatus.set(position, isPurchased);
        }
    }


    public String getTitle() {
        return title;
    }

    public ArrayList<String> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isPurchased(int position) {
        return isValidPosition(position) && purchasedStatus.get(position);
    }

    public int getItemCount() {
        return items.size();
    }
    public int getPurchasedCount() {
        int count = 0;
        for (Boolean status : purchasedStatus) {
            if (status) count++;
        }
        return count;
    }
    private boolean isValidPosition(int position) {
        return position >= 0 && position < items.size();
    }

}