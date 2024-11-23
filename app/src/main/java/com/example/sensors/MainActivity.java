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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

                //สร้างคู่หมายเลขและคำทำนาย
                Map<Integer, String> fortuneMap = new HashMap<>();
                fortuneMap.put(1, "ดวงดีราบรื่น\n" +
                        "สิ่งที่หวังไว้จะสำเร็จลุล่วง ไม่ว่าจะเป็นเรื่องงานหรือความรัก สุขภาพแข็งแรง โชคลาภกำลังจะมา ให้ทำบุญเสริมดวงเพื่อรับสิ่งดี ๆ");
                fortuneMap.put(2, "ช้าแต่มั่นคง\n" +
                        "ผลของการกระทำในอดีตจะเริ่มส่งผล คุณอาจต้องอดทนกับอุปสรรคสักระยะ แต่ทุกอย่างจะดีขึ้นในอนาคต");
                fortuneMap.put(3, "พบการเปลี่ยนแปลง\n" +
                        "การงานหรือความรักอาจมีการเปลี่ยนแปลงที่ไม่คาดคิด แต่สุดท้ายจะนำไปสู่สิ่งที่ดีกว่า อย่ากลัวที่จะเริ่มต้นใหม่");
                fortuneMap.put(4, "มีโชคลาภเล็กน้อย\n" +
                        "สิ่งที่ตั้งใจไว้จะสำเร็จเพียงบางส่วน อย่าเพิ่งท้อแท้ ให้ตั้งใจทำต่อไป คนที่รักคุณจะช่วยสนับสนุน");
                fortuneMap.put(5, "กลาง ๆ ไม่เด่นชัด\n" +
                        "ดวงอยู่ในช่วงนิ่ง ต้องพึ่งพาความขยันและความอดทนเป็นหลัก หมั่นทำบุญและรักษาสุขภาพจะช่วยเสริมพลังดี");
                fortuneMap.put(6, "เจอความท้าทาย\n" +
                        "การงานมีคู่แข่ง ความรักอาจมีความไม่เข้าใจ แต่ถ้าตั้งใจและสื่อสารกันดี ทุกอย่างจะคลี่คลาย");
                fortuneMap.put(7, "สุขภาพต้องระวัง\n" +
                        "ระวังอาการเจ็บป่วยหรืออุบัติเหตุเล็ก ๆ น้อย ๆ การงานจะมีคนช่วยเหลือ แต่ต้องระมัดระวังเรื่องเอกสารหรือคำพูด");
                fortuneMap.put(8, "ความรักสดใส\n" +
                        "หากยังโสด มีโอกาสพบคนถูกใจเร็ว ๆ นี้ ถ้ามีคู่ ความรักจะราบรื่น การงานดีขึ้นหลังจากที่คุณพยายามมานาน");
                fortuneMap.put(9, "สิ่งดีงามกำลังมา\n" +
                        "ทุกอย่างเริ่มเข้าที่เข้าทาง โชคลาภกำลังปรากฏ อย่าลืมแบ่งปันความสุขให้ผู้อื่น ความรักและงานจะสมดุล");
                fortuneMap.put(10, "ดวงต้องสร้างเอง\n" +
                        "ช่วงนี้อาจดูเหมือนไม่มีโชคมากนัก แต่ถ้าพยายามสุดความสามารถ จะมีคนช่วยเปิดทางให้สำเร็จ หมั่นทำความดีเพื่อสร้างพลังบวก");

                //สุ่มหมายเลขเซียมซี
                int randomFortuneNumber = new Random().nextInt(fortuneMap.size()) + 1;
                String fortune = fortuneMap.get(randomFortuneNumber);

                final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
                viewDialog.setIcon(android.R.drawable.btn_star_big_on);
                viewDialog.setTitle("ผลการทำนาย");
                viewDialog.setMessage("หมายเลขเซียมซี: " + randomFortuneNumber + "\nผลการทำนาย: " + fortune);
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