package com.example.stefan.weathraw.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.databinding.ActivityAddCityBinding;
import com.example.stefan.weathraw.ui.adapter.AddCityAdapter;
import com.example.stefan.weathraw.viewmodel.AddCityViewModel;

import java.util.List;

public class AddCityActivity extends BaseActivity implements AddCityAdapter.OnItemSelectListener {

    public static final String EXTRA_CITY = "EXTRA_CITY";
    private ActivityAddCityBinding binding;
    private AddCityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (binding == null) {
            viewModel = ViewModelProviders.of(this).get(AddCityViewModel.class);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_add_city);
            binding.setViewModel(viewModel);
        }

        initViews();
        setObservers();
    }

    private void setObservers() {
        viewModel.getCities().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(@Nullable List<City> cities) {
                ((AddCityAdapter) binding.recyclerAddCity.getAdapter()).addItems(cities);
            }
        });
    }

    private void initViews() {
        binding.recyclerAddCity.setHasFixedSize(true);
        binding.recyclerAddCity.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerAddCity.setAdapter(new AddCityAdapter(this));
    }

    @Override
    public void onItemSelected(City city) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CITY, city);
        setResult(12, intent);
        finish();
    }
}
