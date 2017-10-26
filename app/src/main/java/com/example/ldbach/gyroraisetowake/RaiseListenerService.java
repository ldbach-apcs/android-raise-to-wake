package com.example.ldbach.gyroraisetowake;

import android.app.AlarmManager;
import android.app.Instrumentation;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by ldbach on 10/25/17.
 */

public class RaiseListenerService extends Service implements SensorEventListener {

    private static final int REQUEST_WAKE = 113;
    SensorManager mSensorManager;
    Sensor mSensor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent instance(Context context) {
        return new Intent(context, RaiseListenerService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float rotation = Math.abs(event.values[0]);
        Log.d("Gyro", "Works: " + rotation);
        if (rotation >= 3.14f / 8f) {
            //Intent wakeScreen = WakeScreenActivity.instance(this);
            //Intent wakeScreen = new Intent(getApplicationContext(), WakeScreenActivity.class);
            //startActivity(wakeScreen);
            //Thread t = new Thread(new Runnable() {
            //    @Override
            //    public void run() {
            //        Instrumentation inst = new Instrumentation();
            //        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_POWER);
            //    }
            //});
            //t.run();

            Intent wakeScreenIntent = WakeScreenActivity.instance(this);
            PendingIntent pendingWake = PendingIntent.getActivity(
                    this, REQUEST_WAKE, wakeScreenIntent, 0);

            int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            final int HALF_SECOND_MILLIS = 500;

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setExact(
                        alarmType,
                        SystemClock.elapsedRealtime() + HALF_SECOND_MILLIS,
                        pendingWake);
                stopSelf();
            }
        }

    }

    @Override
    public void onDestroy() {
        Log.d("Gyro", "Service stopped");
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
