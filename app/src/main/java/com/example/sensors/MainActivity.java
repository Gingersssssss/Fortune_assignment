package com.example.sensors;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    private static final int POLL_INTERVAL = 500;
    private Handler hdr = new Handler();
    private PowerManager.WakeLock wl;
    SensorInfo sensor_info = new SensorInfo();
    Boolean shown_dialog = false;
    private static final int shake_threshold = 15;

    private final Runnable pollTask = new Runnable() {
        public void run() {
            showDialog();
            hdr.postDelayed(pollTask, POLL_INTERVAL);
        }
    };

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // กำหนด VideoView
        VideoView stickVideoView = findViewById(R.id.stickView);
        stickVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sticks));
        stickVideoView.setMediaController(new MediaController(this));
        stickVideoView.requestFocus();

        // สั่งให้ Video เล่นอัตโนมัติ
        stickVideoView.start();

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
    }//end onCreate

    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // TO DO
    }//end onAccuracyChanged

    public void onSensorChanged(SensorEvent event){

    }//end onSensorChanged

    public void showDialog() {

        if( (Math.abs(sensor_info.accX)>shake_threshold) || (Math.abs(sensor_info.accY)>shake_threshold) || (Math.abs(sensor_info.accZ)>shake_threshold) ) {
            if(!shown_dialog) {
                shown_dialog = true;
                final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
                viewDialog.setIcon(android.R.drawable.btn_star_big_on);
                viewDialog.setTitle("ข้อความ");
                viewDialog.setMessage("โทรศัพท์มีการเขย่า");
                viewDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                shown_dialog = false;
                            }
                        });
                viewDialog.show();
            }//end if
        }//end if
    }//end showDialog


    @SuppressLint("WakelockTimeout")
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);



        if (!wl.isHeld()) {
            wl.acquire();
        }
        hdr.postDelayed(pollTask, POLL_INTERVAL);
    }//end onResume

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        if (wl.isHeld()) {
            wl.release();
        }
        hdr.removeCallbacks(pollTask);
    }//end onPause

    static class SensorInfo{
        float accX, accY, accZ;
        float graX, graY, graZ;
        float gyrX, gyrY, gyrZ;
        float light;
        float laccX, laccY, laccZ;
        float magX, magY, magZ;
        float orX, orY, orZ;
        float proximity;
        float rotX, rotY, rotZ;
    }//end class SensorInfo
}//end MainActivity