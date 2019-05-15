package com.stefan.weathraw.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.stefan.weathraw.R;
import com.stefan.weathraw.databinding.ActivityMainBinding;
import com.stefan.weathraw.ui.fragment.CityDataFragment;
import com.stefan.weathraw.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        replaceFragment(CityDataFragment.newInstance(), false, CityDataFragment.class.getSimpleName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, String tag) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(tag).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }

}
