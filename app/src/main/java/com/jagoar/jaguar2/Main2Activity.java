package com.jagoar.jaguar2;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class Main2Activity extends AppCompatActivity {
    Context contexto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        contexto = this;
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
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.principal:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.creados:
                            selectedFragment = new CreadosFragment();
                            break;
                        case R.id.asistencia:
                            selectedFragment = null;
                            startActivity(new Intent(Main2Activity.this,MapsActivity.class));
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
