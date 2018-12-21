package com.example.stefan.weathraw.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.databinding.ItemCityAddBinding;

import java.util.ArrayList;
import java.util.List;

public class AddCityAdapter extends RecyclerView.Adapter<AddCityAdapter.ItemViewHolder> {

    private List<City> cityList = new ArrayList<>();
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
        holder.binding.setItemName(cityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void addItems(List<City> items) {
        this.cityList.addAll(items);
        notifyDataSetChanged();
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
                        listener.onItemSelected(cityList.get(getAdapterPosition()).getId());
                    }
                }
            });
        }

    }

    public interface OnItemSelectListener {
        void onItemSelected(int cityId);
    }
}
