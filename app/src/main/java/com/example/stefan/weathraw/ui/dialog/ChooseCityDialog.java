package com.example.stefan.weathraw.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.databinding.DialogChooseCityBinding;
import com.example.stefan.weathraw.model.CityList;
import com.example.stefan.weathraw.ui.adapter.ChooseCityAdapter;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;

public class ChooseCityDialog extends DialogFragment implements ChooseCityAdapter.OnItemClickListener {

    private DialogChooseCityBinding binding;
    private OnDialogItemClickListener listener;

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
        CityList cities = SharedPrefsUtils.getObject(Constants.SELECTED_CITIES, CityList.class);
        binding.recyclerCities.setAdapter(new ChooseCityAdapter(cities, this));

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(int cityId) {
        if (listener != null) {
            listener.onItemClick(cityId);
            dismiss();
        }
    }

    @Override
    public void onAddMoreClick() {
        if (listener != null) {
            listener.onAddMoreClick();
        }
    }

    public void setOnItemClickListener(OnDialogItemClickListener listener) {
        this.listener = listener;
    }

    public void refreshListAfterInsertion(City city) {
        ((ChooseCityAdapter) binding.recyclerCities.getAdapter()).addItem(city);
        binding.recyclerCities.smoothScrollToPosition(binding.recyclerCities.getAdapter().getItemCount());
    }

    public interface OnDialogItemClickListener {
        void onItemClick(Integer city);

        void onAddMoreClick();
    }
}
