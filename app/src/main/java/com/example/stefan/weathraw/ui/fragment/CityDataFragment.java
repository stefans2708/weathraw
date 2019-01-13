package com.example.stefan.weathraw.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.databinding.FragmentCityDataBinding;
import com.example.stefan.weathraw.model.BottomMenuItem;
import com.example.stefan.weathraw.model.WeatherAndForecast;
import com.example.stefan.weathraw.ui.activity.AddCityActivity;
import com.example.stefan.weathraw.ui.activity.MainActivity;
import com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter;
import com.example.stefan.weathraw.ui.adapter.CityAdapter;
import com.example.stefan.weathraw.ui.dialog.ChooseCityDialog;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.viewmodel.CityDataViewModel;

import static com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter.MENU_ITEM_ABOUT;
import static com.example.stefan.weathraw.ui.adapter.BottomMenuAdapter.MENU_ITEM_SETTINGS;

public class CityDataFragment extends BaseFragment implements ChooseCityDialog.OnDialogItemClickListener, SwipeRefreshLayout.OnRefreshListener, Observer<Throwable>, BottomMenuAdapter.OnMenuItemClickListener {

    public static final int RC_ADD_MORE = 123;
    private CityDataViewModel viewModel;
    private FragmentCityDataBinding binding;
    private CityAdapter adapter;
    private ChooseCityDialog dialog;
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
                if (weatherAndForecast == null || weatherAndForecast.getForecastData() == null
                        || weatherAndForecast.getWeatherData() == null)
                    return;
                setAdapter(weatherAndForecast);
                changeRefreshingStatus(false);
            }
        });
        viewModel.getBottomMenuState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean expand) {
                if (expand == null) return;
                bottomSheetBehavior.setState(expand ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void initViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                binding.swipeToRefresh.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
        binding.swipeToRefresh.setOnRefreshListener(this);
        changeRefreshingStatus(true);

        binding.recyclerBottomMenu.setHasFixedSize(true);
        binding.recyclerBottomMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerBottomMenu.setAdapter(new BottomMenuAdapter(getContext(), this));
        bottomSheetBehavior = BottomSheetBehavior.from(binding.linearBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        binding.swipeToRefresh.setEnabled(false);
                        viewModel.setBottomMenuState(true);
                        break;
                    }

                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        binding.swipeToRefresh.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                        viewModel.setBottomMenuState(false);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (!Float.isNaN(slideOffset)) {
                    binding.fab.setRotation(180f * (1 + slideOffset));
                }
            }

        });
    }

    private void setAdapter(WeatherAndForecast data) {
        if (adapter == null) {
            adapter = new CityAdapter(data);
            adapter.setOnChangeCityListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    dialog = ChooseCityDialog.newInstance();
                    dialog.setOnItemClickListener(CityDataFragment.this);
                    dialog.show(getActivity().getFragmentManager(), "choose_day_dialog");
                }
            });
            binding.recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(data);
        }
    }

    @Override
    public void onRefresh() {
        viewModel.refreshData();
        changeRefreshingStatus(true);
    }

    private void changeRefreshingStatus(boolean isRefreshing) {
        binding.swipeToRefresh.setRefreshing(isRefreshing);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_MORE && data != null) {
            City city = data.getParcelableExtra(AddCityActivity.EXTRA_CITY);
            if (city != null) {
                viewModel.updateFavouritesList(city);
                dialog.refreshListAfterInsertion(city);
            }
        }
    }

    @Override
    public void onMenuItemClick(BottomMenuItem bottomMenuItem) {
        switch (bottomMenuItem.getItemType()) {
            case MENU_ITEM_SETTINGS: {
                ((MainActivity) getActivity()).replaceFragment(SettingsFragment.newInstance(), true, SettingsFragment.class.getSimpleName());
                break;
            }

            case MENU_ITEM_ABOUT: {
                ((MainActivity) getActivity()).replaceFragment(AboutFragment.newInstance(), true, SettingsFragment.class.getSimpleName());
                break;
            }
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onItemClick(Integer cityId) {
        SharedPrefsUtils.putInteger(Constants.CURRENT_CITY_ID, cityId);
        viewModel.getData(cityId);
        changeRefreshingStatus(true);
    }

    @Override
    public void onAddMoreClick() {
        startActivityForResult(new Intent(getContext(), AddCityActivity.class), RC_ADD_MORE);
    }
}
