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
import com.example.stefan.weathraw.model.CityList;
import com.example.stefan.weathraw.ui.adapter.ChooseCityAdapter;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;

public class ChooseCityDialog extends DialogFragment implements ChooseCityAdapter.OnItemClickListener {

    private DialogChooseCityBinding binding;
    private OnItemClickListener listener;

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
        cities.add(new City(784227, "Vranje", "RS"));
        cities.add(new City(787657, "Nis", "RS"));
        cities.add(new City(788709, "Leskovac", "RS"));
        CityList cityList = new CityList(cities);
        SharedPrefsUtils.putObject(Constants.SELECTED_CITIES, cityList);
        binding.recyclerCities.setAdapter(new ChooseCityAdapter(cities, this));
        binding.recyclerCities.setNestedScrollingEnabled(false);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(Integer cityId) {
        if(listener != null) {
            listener.onItemClick(cityId);
        }
    }

    @Override
    public void onAddMoreClick() {
        if(listener != null) {
            listener.onAddMoreClick();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Integer city);
        void onAddMoreClick();
    }
}
