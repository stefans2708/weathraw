package com.example.stefan.weathraw.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.stefan.weathraw.databinding.ItemCityCurrentWeatherBinding;
import com.example.stefan.weathraw.databinding.ItemCityForecastBinding;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.viewmodel.ItemCurrentWeatherViewModel;
import com.example.stefan.weathraw.viewmodel.ItemForecastViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_WEATHER = 1;
    private static final int ITEM_FORECAST = 2;

    private WeatherAndForecast weatherAndForecast;
    private OnClickListener listener;

    public CityAdapter(WeatherAndForecast data, OnClickListener listener) {
        this.weatherAndForecast = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return viewType == ITEM_WEATHER ? new ItemWeatherViewHolder(ItemCityCurrentWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false))
                : new ItemForecastViewHolder(ItemCityForecastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_WEATHER) {
            ((ItemWeatherViewHolder) holder).bind(new ItemCurrentWeatherViewModel(weatherAndForecast.getWeatherData()));
        } else {
            ((ItemForecastViewHolder) holder).bind(((ItemForecastViewHolder) holder).binding.lineChart.getContext(), new ItemForecastViewModel(weatherAndForecast.getForecastData()));
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

        public void bind(Context context, ItemForecastViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            setChartData(context, viewModel);
        }

        private void setChartData(Context context, ItemForecastViewModel viewModel) {
            LineDataSet dataSet = new LineDataSet(viewModel.generateTemperatureGraphEntries(), "Forecast");
            dataSet.setFillColor(android.R.color.holo_blue_bright);
            LineData lineData = new LineData(dataSet);
            binding.lineChart.setData(lineData);
            binding.lineChart.setDrawGridBackground(false);
            binding.lineChart.invalidate();
        }
    }

    public interface OnClickListener {

    }
}
