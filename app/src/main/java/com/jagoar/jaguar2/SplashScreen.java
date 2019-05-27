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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        //Splash Screen
        ImageView imagen = (ImageView) findViewById(R.id.jaguar);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splashanim);

        imagen.startAnimation(myanim);
        imagen.setVisibility(imagen.INVISIBLE);

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
