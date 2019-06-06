package com.jagoar.jaguar2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ImageView jaguar;
    private Animation myanim2, myanim,myanim3;
    SharedPref sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true) {
            setTheme(R.style.darkTtheme);
        }else  setTheme(R.style.AppThemes);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        //Splash Screen
        jaguar = (ImageView) findViewById(R.id.jaguar);


        myanim = AnimationUtils.loadAnimation(this, R.anim.zoom);
        myanim2 = AnimationUtils.loadAnimation(this, R.anim.shake);


        jaguar.startAnimation(myanim);
        openApp(true);
    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(SplashScreen.this, ActivityLogin.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, SliderActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 2000);
    }
}
