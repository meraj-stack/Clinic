package com.example.clinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.Animatable;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class SplashActivity extends AppCompatActivity {
    ImageView imageview;
    TextView txt_title, txt_slogan;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageview = findViewById(R.id.imageview);
        txt_slogan = findViewById(R.id.txt_slogan);
        txt_title = findViewById(R.id.txt_title);
        txt_title.setTranslationY(1000f);
        txt_slogan.setTranslationY(1000f);


        Glide.with(SplashActivity.this).asGif().load("file:///android_asset/heartbeat1.gif").into(imageview);
        txt_title.animate().translationY(0f).setDuration(1800).setStartDelay(0);
        txt_slogan.animate().translationY(0f).setDuration(2700).setStartDelay(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        },4000);


















    }



}
