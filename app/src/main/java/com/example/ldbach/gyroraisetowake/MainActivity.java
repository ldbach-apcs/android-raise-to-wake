package com.example.ldbach.gyroraisetowake;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.security.Permission;
import java.util.jar.Manifest;

import static android.Manifest.permission.WAKE_LOCK;

/*
 * This application's intention is to bring iPhone 6s and above feature
 * RaiseToWake onto Android devices (SDK 23+). Besides, it provides a
 * screensaver for basics control like WiFi, Bluetooth, etc.
 *
 * Workflow:
 * When screen is on, turn off RaiseToWake service
 * When screen is turned off, re-enable RaiseToWake service
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERM_WAKE_LOCK = 2307;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSensor();

        // Request wakelock permisison
        if (checkSelfPermission(WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }

        startService(ControlService.instance(this));
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {WAKE_LOCK}, PERM_WAKE_LOCK);
    }

    public void checkSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (accelerometerSensor == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("DEVICE UNSUPPORTED")
                    .setMessage("We are sorry, your device is not on supported devices list")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            builder.create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERM_WAKE_LOCK && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }
}
