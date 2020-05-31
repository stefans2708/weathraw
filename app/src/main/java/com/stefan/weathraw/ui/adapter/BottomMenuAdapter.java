package com.stefan.weathraw.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stefan.weathraw.R;
import com.stefan.weathraw.databinding.ItemBottomSheetBinding;
import com.stefan.weathraw.model.BottomMenuItem;
import com.stefan.weathraw.viewmodel.BottomMenuItemViewModel;

import java.util.Arrays;

public class BottomMenuAdapter extends BaseAdapter<BottomMenuItem, BottomMenuAdapter.ItemViewHolder> {

    public static final int MENU_ITEM_SETTINGS = 1;
    public static final int MENU_ITEM_ABOUT = 2;
    private static final int MENU_ITEMS_COUNT = 2;

    private OnMenuItemClickListener listener;

    public BottomMenuAdapter(Context context, OnMenuItemClickListener listener) {
        this.listener = listener;
        setItems(Arrays.asList(
                new BottomMenuItem(MENU_ITEM_SETTINGS, context.getString(R.string.menu_item_settings)),
                new BottomMenuItem(MENU_ITEM_ABOUT, context.getString(R.string.menu_item_about))
        ));
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(ItemBottomSheetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final BottomMenuItem item = getItem(position);
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

        ItemViewHolder(ItemBottomSheetBinding binding) {
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
