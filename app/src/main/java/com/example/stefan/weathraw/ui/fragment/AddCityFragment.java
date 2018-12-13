package com.example.stefan.weathraw.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.FragmentAddCityBinding;
import com.example.stefan.weathraw.viewmodel.AddCityViewModel;

public class AddCityFragment extends BaseFragment {

    private AddCityViewModel viewModel;
    private FragmentAddCityBinding binding;

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            viewModel = ViewModelProviders.of(this).get(AddCityViewModel.class);
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_city, container, false);
            binding.setViewModel(viewModel);
        }
        return binding.getRoot();
    }
}
