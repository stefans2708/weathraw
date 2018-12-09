package com.example.stefan.weathraw.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.databinding.Item24hForecastBinding;
import com.example.stefan.weathraw.databinding.ItemCityCurrentWeatherBinding;
import com.example.stefan.weathraw.databinding.ItemCityForecastBinding;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.utils.HourXAxisFormatter;
import com.example.stefan.weathraw.viewmodel.ItemCurrentWeatherViewModel;
import com.example.stefan.weathraw.viewmodel.ItemForecastViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_WEATHER = 1;
    private static final int ITEM_24H_FORECAST = 2;
    private static final int ITEM_DAILY_FORECAST = 3;

    private WeatherAndForecast weatherAndForecast;
    private OnClickListener listener;

    public CityAdapter(WeatherAndForecast data, OnClickListener listener) {
        this.weatherAndForecast = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_WEATHER: {
                return new ItemWeatherViewHolder(ItemCityCurrentWeatherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case ITEM_24H_FORECAST: {
                return new Item24ForecastViewHolder(Item24hForecastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
            case ITEM_DAILY_FORECAST:
            default: {
                return new ItemForecastViewHolder(ItemCityForecastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_WEATHER: {
                ((ItemWeatherViewHolder) holder).bind(new ItemCurrentWeatherViewModel(weatherAndForecast.getWeatherData()));
                break;
            }
            case ITEM_24H_FORECAST: {
                ((Item24ForecastViewHolder) holder).bind(new ItemForecastViewModel(weatherAndForecast.getForecastData()));
                break;
            }
            case ITEM_DAILY_FORECAST: {
                ((ItemForecastViewHolder) holder).bind(((ItemForecastViewHolder) holder).binding.cardViewContainer.getContext(), new ItemForecastViewModel(weatherAndForecast.getForecastData()));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEM_WEATHER;
            case 1:
                return ITEM_24H_FORECAST;
            case 2:
            default:
                return ITEM_DAILY_FORECAST;
        }
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

        }

    }

    public class Item24ForecastViewHolder extends RecyclerView.ViewHolder {
        private Item24hForecastBinding binding;

        public Item24ForecastViewHolder(Item24hForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemForecastViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            setChartData(viewModel);
        }

        private void setChartData(ItemForecastViewModel viewModel) {
            LineDataSet dataSet = new LineDataSet(viewModel.generate24HoursForecast(), null);
            dataSet.disableDashedLine();
            dataSet.setColors(Color.YELLOW);
            dataSet.setLineWidth(3);
            dataSet.setCircleColors(Color.BLUE);
            dataSet.setCircleRadius(3);
            dataSet.setValueTextSize(8);
            dataSet.setDrawVerticalHighlightIndicator(false);
            LineData lineData = new LineData(dataSet);
            binding.lineChart24hForecast.setData(lineData);
            binding.lineChart24hForecast.setVisibleXRangeMaximum(8);
            binding.lineChart24hForecast.setTouchEnabled(false);
            binding.lineChart24hForecast.setDescription(null);

            XAxis xAxis = binding.lineChart24hForecast.getXAxis();
            xAxis.setValueFormatter(new HourXAxisFormatter(viewModel));

            binding.lineChart24hForecast.invalidate();
        }

    }

    public interface OnClickListener {

    }
}
