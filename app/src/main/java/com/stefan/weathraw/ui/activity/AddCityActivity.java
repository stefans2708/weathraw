package com.stefan.weathraw.ui.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.stefan.weathraw.R;
import com.stefan.weathraw.cache.model.City;
import com.stefan.weathraw.databinding.ActivityAddCityBinding;
import com.stefan.weathraw.ui.adapter.AddCityAdapter;
import com.stefan.weathraw.viewmodel.AddCityViewModel;
import com.jakewharton.rxbinding3.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
                ((AddCityAdapter) binding.recyclerAddCity.getAdapter()).setItems(cities);
            }
        });
    }

    private void initViews() {
        binding.recyclerAddCity.setHasFixedSize(true);
        binding.recyclerAddCity.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerAddCity.setAdapter(new AddCityAdapter(this));

        binding.searchView.setIconified(false);
        addDisposable(RxSearchView.queryTextChanges(binding.searchView)
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String query) throws Exception {
                        viewModel.setSearchQuery(query);
                    }
                }));
    }

    @Override
    public void onItemSelected(City city) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CITY, city);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
