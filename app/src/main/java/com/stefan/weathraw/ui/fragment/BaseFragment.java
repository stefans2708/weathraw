package com.stefan.weathraw.ui.fragment;

import android.appwidget.AppWidgetManager;
import androidx.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.stefan.weathraw.ui.widget.BaseAppWidgetProvider;
import com.stefan.weathraw.ui.widget.WeatherWidgetProvider;
import com.stefan.weathraw.ui.widget.WeatherWidgetProviderDark;
import com.stefan.weathraw.utils.GeneralUtils;

import java.util.Arrays;
import java.util.List;

public abstract class BaseFragment extends Fragment implements Observer<Throwable> {

    @Override
    public void onChanged(@Nullable Throwable error) {
        if (error == null) return;

        if (!GeneralUtils.isNetworkAvailable()) {
            makeToast("No internet connection");
        } else {
            makeToast(error.getMessage());
        }
    }

    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void updateWidget() {
        List<Class<? extends BaseAppWidgetProvider>> widgetProviders =
                Arrays.asList(WeatherWidgetProvider.class, WeatherWidgetProviderDark.class);

        for (Class<? extends BaseAppWidgetProvider> widgetProviderClass : widgetProviders) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            ComponentName componentName = new ComponentName(getActivity(), widgetProviderClass);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            if (appWidgetIds.length != 0) {
                Intent intent = new Intent(getActivity(), widgetProviderClass);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                getActivity().sendBroadcast(intent);
            }
        }
    }

    public void updateWidgetWithAction(String action) {
        List<Class<? extends BaseAppWidgetProvider>> widgetProviders =
                Arrays.asList(WeatherWidgetProvider.class, WeatherWidgetProviderDark.class);

        for (Class<? extends BaseAppWidgetProvider> widgetProviderClass : widgetProviders) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            ComponentName componentName = new ComponentName(getActivity(), widgetProviderClass);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            if (appWidgetIds.length != 0) {
                Intent intent = new Intent(getActivity(), widgetProviderClass);
                intent.setAction(action);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                getActivity().sendBroadcast(intent);
            }
        }
    }

}
