package com.stefan.weathraw.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.TypedValue;

import com.stefan.weathraw.WeatherApplication;

public class GeneralUtils {

    public static float pixelToDp(int pixel) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixel, WeatherApplication.getAppContext().getResources().getDisplayMetrics());
    }

    public static int dpToPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, WeatherApplication.getAppContext().getResources().getDisplayMetrics());
    }


    public static boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) WeatherApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
