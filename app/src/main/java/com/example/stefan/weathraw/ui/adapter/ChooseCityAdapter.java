package com.example.stefan.weathraw.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.databinding.ItemAddMoreBinding;
import com.example.stefan.weathraw.databinding.ItemCityBinding;
import com.example.stefan.weathraw.model.CityList;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;

public class ChooseCityAdapter extends BaseAdapter<City, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_REGULAR = 1;
    private static final int VIEW_TYPE_LAST = 2;

    private OnItemClickListener listener;

    public ChooseCityAdapter(CityList cities, OnItemClickListener listener) {
        if (cities == null) {
            cities = new CityList();
        }
        this.listener = listener;
        setItems(cities.getCities());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE_REGULAR ? new CityItemViewHolder(ItemCityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false))
                : new AddMoreViewHolder(ItemAddMoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_REGULAR) {
            ((CityItemViewHolder) holder).binding.setItemName(getItem(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == super.getItemCount() ? VIEW_TYPE_LAST : VIEW_TYPE_REGULAR;
    }

    class CityItemViewHolder extends RecyclerView.ViewHolder {

        private ItemCityBinding binding;

        CityItemViewHolder(ItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setOnClickListeners();
        }

        private void setOnClickListeners() {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getItem(getAdapterPosition()).getId());
                    }
                }
            });

            binding.imgRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCityFromList(getAdapterPosition());
                }
            });
        }

    }

    class AddMoreViewHolder extends RecyclerView.ViewHolder {

        private ItemAddMoreBinding binding;

        AddMoreViewHolder(ItemAddMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setOnClickListeners();
        }

        private void setOnClickListeners() {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAddMoreClick();
                    }
                }
            });
        }
    }

    private void removeCityFromList(int position) {
        removeItem(position);
        SharedPrefsUtils.putObject(Constants.SELECTED_CITIES, new CityList(getItems()));
    }

    public interface OnItemClickListener {
        void onItemClick(int cityId);

        void onAddMoreClick();
    }
}
