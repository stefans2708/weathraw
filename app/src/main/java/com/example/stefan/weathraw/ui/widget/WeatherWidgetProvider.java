package com.example.stefan.weathraw.ui.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.model.WidgetDataModel;
import com.example.stefan.weathraw.service.WidgetService;
import com.example.stefan.weathraw.ui.activity.MainActivity;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.GeneralUtils;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.utils.WeatherDataUtils;

import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class WeatherWidgetProvider extends AppWidgetProvider {

    private static final int RC_REFRESH = 123;
    private RemoteViews remoteViews;
    private ComponentName componentName;
    private AppWidgetManager appWidgetManager;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        initWidgetViews(context);
        startWidgetUpdateService(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
         super.onReceive(context, intent);

        if (intent == null || intent.getAction() == null) return;

        switch (intent.getAction()) {
            case WidgetService.ACTION_UPDATE_RESPONSE: {
                changeProgressState(context, false);
                setWidgetData(context, intent);
                break;
            }
            case WidgetService.ACTION_REFRESH: {
                changeProgressState(context, true);
                startWidgetUpdateService(context);
                break;
            }
            case WidgetService.ACTION_ERROR: {
                changeProgressState(context, false);
                int errorType = intent.getIntExtra(WidgetService.EXTRA_ERROR_TYPE, -1);
                Toast.makeText(context, context.getString(errorType == WidgetService.ERROR_TYPE_NO_INTERNET
                        ? R.string.no_internet_connection : R.string.request_timed_out), Toast.LENGTH_SHORT).show();
            }
            case WidgetService.ACTION_UPDATE_SETTINGS: {
                ApplicationSettings settings = SharedPrefsUtils.getObject(Constants.APP_SETTINGS, ApplicationSettings.class);
                if (settings == null) {
                    settings = ApplicationSettings.defaultValues();
                }

                if (settings.isWidgetAutoRefreshEnabled()) {
                    setAlarm(context);
                } else {
                    cancelAlarm(context);
                }
            }
        }

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        cancelAlarm(context);
    }

    private void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, WeatherWidgetProvider.class);
        intent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent alarmAction = PendingIntent.getBroadcast(context, RC_REFRESH, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmAction);
        }
    }

    private void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, WeatherWidgetProvider.class);
        intent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent alarmAction = PendingIntent.getBroadcast(context, RC_REFRESH, intent, 0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                    AlarmManager.INTERVAL_HALF_HOUR, alarmAction);
        }
    }

    private void initWidgetViews(Context context) {
        RemoteViews remoteViews = getRemoteViews(context);

        Intent refreshIntent = new Intent(context, WeatherWidgetProvider.class);
        refreshIntent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.image_refresh, refreshPendingIntent);

        Intent startAppIntent = new Intent(context, MainActivity.class);
        PendingIntent startAppPendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.image_icon, startAppPendingIntent);

        refreshAllWidgetInstances(context);
    }

    private void setWidgetData(Context context, Intent intent) {
        WidgetDataModel widgetData = intent.getParcelableExtra(WidgetService.EXTRA_WEATHER_DATA);
        if (widgetData == null) return;

        RemoteViews remoteViews = getRemoteViews(context);
        remoteViews.setViewVisibility(R.id.relative_data_container, View.VISIBLE);
        remoteViews.setTextViewText(R.id.text_city_name, widgetData.getCity());
        remoteViews.setTextViewText(R.id.text_temperature, WeatherDataUtils.getFormattedTemperature(widgetData.getTemperature()));
        remoteViews.setTextViewText(R.id.text_description, widgetData.getDescription());
        remoteViews.setTextViewText(R.id.text_date, WeatherDataUtils.getDayHourFormat(new Date()));
        remoteViews.setTextViewText(R.id.text_todays_date, WeatherDataUtils.getDayMonthFormat(new Date()));

        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.image_icon, remoteViews, AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WeatherWidgetProvider.class)));
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(WeatherDataUtils.getOfflineIconUrl(widgetData.getIconId()))
                .into(appWidgetTarget);

        refreshAllWidgetInstances(context);
        Toast.makeText(context, "Weather data updated", Toast.LENGTH_SHORT).show();
    }

    private void startWidgetUpdateService(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(WidgetService.ACTION_REFRESH);

        WidgetService.enqueueJob(context, intent);
    }

    private void changeProgressState(Context context, boolean showProgressBar) {
        RemoteViews remoteViews = getRemoteViews(context);
        remoteViews.setViewVisibility(R.id.progress_bar, showProgressBar ? View.VISIBLE : View.GONE);
        remoteViews.setViewVisibility(R.id.image_refresh, showProgressBar ? View.GONE : View.VISIBLE);
        refreshAllWidgetInstances(context);
    }

    private RemoteViews getRemoteViews(Context context) {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_weather);
        }
        return remoteViews;
    }

    private void refreshAllWidgetInstances(Context context) {
        if (appWidgetManager == null) {
            componentName = new ComponentName(context, WeatherWidgetProvider.class);
            appWidgetManager = AppWidgetManager.getInstance(context);
        }

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        appWidgetManager.updateAppWidget(appWidgetIds, getRemoteViews(context));
    }

}
