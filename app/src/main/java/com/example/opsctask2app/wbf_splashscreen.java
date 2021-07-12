package com.example.opsctask2app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class wbf_splashscreen extends AppCompatActivity {

    //Setting Variables
    Animation topSplash, bottomSplash;
    ImageView imgSplash;
    TextView head, subHead;

    //Set timeout
    private static int SPLASH_SCREEN = 4500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.wbf_splashscreen);

        //Hide Action Bar
        getSupportActionBar().hide();

        //Disable dark mode settings
        //stackoverflow.com. 2021. How to disable night mode in my application even if night mode is enabled in andr: Android Tutorial.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Setting screen orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Setting animations
        topSplash = AnimationUtils.loadAnimation(this, R.anim.top_animation_splash);
        bottomSplash = AnimationUtils.loadAnimation(this, R.anim.bottom_animation_splash);

        imgSplash = findViewById(R.id.imgSplashLogo);
        head = findViewById(R.id.tvHeaderSplash);
        subHead = findViewById(R.id.tvSubHeadSplash);

        imgSplash.setAnimation(topSplash);
        head.setAnimation(bottomSplash);
        subHead.setAnimation(bottomSplash);

        //Checking if user is first time user
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);

        //tutorialspoint.com. 2021.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //If first time user navigate to wbf_user_guide
                if (isFirstRun)
                {
                   getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
                   Intent intent = new Intent(wbf_splashscreen.this, wbf_user_guide.class);
                   startActivity(intent);
                   finish();
                }
                //If not first time user navigate to wbf_main
                else {
                    Intent intent = new Intent(wbf_splashscreen.this, wbf_main.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);

    }
}
