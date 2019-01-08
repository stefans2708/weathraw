package com.example.stefan.weathraw.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.Item24hForecastBinding;
import com.example.stefan.weathraw.databinding.ItemCityCurrentWeatherBinding;
import com.example.stefan.weathraw.databinding.ItemCityForecastBinding;
import com.example.stefan.weathraw.databinding.ItemCityForecastContentBinding;
import com.example.stefan.weathraw.model.DayAverageValues;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.model.WeatherData;
import com.example.stefan.weathraw.utils.HourXAxisFormatter;
import com.example.stefan.weathraw.utils.WeatherDataUtils;
import com.example.stefan.weathraw.viewmodel.ItemCurrentWeatherViewModel;
import com.example.stefan.weathraw.viewmodel.ItemForecastViewModel;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_WEATHER = 1;
    private static final int ITEM_24H_FORECAST = 2;
    private static final int ITEM_DAILY_FORECAST = 3;

    private WeatherAndForecast weatherAndForecast;
    private View.OnClickListener onChangeCityClickListener;

    public CityAdapter(WeatherAndForecast data) {
        this.weatherAndForecast = data;
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

    public void setOnChangeCityListener(View.OnClickListener onClickListener) {
        this.onChangeCityClickListener = onClickListener;
    }

    public void setData(WeatherAndForecast data) {
        this.weatherAndForecast = data;
        notifyDataSetChanged();
    }

    public class ItemWeatherViewHolder extends RecyclerView.ViewHolder {
        private ItemCityCurrentWeatherBinding binding;

        ItemWeatherViewHolder(final ItemCityCurrentWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.txtChangeCity.setOnClickListener(onChangeCityClickListener);
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
            addItems(context, viewModel.getDayAverageValuesList());
        }

        private void addItems(Context context, List<DayAverageValues> dayAverageValuesList) {
            LayoutInflater inflater = LayoutInflater.from(context);
            binding.linearItemsHolder.removeAllViews();
            for (int i = 0; i < 4; i++) {
                ItemCityForecastContentBinding itemBinding =
                        ItemCityForecastContentBinding.inflate(LayoutInflater.from(context), binding.linearItemsHolder, false);
                itemBinding.setDayAverageValues(dayAverageValuesList.get(i));
                binding.executePendingBindings();
                binding.linearItemsHolder.addView(inflater.inflate(R.layout.view_separator, binding.linearItemsHolder, false));
                binding.linearItemsHolder.addView(itemBinding.getRoot());
            }

        }

    }

    public class Item24ForecastViewHolder extends RecyclerView.ViewHolder {
        private Item24hForecastBinding binding;

        Item24ForecastViewHolder(Item24hForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemForecastViewModel viewModel) {
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            setChartData(viewModel);
            setImages(viewModel);
        }

        private void setImages(ItemForecastViewModel viewModel) {
            if (viewModel == null || viewModel.getForecast() == null) return;

            binding.linearImagesContainer.removeAllViews();
            List<WeatherData> list = viewModel.getForecast().getList();
            for (int i = 0; i < 8; i++) {
                ImageView imageView = new ImageView(binding.txtTitle.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                imageView.setLayoutParams(params);

                WeatherData data = list.get(i); //todo: sacuvati slike offline i na osnovu id-a uzimati...
                Glide.with(binding.txtTitle.getContext())
                        .load(WeatherDataUtils.getIconUrl(data.getWeatherDescription().getIcon()))
                        .into(imageView);

                binding.linearImagesContainer.addView(imageView);
            }
        }

        private void setChartData(ItemForecastViewModel viewModel) {
            LineDataSet dataSet = new LineDataSet(viewModel.generate24HoursForecast(), null);
            dataSet.disableDashedLine();
            dataSet.setColors(binding.txtTitle.getContext().getResources().getColor(R.color.colorAccent));
            dataSet.setLineWidth(3);
            dataSet.setCircleColors(binding.txtTitle.getContext().getResources().getColor(R.color.colorPrimaryDark));
            dataSet.setCircleRadius(3);
            dataSet.setValueTextSize(8);
            dataSet.setDrawVerticalHighlightIndicator(false);
            LineData lineData = new LineData(dataSet);
            binding.lineChart24hForecast.setData(lineData);
            binding.lineChart24hForecast.setTouchEnabled(false);
            binding.lineChart24hForecast.setDescription(null);
            binding.lineChart24hForecast.getLegend().setEnabled(false);
            binding.lineChart24hForecast.getAxisLeft().setDrawLabels(false);
            binding.lineChart24hForecast.bringToFront();

            XAxis xAxis = binding.lineChart24hForecast.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new HourXAxisFormatter(viewModel));

            YAxis yAxis = binding.lineChart24hForecast.getAxisRight();
            yAxis.setDrawLabels(false);
            LimitLine limitLine = new LimitLine(0);
            limitLine.setLineColor(Color.BLACK);
            limitLine.setLineWidth(1);
            yAxis.addLimitLine(limitLine);

            binding.lineChart24hForecast.invalidate();
        }

    }

}
