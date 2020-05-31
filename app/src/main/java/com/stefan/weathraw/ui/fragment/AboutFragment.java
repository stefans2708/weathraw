package com.stefan.weathraw.ui.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stefan.weathraw.R;
import com.stefan.weathraw.databinding.FragmentAboutBinding;

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
                (androidx.appcompat.widget.Toolbar) binding.getRoot().findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.about));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
