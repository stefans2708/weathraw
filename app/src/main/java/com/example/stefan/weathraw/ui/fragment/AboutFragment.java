package com.example.stefan.weathraw.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.FragmentAboutBinding;

public class AboutFragment extends BaseFragment {

    private FragmentAboutBinding binding;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
        }
        setUpToolbar();
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        binding.txtAboutApi.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(
                (android.support.v7.widget.Toolbar) binding.getRoot().findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.about));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
