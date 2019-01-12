package com.example.stefan.weathraw.model;

import com.example.stefan.weathraw.utils.Constants;

public class ApplicationSettings {

    private int widgetCityId;
    private boolean widgetAutoRefreshEnabled;
    private boolean notificationEnabled;

    public ApplicationSettings(int widgetCityId, boolean widgetAutoRefreshEnabled, boolean notificationEnabled) {
        this.widgetCityId = widgetCityId;
        this.widgetAutoRefreshEnabled = widgetAutoRefreshEnabled;
        this.notificationEnabled = notificationEnabled;
    }

    public int getWidgetCityId() {
        return widgetCityId;
    }

    public void setWidgetCityId(int widgetCityId) {
        this.widgetCityId = widgetCityId;
    }

    public boolean isWidgetAutoRefreshEnabled() {
        return widgetAutoRefreshEnabled;
    }

    public void setWidgetAutoRefreshEnabled(boolean widgetAutoRefreshEnabled) {
        this.widgetAutoRefreshEnabled = widgetAutoRefreshEnabled;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public static ApplicationSettings defaultValues() {
        return new ApplicationSettings(Constants.CITY_NIS, false, false);
    }
}
