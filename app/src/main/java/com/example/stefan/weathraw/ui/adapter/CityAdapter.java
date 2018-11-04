package com.example.stefan.weathraw.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.stefan.weathraw.databinding.ItemCityCurrentWeatherBinding;
import com.example.stefan.weathraw.databinding.ItemCityForecastBinding;
import com.example.stefan.weathraw.model.FiveDayCityForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.viewmodel.ItemCurrentWeatherViewModel;
import com.example.stefan.weathraw.viewmodel.ItemForecastViewModel;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_WEATHER = 1;
    private static final int ITEM_FORECAST = 2;

    private WeatherData weatherData;
    private FiveDayCityForecast forecastData;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == ITEM_WEATHER ? new ItemWeatherViewHolder(ItemCityCurrentWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false))
                : new ItemForecastViewHolder(ItemCityForecastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_WEATHER) {
            ((ItemWeatherViewHolder) holder).bind(new ItemCurrentWeatherViewModel(weatherData));
        } else {
            ((ItemForecastViewHolder) holder).bind(new ItemForecastViewModel(forecastData));
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_WEATHER : ITEM_FORECAST;
    }

    public class ItemWeatherViewHolder extends RecyclerView.ViewHolder {
        private ItemCityCurrentWeatherBinding binding;

        ItemWeatherViewHolder(ItemCityCurrentWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemCurrentWeatherViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

    public class ItemForecastViewHolder extends RecyclerView.ViewHolder {
        private ItemCityForecastBinding binding;

        ItemForecastViewHolder(ItemCityForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemForecastViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }

}
