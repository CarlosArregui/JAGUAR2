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
    private ImageView imagen2;
    private ImageView imagen;
    private Animation myanim2, myanim,myanim3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        //Splash Screen
        imagen = (ImageView) findViewById(R.id.jaguar);
        imagen2 = (ImageView) findViewById(R.id.rugido_id);
        myanim = AnimationUtils.loadAnimation(this, R.anim.zoom);
        myanim2 = AnimationUtils.loadAnimation(this, R.anim.shake);
        myanim3 = AnimationUtils.loadAnimation(this, R.anim.small);
        imagen.startAnimation(myanim);
        /*myanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imagen2.startAnimation(myanim2);
                myanim2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imagen.startAnimation(myanim3);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    */

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
