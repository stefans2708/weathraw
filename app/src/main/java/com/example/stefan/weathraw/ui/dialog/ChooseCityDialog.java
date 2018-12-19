package com.example.stefan.weathraw.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.cashe.model.City;
import com.example.stefan.weathraw.databinding.DialogChooseCityBinding;
import com.example.stefan.weathraw.ui.adapter.ChooseCityAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChooseCityDialog extends DialogFragment {

    private DialogChooseCityBinding binding;

    public static ChooseCityDialog newInstance() {
        return new ChooseCityDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DialogChooseCityBinding.inflate(inflater, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        binding.recyclerCities.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.recyclerCities.setHasFixedSize(true);
        List<City> cities = new ArrayList<>();
//        cities = (List<City>) SharedPrefsUtils.getObject(Constants.SELECTED_CITIES, cities.getClass());
        cities.add(new City(1, "Vranje", "RS"));
        cities.add(new City(2, "Nis", "RS"));
        cities.add(new City(3, "Leskovac", "RS"));
        binding.recyclerCities.setAdapter(new ChooseCityAdapter(cities));
        binding.recyclerCities.setNestedScrollingEnabled(false);
    }

}
