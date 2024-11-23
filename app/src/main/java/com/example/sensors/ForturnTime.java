package com.example.sensors;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForturnTime {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.noon_page);

        // กำหนด View Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sunlight), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // กำหนด VideoView
        VideoView sunVideoView = findViewById(R.id.sunlight);
        sunVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sun_light));
        sunVideoView.setMediaController(new MediaController(this));
        sunVideoView.requestFocus();

        // สั่งให้ Video เล่นอัตโนมัติ
        sunVideoView.start();

        // เพิ่ม animation
        animateVideoView(sunVideoView);

        // ตั้งเวลา animation
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(DayCount.this, MainActivity.class);
            startActivity(intent);
            finish(); // ปิด Activity ปัจจุบัน
        }, 3000); // 4000ms (3 วิ)
    }
}
