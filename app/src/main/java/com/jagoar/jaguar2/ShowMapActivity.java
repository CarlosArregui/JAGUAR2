package com.jagoar.jaguar2;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String current_user;
    public ArrayList<Marker> lista_marker;
    public ArrayList<Punto>lista_puntos;
    Bitmap smallMarker;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent login_inent=getIntent();
        current_user=login_inent.getStringExtra("currentUser");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker1);
        Bitmap b=bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
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
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
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
            public boolean onMarkerClick(final Marker marker) {
                final LatLng coord=marker.getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 9), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        String coordenadas=coord.toString().replace("lat/lng: (","").replace(")","");
                        String audio="";
                        String titulo="";
                        int height = 80;
                        int width = 80;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.exploration);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap changeMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(changeMarker));
                        Log.v("jeje",coordenadas);
                        for (Punto p: lista_puntos){
                            Log.v("jeje",p.getCoord());
                            if (coordenadas.equals(p.getCoord())){

                                audio=p.getUrl();
                                titulo=p.getTitulo();
                            }
                        }
                        try{
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                mediaPlayer = null;

                            }
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(audio);
                            snackbar("Reproduciendo "+titulo);
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();


                                }
                            });
                            mediaPlayer.prepare();
                        }catch (Exception  e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                return true;
            }
        });
    }

    private void snackbar(String mensaje){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),mensaje , Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
