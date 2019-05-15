package com.stefan.weathraw.ui.widget;

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

import com.stefan.weathraw.R;
import com.stefan.weathraw.WeatherApplication;
import com.stefan.weathraw.service.WidgetService;

import java.io.IOException;

import static android.content.Context.ALARM_SERVICE;

public abstract class BaseAppWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_PROVIDER_TYPE = "EXTRA_PROVIDER_TYPE";
    public static final int EXTRA_PROVIDER_TYPE_REGULAR = 1;
    public static final int EXTRA_PROVIDER_TYPE_DARK = 2;
    private static final int RC_REFRESH = 123;

    private RemoteViews remoteViews;
    private ComponentName componentName;
    private AppWidgetManager appWidgetManager;

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        cancelAlarm(context);
    }

    protected void startWidgetUpdateService(Context context) {
        WidgetService.enqueueJob(context,
                new Intent(context, WidgetService.class)
                        .putExtra(EXTRA_PROVIDER_TYPE, getProviderType())
                        .setAction(WidgetService.ACTION_REFRESH));
    }

    protected void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, getProviderClass());
        intent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent alarmAction = PendingIntent.getBroadcast(context, RC_REFRESH, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmAction);
        }
    }

    protected void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, getProviderClass());
        intent.setAction(WidgetService.ACTION_REFRESH);
        PendingIntent alarmAction = PendingIntent.getBroadcast(context, RC_REFRESH, intent, 0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                    AlarmManager.INTERVAL_HOUR, alarmAction);
        }
    }

    protected void changeProgressState(Context context, boolean showProgressBar) {
        RemoteViews remoteViews = getRemoteViews(context);
        remoteViews.setViewVisibility(R.id.progress_bar, showProgressBar ? View.VISIBLE : View.GONE);
        remoteViews.setViewVisibility(R.id.image_refresh, showProgressBar ? View.INVISIBLE : View.VISIBLE);
        refreshAllWidgetInstances(context);
    }

    protected void loadWidgetImage(RemoteViews remoteViews, int imageId, String imageUrl) {
        AssetManager assets = WeatherApplication.getAppContext().getAssets();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(assets.open(imageUrl + ".png"));
            remoteViews.setImageViewBitmap(imageId, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void refreshAllWidgetInstances(Context context) {
        if (appWidgetManager == null) {
            componentName = new ComponentName(context, getProviderClass());
            appWidgetManager = AppWidgetManager.getInstance(context);
        }

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        appWidgetManager.updateAppWidget(appWidgetIds, getRemoteViews(context));
    }

    protected RemoteViews getRemoteViews(Context context) {
        if (remoteViews == null) {
            remoteViews = new RemoteViews(context.getPackageName(), getLayout());
        }
        return remoteViews;
    }

    protected abstract Class<? extends BaseAppWidgetProvider> getProviderClass();

    protected abstract void initWidgetViews(Context context);

    public abstract int getLayout();

    public abstract int getProviderType();

    protected abstract void setWidgetData(Context context, Intent intent);
}
