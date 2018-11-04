package com.example.stefan.weathraw.ui.activity;

import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.ActivityMainBinding;
import com.example.stefan.weathraw.ui.fragment.CityDataFragment;
import com.example.stefan.weathraw.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        replaceFramgent(CityDataFragment.newInstance(), false);
    }

    private void replaceFramgent(Fragment fragment, boolean addToBackStack) {
//        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
    }


}
