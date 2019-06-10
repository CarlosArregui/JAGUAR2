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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
    Context contexto;
    String current_mail, current_user;
    SharedPref sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if (sharedpref.loadNightModeState() == true) {
            setTheme(R.style.darkTtheme);
        } else setTheme(R.style.AppThemes);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        contexto = this;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        current_mail = user.getEmail();


        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios");
        Query q = bbdd.orderByChild("correo").equalTo(current_mail);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    current_user = d.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                            selectedFragment = new BuscarFragment();
                            break;

                        case R.id.asistencia:
                            Intent I = new Intent(contexto, MapsActivity.class);
                            startActivity(I);
                            break;
                        case R.id.anadir:
                            selectedFragment = null;
                            Intent showMap = new Intent(contexto, ShowMapActivity.class);
                            showMap.putExtra("currentUser", current_user);
                            startActivity(showMap);
                            break;

                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };
}
