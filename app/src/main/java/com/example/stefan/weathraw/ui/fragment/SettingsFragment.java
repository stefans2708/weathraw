package com.example.stefan.weathraw.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar();
    }

    private void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar((android.support.v7.widget.Toolbar) binding.getRoot().findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getContext().getString(R.string.settings));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
