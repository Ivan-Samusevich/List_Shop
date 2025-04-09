package com.example.listshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ListViewHolder> {
    private List<ShoppingList> lists;
    private OnListClickListener listener;

    public interface OnListClickListener {
        void onListClick(int position);

        void onListLongClick(int position);
    }

    public ShoppingListAdapter(List<ShoppingList> lists, OnListClickListener listener) {
        this.lists = lists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ShoppingList list = lists.get(position);

        holder.listTitle.setText(list.getTitle());

        int purchasedCount = list.getPurchasedCount();
        int totalCount = list.getItemCount();
        holder.listStats.setText(purchasedCount + "/" + totalCount);

        holder.itemView.setOnClickListener(v -> listener.onListClick(position));
        holder.itemView.setOnLongClickListener(v -> {listener.onListLongClick(position);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void updateLists(List<ShoppingList> newLists) {
        lists = newLists;
        notifyDataSetChanged();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView listTitle;
        TextView listStats;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.list_title);
            listStats = itemView.findViewById(R.id.list_stats);
        }
    }
}