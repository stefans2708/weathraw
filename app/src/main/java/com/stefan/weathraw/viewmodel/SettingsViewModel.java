package com.stefan.weathraw.viewmodel;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.stefan.weathraw.NotificationBroadcastReceiver;
import com.stefan.weathraw.cache.model.City;
import com.stefan.weathraw.cache.repository.CityRepository;
import com.stefan.weathraw.model.ApplicationSettings;
import com.stefan.weathraw.model.CityList;
import com.stefan.weathraw.utils.Constants;
import com.stefan.weathraw.utils.SharedPrefsUtils;
import com.stefan.weathraw.utils.SingleLiveEvent;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewModel extends BaseApplicationViewModel {

    private static final int RC_DAILY_ALARM = 3;

    private LiveData<String> currentWidgetCityName;
    private MutableLiveData<Integer> currentWidgetCityId = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> changeCityClick = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> cityChanged = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> autoUpdateStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> enableNotifications = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> notificationTestClick = new SingleLiveEvent<>();
    private SingleLiveEvent<DateTime> notificationTimeClick = new SingleLiveEvent<>();
    private MutableLiveData<String> notificationTime = new MutableLiveData<>();

    private ApplicationSettings settings;
    private CityRepository cityRepository = new CityRepository();

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
        if (settings == null) {
            settings = ApplicationSettings.defaultValues();
            SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        }

        currentWidgetCityName = Transformations.switchMap(currentWidgetCityId, new Function<Integer, LiveData<String>>() {
            @Override
            public LiveData<String> apply(Integer cityId) {
                return cityRepository.getCityNameById(cityId);
            }
        });

        currentWidgetCityId.setValue(settings.getWidgetCityId());
        notificationTime.setValue(getTimeFormat(settings.getNotificationTimeHour(), settings.getNotificationTimeMinute()));
        enableNotifications.setValue(settings.isNotificationEnabled());
    }

    public void onNewCitySelected(int newCityId) {
        settings.setWidgetCityId(newCityId);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        currentWidgetCityId.setValue(newCityId);
        cityChanged.setValue(true);
    }

    public void updateFavouritesList(City city) {
        CityList cityList = SharedPrefsUtils.getObject(Constants.SELECTED_CITIES, CityList.class);
        List<City> cities = cityList == null ? new ArrayList<City>() : cityList.getCities();
        cities.add(city);
        cityList = new CityList(cities);
        SharedPrefsUtils.putObject(Constants.SELECTED_CITIES, cityList);
    }

    public void updateNotificationTime(int hourOfDay, int minute) {
        settings.setNotificationTimeHour(hourOfDay);
        settings.setNotificationTimeMinute(minute);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        notificationTime.setValue(getTimeFormat(hourOfDay, minute));
        cancelDailyAlarm();
        setDailyAlarm();
    }

    private String getTimeFormat(int hour, int minute) {
        DateTime dateTime = DateTime.now().withTime(hour, minute, 0, 0);
        return DateTimeFormat.forPattern("HH:mm").print(dateTime);
    }

    public void onCityClick() {
        changeCityClick.setValue(true);
    }

    public void onAutoRefreshClick(boolean isChecked) {
        settings.setWidgetAutoRefreshEnabled(isChecked);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        autoUpdateStatus.setValue(isChecked);
    }

    public void onEnableNotificationClick(boolean isChecked) {
        settings.setNotificationEnabled(isChecked);
        SharedPrefsUtils.putObject(Constants.APP_SETTINGS, settings);
        enableNotifications.setValue(isChecked);

        if (isChecked) {
            setDailyAlarm();
        } else {
            cancelDailyAlarm();
        }
    }

    private void setDailyAlarm() {
        AlarmManager alarmManager = ((AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE));
        Intent intent = new Intent(getApplication(), NotificationBroadcastReceiver.class);
        intent.setAction(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplication(), RC_DAILY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            DateTime alarmTime = DateTime.now()
                    .withTime(settings.getNotificationTimeHour(), settings.getNotificationTimeMinute(), 0, 0);
            if (alarmTime.isBeforeNow()) {
                alarmTime = alarmTime.plusDays(1);
            }
            alarmManager.setRepeating(AlarmManager.RTC, alarmTime.getMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    private void cancelDailyAlarm() {
        AlarmManager alarmManager = ((AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE));
        Intent intent = new Intent(getApplication(), NotificationBroadcastReceiver.class);
        intent.setAction(NotificationBroadcastReceiver.ACTION_SHOW_NOTIFICATION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplication(), RC_DAILY_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }

    public void onNotificationTestClick() {
        notificationTestClick.setValue(true);
    }

    public void onNotificationTimeClick() {
        notificationTimeClick.setValue(DateTime.now().withTime(settings.getNotificationTimeHour(), settings.getNotificationTimeMinute(),0,0));
    }

    public LiveData<Boolean> getChangeCityClick() {
        return changeCityClick;
    }

    public ApplicationSettings getSettings() {
        return settings;
    }

    public LiveData<String> getCurrentWidgetCityName() {
        return currentWidgetCityName;
    }

    public LiveData<Boolean> getAutoUpdateStatus() {
        return autoUpdateStatus;
    }

    public LiveData<Boolean> getNotificationsStatus() {
        return enableNotifications;
    }

    public LiveData<Boolean> getNotificationTestClick() {
        return notificationTestClick;
    }

    public LiveData<DateTime> getNotificationTimeClick() {
        return notificationTimeClick;
    }

    public LiveData<String> getNotificationTime() {
        return notificationTime;
    }

    public LiveData<Boolean> getCityChanged() {
        return cityChanged;
    }
}
