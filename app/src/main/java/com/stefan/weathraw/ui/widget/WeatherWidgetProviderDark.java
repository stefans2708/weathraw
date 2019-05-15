package com.stefan.weathraw.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.stefan.weathraw.R;
import com.stefan.weathraw.model.ApplicationSettings;
import com.stefan.weathraw.model.WidgetDataModel;
import com.stefan.weathraw.model.WidgetNextHourDataModel;
import com.stefan.weathraw.service.WidgetService;
import com.stefan.weathraw.ui.activity.MainActivity;
import com.stefan.weathraw.utils.Constants;
import com.stefan.weathraw.utils.SharedPrefsUtils;
import com.stefan.weathraw.utils.WeatherDataUtils;

import java.util.Date;
import java.util.List;

public class WeatherWidgetProviderDark extends BaseAppWidgetProvider {

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
    public int getLayout() {
        return R.layout.widget_weather_dark;
    }

    @Override
    public int getProviderType() {
        return EXTRA_PROVIDER_TYPE_DARK;
    }

    @Override
    protected Class<? extends BaseAppWidgetProvider> getProviderClass() {
        return WeatherWidgetProviderDark.class;
    }

    @Override
    protected void initWidgetViews(Context context) {

        RemoteViews remoteViews = getRemoteViews(context);

        Intent refreshIntent = new Intent(context, WeatherWidgetProviderDark.class);
        refreshIntent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.image_refresh, refreshPendingIntent);

        Intent startAppIntent = new Intent(context, MainActivity.class);
        PendingIntent startAppPendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_icon, startAppPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.txt_temperature, startAppPendingIntent);

        refreshAllWidgetInstances(context);
    }

    @Override
    protected void setWidgetData(Context context, Intent intent) {
        WidgetDataModel widgetData = intent.getParcelableExtra(WidgetService.EXTRA_WEATHER_DATA);
        if (widgetData == null) return;

        RemoteViews remoteViews = getRemoteViews(context);
        remoteViews.setTextViewText(R.id.txt_city_name, widgetData.getCity());
        remoteViews.setTextViewText(R.id.txt_temperature, WeatherDataUtils.getFormattedTemperature(widgetData.getTemperature()));
        remoteViews.setTextViewText(R.id.txt_description, widgetData.getDescription());
        remoteViews.setTextViewText(R.id.txt_date, WeatherDataUtils.getDayHourFormat(new Date()));

        setNextHoursWidgetData(remoteViews, widgetData.getNextHoursData());

        loadWidgetImage(remoteViews, R.id.img_icon, widgetData.getIconId());
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
        loadWidgetImage(remoteViews, R.id.img_next_data_1, nextHourData.getIcon());

        nextHourData = nextHoursData.get(1);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_2, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_2_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(remoteViews, R.id.img_next_data_2, nextHourData.getIcon());

        nextHourData = nextHoursData.get(2);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_3, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_3_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(remoteViews, R.id.img_next_data_3, nextHourData.getIcon());

        nextHourData = nextHoursData.get(3);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_4, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_4_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(remoteViews, R.id.img_next_data_4, nextHourData.getIcon());

        nextHourData = nextHoursData.get(4);
        remoteViews.setTextViewText(R.id.txt_next_data_hour_5, WeatherDataUtils.getHourMinuteFromUnixTime(nextHourData.getDateTime()));
        remoteViews.setTextViewText(R.id.txt_data_5_temperature, WeatherDataUtils.getTemperatureFormat(nextHourData.getTemperature()));
        loadWidgetImage(remoteViews, R.id.img_next_data_5, nextHourData.getIcon());
    }

}
