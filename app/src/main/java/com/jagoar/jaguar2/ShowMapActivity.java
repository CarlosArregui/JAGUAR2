package com.jagoar.jaguar2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.system.Os;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String current_user;
    public ArrayList<Marker> lista_marker;
    public ArrayList<Punto>lista_puntos;
    Button btn_autoplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent login_inent=getIntent();
        if (login_inent.getStringExtra("currentUser")!=null){
            current_user=login_inent.getStringExtra("currentUser");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos");
        Query q=bbdd.orderByChild("creador").equalTo(current_user);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //saca datos y los catualiza en la vista
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Marker> lista_markerFirebase =new ArrayList<>();
                ArrayList<Punto> lista_puntosFirebase =new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Punto p = snapshot.getValue(Punto.class);
                    String coord =p.getCoord();
                    String[] latlong = coord.split(",");
                    String url=p.getUrl();
                    lista_puntosFirebase.add(p);
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng location = new LatLng(latitude, longitude);
                    Marker marker=mMap.addMarker(new MarkerOptions().position(location).title(p.getTitulo()));
                    lista_markerFirebase.add(marker);


                }
                lista_marker=lista_markerFirebase;
                lista_puntos=lista_puntosFirebase;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String titulo=marker.getTitle();
                LatLng coord=marker.getPosition();
                String coordenadas=coord.toString().replace("lat/lng: (","").replace(")","");
                String audio="";
                Log.v("jeje",coordenadas);
                for (Punto p: lista_puntos){
                    Log.v("jeje",p.getCoord());
                    if (coordenadas.equals(p.getCoord())){

                        audio=p.getUrl();
                    }
                }
                try {
                    MediaPlayer mediaPlayer=new MediaPlayer();
                    mediaPlayer.setDataSource(audio);

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            boolean audio=true;
                            mp.start();
                        }
                    });
                    mediaPlayer.prepare();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
}
