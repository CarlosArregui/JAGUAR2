package com.jagoar.jaguar2;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class SliderActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET }, 2);
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle(getString(R.string.whats_jaguar));
        sliderPage.setImageDrawable(R.drawable.jaguar);
        sliderPage.setDescription(getString(R.string.slider_descripcion_1));
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.backgroundcolorDark));
        showSkipButton(true);
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle(getString(R.string.how_does_it_work));
        sliderPage1.setImageDrawable(R.drawable.privacy);
        sliderPage1.setDescription(getString(R.string.slider_descripcion_2));
        sliderPage1.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent1));
        addSlide(AppIntroFragment.newInstance(sliderPage1));


    }
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(intent);
    }
}
