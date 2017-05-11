package com.example.android.shopinggururegister.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.android.shopinggururegister.R;

/**
 * Created by Android on 06-08-2016.
 */
public class SplashActivity extends AppCompatActivity {
    ImageView splashLogoImageView;
    Animation animationSplash;
    protected boolean active = true;
    protected int splashTime = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.splash_screen_layout);
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    startAnimations();
                    int waited = 0;
                    while (active && (waited < splashTime)) {
                        sleep(100);
                        if (active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }
            }
        };
        splashThread.start();
    }

    private void startAnimations() {
        animationSplash = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        splashLogoImageView = (ImageView) findViewById(R.id.splashLogoImageView);
        splashLogoImageView.startAnimation(animationSplash);
    }
}
