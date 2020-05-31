package com.stefan.weathraw.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stefan.weathraw.cache.model.City;
import com.stefan.weathraw.databinding.ItemCityAddBinding;

public class AddCityAdapter extends BaseAdapter<City, AddCityAdapter.ItemViewHolder> {

    private OnItemSelectListener listener;

    public AddCityAdapter(OnItemSelectListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(ItemCityAddBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.binding.setItemName(getItem(position).getName());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemCityAddBinding binding;

        ItemViewHolder(ItemCityAddBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(getItem(getAdapterPosition()));
                    }
                }
            });
        }

    }

    public interface OnItemSelectListener {
        void onItemSelected(City city);
    }
}
