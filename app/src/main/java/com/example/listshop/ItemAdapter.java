package com.example.listshop;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ShoppingList shoppingList;

    public ItemAdapter(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemName = shoppingList.getItems().get(position);
        boolean isPurchased = shoppingList.isPurchased(position);


        holder.itemName.setText(itemName);
        updateItemStyle(holder.itemName, isPurchased);

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(isPurchased);


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shoppingList.setPurchasedStatus(position, isChecked);
            updateItemStyle(holder.itemName, isChecked);
        });
    }
    private void updateItemStyle(TextView textView, boolean isPurchased) {
        if (isPurchased) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
    @Override
    public int getItemCount() {
        return shoppingList.getItemCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            checkBox = itemView.findViewById(R.id.itemCheckbox);

            itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }
    }
}