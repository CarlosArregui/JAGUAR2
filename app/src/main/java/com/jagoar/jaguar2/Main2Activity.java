package com.jagoar.jaguar2;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class Main2Activity extends AppCompatActivity {
    Context contexto;
    String current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        contexto = this;

        Intent login_inent=getIntent();
        current_user=login_inent.getStringExtra("currentUser");
        Log.v("jeje ","estamos en Main activity2: "+current_user);

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
                            Log.v("jeje","home fragment selected: "+current_user);


                            break;
                        case R.id.creados:
                            selectedFragment = new CreadosFragment();

                            Log.v("jeje","home fragment Creados");

                            break;
                        case R.id.asistencia:
                            selectedFragment = null;
                            Intent I = new Intent(contexto,MapsActivity.class);
                            I.putExtra("currentUser",current_user);
                            startActivity(I);

                            break;
                        case R.id.add_puntos:
                            selectedFragment = new AnadirFragment();
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
