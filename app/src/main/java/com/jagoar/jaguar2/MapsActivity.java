package com.jagoar.jaguar2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    private Marker punto;
    LocationManager locationManager;
    Dialog customDialog = null;
    ImageButton imagen;
    EditText et_titulo,et_fecha,et_hora,et_descripcion;
    Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        contexto=this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        miUbicacion();
        miEvento();

    }

    private void miEvento() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (latLng != null){
                    punto = mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                    sacarAlertDialog(latLng.toString());
                }
            }

        });
    }

    private void sacarAlertDialog(final String latLng) {
        //metodo para llamar a nuestro alert dialog, crear puntos y subirlos a firebase.

        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Inserccion");
        constructor.setMessage("Insertar el nuevo contacto");
        LayoutInflater inflador=LayoutInflater.from(this);
        final View vista=inflador.inflate(R.layout.add_punto,null);
        constructor.setView(vista);


        constructor.setPositiveButton("Insertar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //creamos nuestro objeto punto y le asignamos su clave
                EditText et_nombre=vista.findViewById(R.id.et_titulo);
                DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos");
                String id=bbdd.push().getKey();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String fecha = df.format(c);
                String titulo=et_nombre.getText().toString();
                String creador="pepe";
                String coord=punto.getPosition().toString().replace("lat/lng: (","").replace(")","");
                Punto p=new Punto(id,titulo,creador,fecha,coord);

                //insertamos nuestro objeto
                bbdd.child(id).setValue(p);

                //cambiamos de activity y cerramos este

                Intent I = new Intent(contexto,MainActivity.class);
                startActivity(I);
                finish();




            }
        });
        constructor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ALERT", "has clicado cancelar");
                punto.remove();

            }
        });
        AlertDialog alert = constructor.create();
        alert.show();

    }








    private void agregegarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) {
            marcador.remove();
            marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Mi ubicacion")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.animateCamera(miUbicacion);
        } else {
            marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Mi ubicacion")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.animateCamera(miUbicacion);
        }
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            //location.getLatitude()
            //location.getLongitude()
            lat = location.getLatitude();//40.4165000;
            lng =  location.getLongitude();//-3.7025600;
            agregegarMarcador(lat,lng);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location!=null) {
                actualizarUbicacion(location);
                Log.v("ubicacion", location.getLatitude()+", "+location.getLongitude());
                locationManager.removeUpdates(this);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null) {
            actualizarUbicacion(location);
            Log.v("ubicacion", location.getLatitude()+", "+location.getLongitude());
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }*/
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }


}
