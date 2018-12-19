package com.example.stefan.weathraw.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.ActivityAddCityBinding;
import com.example.stefan.weathraw.viewmodel.AddCityViewModel;

public class AddCityActivity extends BaseActivity {

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

    }

}
