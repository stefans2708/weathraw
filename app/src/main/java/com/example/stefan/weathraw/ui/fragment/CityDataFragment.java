package com.example.stefan.weathraw.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.FragmentCityDataBinding;
import com.example.stefan.weathraw.model.ResponseMessage;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.ui.adapter.CityAdapter;
import com.example.stefan.weathraw.viewmodel.CityDataViewModel;

public class CityDataFragment extends BaseFragment implements CityAdapter.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Observer<Throwable> {

    private CityDataViewModel viewModel;
    private FragmentCityDataBinding binding;
    private CityAdapter adapter;

    public static CityDataFragment newInstance() {
        return new CityDataFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            viewModel = ViewModelProviders.of(this).get(CityDataViewModel.class);
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_data, container, false);
            binding.setViewModel(viewModel);
        }

        if (adapter != null) {
            binding.recyclerView.setAdapter(adapter);
        }

        initViews();
        setUpObservers();
        return binding.getRoot();
    }

    @Override
    public void onChanged(@Nullable Throwable error) {
        super.onChanged(error);
        changeRefreshingStatus(false);
    }

    private void setUpObservers() {
        viewModel.getErrorMessage().observe(this, this);
        viewModel.getWeatherLiveData().observe(this, new Observer<WeatherAndForecast>() {
            @Override
            public void onChanged(@Nullable WeatherAndForecast weatherAndForecast) {
                setAdapter(weatherAndForecast);
                changeRefreshingStatus(false);
            }
        });
    }

    private void initViews() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.swipeToRefresh.setOnRefreshListener(this);
        changeRefreshingStatus(true);
    }

    private void setAdapter(WeatherAndForecast data) {
        if (adapter == null) {
            adapter = new CityAdapter(data, this);
            binding.recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        viewModel.getData();
        changeRefreshingStatus(true);
    }

    private void changeRefreshingStatus(boolean isRefreshing) {
        binding.swipeToRefresh.setRefreshing(isRefreshing);
    }
}
