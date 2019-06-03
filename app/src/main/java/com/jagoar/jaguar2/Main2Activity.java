package com.jagoar.jaguar2;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class Main2Activity extends AppCompatActivity {
    Context contexto;
    String current_user;
    SharedPref sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true) {
            setTheme(R.style.darkTtheme);
        }else  setTheme(R.style.AppThemes);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        contexto = this;
        Intent login_inent=getIntent();

        current_user=login_inent.getStringExtra("currentUser");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Bundle bundle = new Bundle();
                    bundle.putString("currentUser", current_user);
                    Fragment selectedFragment = null;




                    switch (item.getItemId()) {
                        case R.id.principal:

                            selectedFragment = new HomeFragment();
                            selectedFragment.setArguments(bundle);


                            break;
                        case R.id.creados:
                            selectedFragment = new BuscarFragment();
                            selectedFragment.setArguments(bundle);
                            break;
                        case R.id.asistencia:
                            selectedFragment = null;
                            Intent I = new Intent(contexto,MapsActivity.class);
                            I.putExtra("currentUser",current_user);
                            startActivity(I);

                            break;
                        case R.id.anadir:
                            selectedFragment = null;
                            Intent showMap = new Intent(contexto,ShowMapActivity.class);
                            showMap.putExtra("currentUser",current_user);
                            startActivity(showMap);

                            break;
                    }

                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };
}
