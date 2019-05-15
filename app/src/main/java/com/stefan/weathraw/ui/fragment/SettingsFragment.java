package com.stefan.weathraw.ui.fragment;

import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.stefan.weathraw.NotificationBroadcastReceiver;
import com.stefan.weathraw.R;
import com.stefan.weathraw.cache.model.City;
import com.stefan.weathraw.databinding.FragmentSettingsBinding;
import com.stefan.weathraw.service.NotificationService;
import com.stefan.weathraw.service.WidgetService;
import com.stefan.weathraw.ui.activity.AddCityActivity;
import com.stefan.weathraw.ui.dialog.ChooseCityDialog;
import com.stefan.weathraw.viewmodel.SettingsViewModel;

import org.joda.time.DateTime;

import static com.stefan.weathraw.ui.fragment.CityDataFragment.RC_ADD_MORE;

public class SettingsFragment extends BaseFragment implements ChooseCityDialog.OnDialogItemClickListener {

    private SettingsViewModel viewModel;
    private FragmentSettingsBinding binding;
    private ChooseCityDialog dialog;

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
            binding.setLifecycleOwner(this);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar();
        setObservers();
    }

    private void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar((android.support.v7.widget.Toolbar) binding.getRoot().findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.settings));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setObservers() {
        viewModel.getChangeCityClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    dialog = ChooseCityDialog.newInstance();
                    dialog.setOnItemClickListener(SettingsFragment.this);
                    dialog.show(getActivity().getFragmentManager(), ChooseCityDialog.class.getSimpleName());
                }
            }
        });

        viewModel.getCityChanged().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean s) {
                updateWidget();
            }
        });

        viewModel.getAutoUpdateStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                updateWidgetWithAction(WidgetService.ACTION_UPDATED_SETTINGS);
            }
        });

        viewModel.getNotificationTestClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean click) {
                if (click == null) return;

                Intent intent = new Intent(getContext(), NotificationBroadcastReceiver.class);
                intent.setAction(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION);
                NotificationService.enqueueWork(getContext(), NotificationService.class, NotificationService.UNIQUE_JOB_ID, intent);

            }
        });

        viewModel.getNotificationTimeClick().observe(this, new Observer<DateTime>() {
            @Override
            public void onChanged(@Nullable DateTime previousSelectedTime) {
                if (previousSelectedTime == null) return;

                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        viewModel.updateNotificationTime(hourOfDay, minute);
                    }
                }, previousSelectedTime.getHourOfDay(), previousSelectedTime.getMinuteOfHour(), true).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_MORE && data != null) {
            City city = data.getParcelableExtra(AddCityActivity.EXTRA_CITY);
            if (city != null) {
                dialog.addCityToList(city);
                viewModel.updateFavouritesList(city);
            }
        }
    }

    @Override
    public void onCityItemClick(Integer cityId) {
        viewModel.onNewCitySelected(cityId);
    }

    @Override
    public void onAddMoreClick() {
        startActivityForResult(new Intent(getContext(), AddCityActivity.class), RC_ADD_MORE);
    }
}
