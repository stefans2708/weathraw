package com.stefan.weathraw.model;

import com.stefan.weathraw.utils.Constants;

public class ApplicationSettings {

    private int widgetCityId;
    private boolean widgetAutoRefreshEnabled;
    private boolean notificationEnabled;
    private int notificationTimeHour;
    private int notificationTimeMinute;

    public ApplicationSettings(int widgetCityId, boolean widgetAutoRefreshEnabled, boolean notificationEnabled, int notificationTimeHour, int notificationTimeMinute) {
        this.widgetCityId = widgetCityId;
        this.widgetAutoRefreshEnabled = widgetAutoRefreshEnabled;
        this.notificationEnabled = notificationEnabled;
        this.notificationTimeHour = notificationTimeHour;
        this.notificationTimeMinute = notificationTimeMinute;
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

    public int getNotificationTimeHour() {
        return notificationTimeHour;
    }

    public void setNotificationTimeHour(int notificationTimeHour) {
        this.notificationTimeHour = notificationTimeHour;
    }

    public int getNotificationTimeMinute() {
        return notificationTimeMinute;
    }

    public void setNotificationTimeMinute(int notificationTimeMinute) {
        this.notificationTimeMinute = notificationTimeMinute;
    }

    public static ApplicationSettings defaultValues() {
        return new ApplicationSettings(Constants.DEFAULT_CITY_ID, false, false, 12, 0);
    }
}
