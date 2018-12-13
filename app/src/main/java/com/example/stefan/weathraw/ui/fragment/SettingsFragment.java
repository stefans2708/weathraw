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
import com.example.stefan.weathraw.databinding.FragmentSettingsBinding;
import com.example.stefan.weathraw.viewmodel.SettingsViewModel;

public class SettingsFragment extends BaseFragment {

    private SettingsViewModel viewModel;
    private FragmentSettingsBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
            binding.setViewModel(viewModel);
        }
        return binding.getRoot();
    }

}
