package com.example.ldbach.gyroraisetowake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ldbach on 10/25/17.
 */

public class ScreenStatusListenerReceiver extends BroadcastReceiver {

    Intent gyroIntent = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Listen to ScreenOn and ScreenOff
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // Unregister RaiseToWake service
            // Log.d("ScreenOn", "hm:");
            if (gyroIntent == null) return;
            context.stopService(gyroIntent);
            gyroIntent = null;

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // Register RaiseToWake service
            gyroIntent = RaiseListenerService.instance(context);
            context.startService(gyroIntent);
        }
    }
}
