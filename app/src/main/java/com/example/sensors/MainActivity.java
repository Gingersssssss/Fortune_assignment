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
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.TextView;

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

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
            TextView text1 = findViewById(R.id.textView1);
            text1.setText(R.string.accelerometer_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) == null){
            TextView text2 = findViewById(R.id.textView2);
            text2.setText(R.string.gravity_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null){
            TextView text3 = findViewById(R.id.textView3);
            text3.setText(R.string.gyroscope_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null){
            TextView text4 = findViewById(R.id.textView4);
            text4.setText(R.string.light_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) == null){
            TextView text5 = findViewById(R.id.textView5);
            text5.setText(R.string.linear_acceleration_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
            TextView text6 = findViewById(R.id.textView6);
            text6.setText(R.string.magnetic_field_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) == null){
            TextView text7 = findViewById(R.id.textView7);
            text7.setText(R.string.orientation_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null){
            TextView text8 = findViewById(R.id.textView8);
            text8.setText(R.string.proximity_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null){
            TextView text9 = findViewById(R.id.textView9);
            text9.setText(R.string.rotation_vector_na);
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null){
            TextView text10 = findViewById(R.id.textView10);
            text10.setText(R.string.ambient_temp_na);
        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Sensors Info");
    }//end onCreate

    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // TO DO
    }//end onAccuracyChanged

    public void onSensorChanged(SensorEvent event){
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            sensor_info.accX=event.values[0];
            sensor_info.accY=event.values[1];
            sensor_info.accZ=event.values[2];
        }
        if (type == Sensor.TYPE_GRAVITY) {
            sensor_info.graX=event.values[0];
            sensor_info.graY=event.values[1];
            sensor_info.graZ=event.values[2];
        }
        if (type == Sensor.TYPE_GYROSCOPE) {
            sensor_info.gyrX=event.values[0];
            sensor_info.gyrY=event.values[1];
            sensor_info.gyrZ=event.values[2];
        }
        if (type == Sensor.TYPE_LIGHT) {
            sensor_info.light=event.values[0];
        }
        if (type == Sensor.TYPE_LINEAR_ACCELERATION) {
            sensor_info.laccX=event.values[0];
            sensor_info.laccY=event.values[1];
            sensor_info.laccZ=event.values[2];
        }
        if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            sensor_info.magX=event.values[0];
            sensor_info.magY=event.values[1];
            sensor_info.magZ=event.values[2];
        }
        if (type == Sensor.TYPE_ORIENTATION) {
            sensor_info.orX=event.values[0];
            sensor_info.orY=event.values[1];
            sensor_info.orZ=event.values[2];
        }
        if (type == Sensor.TYPE_PROXIMITY) {
            sensor_info.proximity=event.values[0];
        }
        if (type == Sensor.TYPE_ROTATION_VECTOR) {
            sensor_info.rotX=event.values[0];
            sensor_info.rotY=event.values[1];
            sensor_info.rotZ=event.values[2];
        }
        if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            TextView text10 = (TextView) findViewById(R.id.textView10);
            text10.setText(String.format("Ambient Temperature : %1$s", event.values[0]));
        }
    }//end onSensorChanged

    public void showDialog() {
        String message1 = String.format("Accelerometer (m/s^2): \n x: %1$s \n y: %2$s \n z: %3$s", sensor_info.accX, sensor_info.accY, sensor_info.accZ);
        TextView text1 = findViewById(R.id.textView1);
        text1.setText(message1);

        String message2 = String.format("Gravity (m/s^2): \n x: %1$s \n y: %2$s \n z: %3$s", sensor_info.graX, sensor_info.graY, sensor_info.graZ);
        TextView text2 = findViewById(R.id.textView2);
        text2.setText(message2);

        String message3 = String.format("Gyroscope (rad/s) : \n x: %1$s \n y: %2$s \n z: %3$s", sensor_info.gyrX, sensor_info.gyrY, sensor_info.gyrZ);
        TextView text3 = findViewById(R.id.textView3);
        text3.setText(message3);

        String message4 = String.format("Light (lux) : %1$s", sensor_info.light);
        TextView text4 = findViewById(R.id.textView4);
        text4.setText(message4);

        String message5 = String.format("Linear Accr (m/s^2): \n x: %1$s \n y: %2$s \n z: %3$s", sensor_info.laccX, sensor_info.laccY, sensor_info.laccZ);
        TextView text5 = findViewById(R.id.textView5);
        text5.setText(message5);

        String message6 = String.format("Magnetic (microT) : \n x: %1$s \n y: %2$s \n z: %3$s", sensor_info.magX, sensor_info.magY, sensor_info.magZ);
        TextView text6 = findViewById(R.id.textView6);
        text6.setText(message6);

        String message7 = String.format("Orientation (deg) : \n z: %1$s \n x: %2$s \n y: %3$s", sensor_info.orX, sensor_info.orY, sensor_info.orZ);
        TextView text7 = findViewById(R.id.textView7);
        text7.setText(message7);

        String message8 = String.format("Proximity (cm) : %1$s", sensor_info.proximity);
        TextView text8 = findViewById(R.id.textView8);
        text8.setText(message8);

        String message9 = String.format("Rotation Vector : \n x: %1$s \n y: %2$s \n z: %3$s", sensor_info.rotX, sensor_info.rotY, sensor_info.rotZ);
        TextView text9 = findViewById(R.id.textView9);
        text9.setText(message9);

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

    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            int health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            String health_text = (health == 2) ? "GOOD" : "NOT GOOD";
            int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            String plugged_text = (plugged == 2) ? "USB" : (plugged == 1) ? "AC" : "UNPLUGGED";
            boolean present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            String status_text = (status == 3) ? "DISCHARGING" : (status == 2) ? "CHARGING" : "FULL";
            String technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            int voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);

            TextView text = findViewById(R.id.textView11);
            text.setText(   "Battery Info: \n"+
                    "Health: "+health_text+"\n"+
                    "Present: "+present+"\n"+
                    "Level: "+level+"  "+
                    "Scale: "+scale+"\n"+
                    "Plugged: "+plugged_text+"\n"+
                    "Status: "+status_text+"\n"+
                    "Technology: "+technology+"\n"+
                    "Temperature: "+temperature+"\n"+
                    "Voltage: "+voltage+"\n");
        }//end onReceive
    };

    @SuppressLint("WakelockTimeout")
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);

        this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (!wl.isHeld()) {
            wl.acquire();
        }
        hdr.postDelayed(pollTask, POLL_INTERVAL);
    }//end onResume

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        this.unregisterReceiver(this.batteryInfoReceiver);

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