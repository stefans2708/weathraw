package com.example.stefan.weathraw.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.example.stefan.weathraw.NotificationBroadcastReceiver;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.cache.model.City;
import com.example.stefan.weathraw.databinding.FragmentSettingsBinding;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.service.NotificationService;
import com.example.stefan.weathraw.service.WidgetService;
import com.example.stefan.weathraw.ui.activity.AddCityActivity;
import com.example.stefan.weathraw.ui.dialog.ChooseCityDialog;
import com.example.stefan.weathraw.viewmodel.SettingsViewModel;

import org.joda.time.DateTime;

import static com.example.stefan.weathraw.ui.fragment.CityDataFragment.RC_ADD_MORE;

public class SettingsFragment extends BaseFragment implements ChooseCityDialog.OnDialogItemClickListener {

    private static final int RC_DAILY_ALARM = 3;
    private SettingsViewModel viewModel;
    private FragmentSettingsBinding binding;
    private ChooseCityDialog dialog;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                viewModel.updateNotificationTime(hourOfDay, minute);
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar();
        setObservers();
    }

    private void setObservers() {
        viewModel.getChangeCityClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    dialog = ChooseCityDialog.newInstance();
                    dialog.setOnItemClickListener(SettingsFragment.this);
                    dialog.show(getActivity().getFragmentManager(), "choose_day_dialog");
                }
            }
        });

        viewModel.getCurrentWidgetCityName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                updateWidget();
            }
        });
        viewModel.getAutoUpdateStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                updateWidgetWithAction(WidgetService.ACTION_UPDATE_SETTINGS);
            }
        });
        viewModel.getNotificationsStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean notificationsEnabled) {
                if (notificationsEnabled == null) return;

                binding.switchEnableNotification.setEnabled(notificationsEnabled);
                Intent intent = new Intent(getContext(), NotificationBroadcastReceiver.class);
                intent.setAction(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION);
                if (notificationsEnabled) {
                    setDailyAlarm();
                } else {
                    cancelDailyAlarm();
                }
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
        viewModel.getNotificationTimeClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean click) {
                if (click == null) return;
                new TimePickerDialog(getContext(), onTimeSetListener, 12, 0, true).show();
            }
        });
    }

    private void setDailyAlarm() {
        ApplicationSettings settings = viewModel.getSettings();
        AlarmManager alarmManager = ((AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE));
        Intent intent = new Intent(getContext(), NotificationBroadcastReceiver.class);
        intent.setAction(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), RC_DAILY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            DateTime alarmTime = DateTime.now()
                    .withTime(settings.getNotificationTimeHour(), settings.getNotificationTimeMinute(),0,0);
            if (alarmTime.isBeforeNow()) {
                alarmTime = alarmTime.plusDays(1);
            }
            alarmManager.setRepeating(AlarmManager.RTC, alarmTime.getMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    private void cancelDailyAlarm() {
        AlarmManager alarmManager = ((AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE));
        Intent intent = new Intent(getContext(), NotificationBroadcastReceiver.class);
        intent.setAction(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), RC_DAILY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }

    private void setUpToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar((android.support.v7.widget.Toolbar) binding.getRoot().findViewById(R.id.toolbar));
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getContext().getString(R.string.settings));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_MORE && data != null) {
            City city = data.getParcelableExtra(AddCityActivity.EXTRA_CITY);
            if (city != null) {
                dialog.refreshListAfterInsertion(city);
                viewModel.updateFavouritesList(city);
            }
        }
    }

    @Override
    public void onItemClick(Integer cityId) {
        viewModel.onNewCitySelected(cityId);
    }

    @Override
    public void onAddMoreClick() {
        startActivityForResult(new Intent(getContext(), AddCityActivity.class), RC_ADD_MORE);
    }
}
