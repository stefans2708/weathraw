package com.stefan.weathraw.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mItems;

    public List<T> getItems() {
        return mItems;
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    public void setItems(List<T> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void addAllItems(List<T> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        this.mItems.add(item);
        notifyItemInserted(mItems.size());
    }

    public void removeItem(int position) {
        if (mItems == null) return;
        this.mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
