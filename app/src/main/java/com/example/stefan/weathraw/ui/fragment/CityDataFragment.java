package com.example.stefan.weathraw.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.databinding.FragmentCityDataBinding;
import com.example.stefan.weathraw.model.BottomMenuItem;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.ui.activity.MainActivity;
import com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter;
import com.example.stefan.weathraw.ui.adapter.CityAdapter;
import com.example.stefan.weathraw.viewmodel.CityDataViewModel;

import static com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter.MENU_ITEM_ABOUT;
import static com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter.MENU_ITEM_ADD_CITY;
import static com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter.MENU_ITEM_SETTINGS;

public class CityDataFragment extends BaseFragment implements CityAdapter.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Observer<Throwable>,BottomMenuAdapter.OnMenuItemClickListener {

    private CityDataViewModel viewModel;
    private FragmentCityDataBinding binding;
    private CityAdapter adapter;
    private BottomSheetBehavior bottomSheetBehavior;

    public static CityDataFragment newInstance() {
        return new CityDataFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            viewModel = ViewModelProviders.of(this).get(CityDataViewModel.class);
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_data, container, false);
            binding.setViewModel(viewModel);
        }

        if (adapter != null) {
            binding.recyclerView.setAdapter(adapter);
        }

        initViews();
        setUpObservers();
        return binding.getRoot();
    }

    @Override
    public void onChanged(@Nullable Throwable error) {
        super.onChanged(error);
        changeRefreshingStatus(false);
    }

    private void setUpObservers() {
        viewModel.getErrorMessage().observe(this, this);
        viewModel.getWeatherLiveData().observe(this, new Observer<WeatherAndForecast>() {
            @Override
            public void onChanged(@Nullable WeatherAndForecast weatherAndForecast) {
                setAdapter(weatherAndForecast);
                changeRefreshingStatus(false);
            }
        });
        viewModel.getBottomMenuState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean expand) {
                if (expand == null) return;
                bottomSheetBehavior.setState(expand ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void initViews() {
        binding.recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                binding.swipeToRefresh.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
        binding.swipeToRefresh.setOnRefreshListener(this);
        changeRefreshingStatus(true);

        bottomSheetBehavior = BottomSheetBehavior.from(binding.linearBottomSheet);
        binding.recyclerBottomMenu.setHasFixedSize(true);
        binding.recyclerBottomMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerBottomMenu.setAdapter(new BottomMenuAdapter(getContext(), this));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        binding.imgToggleBottomSheet.setImageResource(R.drawable.ic_arrow_down);
                        binding.swipeToRefresh.setEnabled(false);
                        viewModel.setBottomMenuState(true);
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        binding.imgToggleBottomSheet.setImageResource(R.drawable.ic_arrow_up);
                        binding.swipeToRefresh.setEnabled(true);
                        viewModel.setBottomMenuState(false);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void setAdapter(WeatherAndForecast data) {
        if (adapter == null) {
            adapter = new CityAdapter(data, this);
            binding.recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        viewModel.getData();
        changeRefreshingStatus(true);
    }

    private void changeRefreshingStatus(boolean isRefreshing) {
        binding.swipeToRefresh.setRefreshing(isRefreshing);
    }

    @Override
    public void onMenuItemClick(BottomMenuItem bottomMenuItem) {
        switch (bottomMenuItem.getItemType()) {
            case MENU_ITEM_SETTINGS: {
                ((MainActivity) getActivity()).replaceFragment(SettingsFragment.newInstance(), true, SettingsFragment.class.getSimpleName());
                break;
            }
            case MENU_ITEM_ADD_CITY: {
                ((MainActivity) getActivity()).replaceFragment(AddCityFragment.newInstance(), true, SettingsFragment.class.getSimpleName());
                break;
            }
            case MENU_ITEM_ABOUT: {
                ((MainActivity) getActivity()).replaceFragment(AboutFragment.newInstance(), true, SettingsFragment.class.getSimpleName());
                break;
            }
        }
    }
}
