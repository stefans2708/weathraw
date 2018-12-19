package com.example.stefan.weathraw.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.databinding.ItemAddMoreBinding;
import com.example.stefan.weathraw.databinding.ItemCityBinding;

import java.util.List;

public class ChooseCityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_REGULAR = 1;
    private static final int VIEW_TYPE_LAST = 2;
    private List<City> cities;

    public ChooseCityAdapter(List<City> cities) {
        this.cities = cities;
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
            ((CityItemViewHolder) holder).binding.setItemName(cities.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return cities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == cities.size() ? VIEW_TYPE_LAST : VIEW_TYPE_REGULAR;
    }

    class CityItemViewHolder extends RecyclerView.ViewHolder {

        private ItemCityBinding binding;

        CityItemViewHolder(ItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    class AddMoreViewHolder extends RecyclerView.ViewHolder {

        private ItemAddMoreBinding binding;

        AddMoreViewHolder(ItemAddMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
