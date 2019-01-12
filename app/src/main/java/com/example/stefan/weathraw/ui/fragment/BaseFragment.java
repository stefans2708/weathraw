package com.example.stefan.weathraw.ui.fragment;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.stefan.weathraw.R;
import com.example.stefan.weathraw.model.ResponseMessage;
import com.example.stefan.weathraw.ui.widget.WeatherWidgetProvider;
import com.example.stefan.weathraw.viewmodel.BaseViewModel;

public abstract class BaseFragment extends Fragment implements Observer<Throwable> {

    private BaseViewModel baseViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
    }

    @Override
    public void onChanged(@Nullable Throwable error) {
        if (error == null) return;

        if (!hasInternetConnection()) {
            makeToast("No internet connection");
        } else {
            makeToast(error.getMessage());
        }
    }

    public void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    public void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
        ComponentName componentName = new ComponentName(getActivity(), WeatherWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        if (appWidgetIds.length != 0) {
            Intent intent = new Intent(getActivity(), WeatherWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            getActivity().sendBroadcast(intent);
        }
    }
}
