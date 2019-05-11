package com.example.stefan.weathraw.ui.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.WeatherApplication;
import com.example.stefan.weathraw.model.ApplicationSettings;
import com.example.stefan.weathraw.model.WidgetDataModel;
import com.example.stefan.weathraw.model.WidgetNextHourDataModel;
import com.example.stefan.weathraw.service.WidgetService;
import com.example.stefan.weathraw.ui.activity.MainActivity;
import com.example.stefan.weathraw.utils.Constants;
import com.example.stefan.weathraw.utils.SharedPrefsUtils;
import com.example.stefan.weathraw.utils.WeatherDataUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
            case WidgetService.ACTION_UPDATED_SETTINGS: {
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
        remoteViews.setOnClickPendingIntent(R.id.img_icon, startAppPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.txt_temperature, startAppPendingIntent);

        refreshAllWidgetInstances(context);
    }

    private void setWidgetData(Context context, Intent intent) {
        WidgetDataModel widgetData = intent.getParcelableExtra(WidgetService.EXTRA_WEATHER_DATA);
        if (widgetData == null) return;

        RemoteViews remoteViews = getRemoteViews(context);
        remoteViews.setTextViewText(R.id.txt_city_name, widgetData.getCity());
        remoteViews.setTextViewText(R.id.txt_temperature, WeatherDataUtils.getFormattedTemperature(widgetData.getTemperature()));
        remoteViews.setTextViewText(R.id.txt_description, widgetData.getDescription());
        remoteViews.setTextViewText(R.id.txt_date, WeatherDataUtils.getDayHourFormat(new Date()));
        remoteViews.setTextViewText(R.id.txt_todays_date, WeatherDataUtils.getDayMonthFormat(new Date()));

        setNextHoursWidgetData(remoteViews, widgetData.getNextHoursData());

        loadWidgetImage(R.id.img_icon, widgetData.getIconId());
//        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.image_icon, remoteViews, AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WeatherWidgetProvider.class)));
//        Glide.with(context.getApplicationContext())
//                .asBitmap()
//                .load(WeatherDataUtils.getOfflineIconUrl(widgetData.getIconId()))
//                .into(appWidgetTarget);

        refreshAllWidgetInstances(context);
    }

    private void setNextHoursWidgetData(RemoteViews remoteViews, List<WidgetNextHourDataModel> nextHoursData) {
        WidgetNextHourDataModel nextHourData = nextHoursData.get(0);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_1, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_1_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(R.id.img_next_data_1, nextHourData.getIcon());

        nextHourData = nextHoursData.get(1);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_2, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_2_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(R.id.img_next_data_2, nextHourData.getIcon());

        nextHourData = nextHoursData.get(2);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_3, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_3_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(R.id.img_next_data_3, nextHourData.getIcon());

        nextHourData = nextHoursData.get(3);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_4, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_4_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(R.id.img_next_data_4, nextHourData.getIcon());

        nextHourData = nextHoursData.get(4);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_5, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_5_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(R.id.img_next_data_5, nextHourData.getIcon());
    }

    private void loadWidgetImage(int imageId, String imageUrl) {
        AssetManager assets = WeatherApplication.getAppContext().getAssets();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(assets.open(imageUrl + ".png"));
            remoteViews.setImageViewBitmap(imageId, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startWidgetUpdateService(Context context) {
        WidgetService.enqueueJob(context,
                new Intent(context, WidgetService.class).setAction(WidgetService.ACTION_REFRESH));
    }

    private void changeProgressState(Context context, boolean showProgressBar) {
        RemoteViews remoteViews = getRemoteViews(context);
        remoteViews.setViewVisibility(R.id.progress_bar, showProgressBar ? View.VISIBLE : View.GONE);
        remoteViews.setViewVisibility(R.id.image_refresh, showProgressBar ? View.INVISIBLE : View.VISIBLE);
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
