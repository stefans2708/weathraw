package com.example.stefan.weathraw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.stefan.weathraw.service.NotificationService;
import com.example.stefan.weathraw.service.WidgetService;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_SHOW_NOTIFICATION = "ACTION_SHOW_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        switch (intent.getAction()) {
            case ACTION_SHOW_NOTIFICATION: {
                startService(context);
                break;
            }
        }
    }

    private void startService(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_SHOW_NOTIFICATION);
        NotificationService.enqueueJob(context, intent);
    }
}
