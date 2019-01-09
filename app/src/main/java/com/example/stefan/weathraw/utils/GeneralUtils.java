package com.example.stefan.weathraw.utils;

import android.util.TypedValue;

import com.example.stefan.weathraw.WeatherApplication;

public class GeneralUtils {

    public static float pixelToDp(int pixel) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixel, WeatherApplication.getAppContext().getResources().getDisplayMetrics());
    }

    public static int dpToPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, WeatherApplication.getAppContext().getResources().getDisplayMetrics());
    }

}
