package com.example.stefan.weathraw.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.ItemBottomSheetBinding;
import com.example.stefan.weathraw.model.BottomMenuItem;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.viewmodel.BottomMenuItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ItemViewHolder> {

    public static final int MENU_ITEM_SETTINGS = 1;
    public static final int MENU_ITEM_ADD_CITY = 2;
    public static final int MENU_ITEM_ABOUT = 3;
    private static final int MENU_ITEMS_COUNT = 3;

    private List<BottomMenuItem> items;
    private OnMenuItemClickListener listener;

    public BottomMenuAdapter(Context context, OnMenuItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>(MENU_ITEMS_COUNT);
        items.add(new BottomMenuItem(MENU_ITEM_SETTINGS, context.getString(R.string.menu_item_settings)));
        items.add(new BottomMenuItem(MENU_ITEM_ADD_CITY, context.getString(R.string.menu_item_add_city)));
        items.add(new BottomMenuItem(MENU_ITEM_ABOUT, context.getString(R.string.menu_item_about)));
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(ItemBottomSheetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final BottomMenuItem item = items.get(position);
        holder.bind(new BottomMenuItemViewModel(item));
        holder.binding.txtItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMenuItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return MENU_ITEMS_COUNT;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ItemBottomSheetBinding binding;

        public ItemViewHolder(ItemBottomSheetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BottomMenuItemViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(BottomMenuItem item);
    }

}
