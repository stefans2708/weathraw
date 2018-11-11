package com.example.stefan.weathraw.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.model.WidgetDataModel;
import com.example.stefan.weathraw.service.WidgetService;
import com.example.stefan.weathraw.ui.activity.MainActivity;
import com.example.stefan.weathraw.utils.WeatherDataUtils;

import java.util.Date;

public class WeatherWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        initWidgetViews(context, appWidgetManager, appWidgetIds);
        startWidgetUpdateService(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent == null || intent.getAction() == null) return;

        switch (intent.getAction()) {
            case WidgetService.ACTION_UPDATE_RESPONSE: {
                setWidgetData(context, intent);
                break;
            }
            case WidgetService.ACTION_REFRESH: {
                startWidgetUpdateService(context);
                break;
            }
        }

    }

    private void initWidgetViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_weather);

        Intent refreshIntent = new Intent(context, WeatherWidgetProvider.class);
        refreshIntent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.image_refresh, refreshPendingIntent);

        Intent startAppIntent = new Intent(context, MainActivity.class);
        PendingIntent startAppPendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.image_icon, startAppPendingIntent);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    private void setWidgetData(Context context, Intent intent) {
        WidgetDataModel widgetData = intent.getParcelableExtra(WidgetService.EXTRA_WEATHER_DATA);
        if (widgetData == null) return;

        ComponentName componentName = new ComponentName(context, WeatherWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_weather);
        remoteViews.setTextViewText(R.id.text_city_name, widgetData.getCity());
        remoteViews.setTextViewText(R.id.text_temperature, WeatherDataUtils.getFormattedTemperature(widgetData.getTemperature()));
        remoteViews.setTextViewText(R.id.text_description, widgetData.getDescription());
        remoteViews.setTextViewText(R.id.text_date, WeatherDataUtils.getFormattedDate(new Date()));

        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.image_icon, remoteViews, appWidgetIds);
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(WeatherDataUtils.getIconUrl(widgetData.getIconId()))
                .into(appWidgetTarget);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        Toast.makeText(context, "Weather data updated", Toast.LENGTH_SHORT).show();
    }

    private void startWidgetUpdateService(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(WidgetService.ACTION_UPDATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }


}
