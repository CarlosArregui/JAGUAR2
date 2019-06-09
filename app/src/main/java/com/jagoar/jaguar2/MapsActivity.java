package com.jagoar.jaguar2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private ProgressDialog mProgress;
    private StorageReference mSorage;
    private static final String LOG_TAG="Record_log";
    private Button recordBtn, btnVolver, btnAñadir;
    private EditText et_nombre;
    private TextView recordLabel;
    private String fileName;
    private MediaRecorder  recorder;
    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    LocationManager locationManager;
    Dialog customDialog = null;
    ImageButton imagen;
    EditText et_titulo,et_fecha,et_hora,et_descripcion;
    Context contexto;
    String countryName;
    String current_user;
    String url;
    String current_mail;
    String localizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSorage= FirebaseStorage.getInstance().getReference();
        contexto=this;

        //correo del usuario
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         current_mail = user.getEmail();



         //saca el current user

        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios");
        Query q=bbdd.orderByChild("correo").equalTo(current_mail);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d: dataSnapshot.getChildren()) {

                    current_user =d.getKey();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
        mProgress=new ProgressDialog(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //metodo sacar ubicacion
        miUbicacion();

        miEvento();

    }

    private void miEvento() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                localizacion=latLng.toString();
                if (latLng != null){
                    sacarAlertDialog(localizacion);
                }
            }

        });
    }

    private void sacarAlertDialog(final String latLng) {
        //metodo para llamar a nuestro alert dialog, crear puntos y subirlos a firebase.
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setCancelable(false);
        LayoutInflater inflador=LayoutInflater.from(this);
        final View vista=inflador.inflate(R.layout.add_punto,null);
        constructor.setView(vista);
        recordLabel=(TextView)vista.findViewById(R.id.tvGrabar);
        recordBtn =(Button)vista.findViewById(R.id.btn_grabacion);
        et_nombre = vista.findViewById(R.id.et_titulo);
        recordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    recordLabel.setText("Grabación iniciada");
                    startRecording();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    stopRecording();
                    recordLabel.setText("Grabación finalizada");
                }
                return false;
            }
        });

        btnAñadir =(Button)vista.findViewById(R.id.btn_ins_add);
        btnVolver =(Button)vista.findViewById(R.id.btn_volver_add);

        final AlertDialog alert = constructor.create();
        alert.show();

        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!et_nombre.getText().toString().trim().equals("")){
                        if (recorder!=null) {
                            //creamos nuestro objeto punto y le asignamos su clave

                            DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos");
                            String id = bbdd.push().getKey();


                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String fecha = df.format(c);
                            String titulo = et_nombre.getText().toString();
                            String creador = current_user;
                            String coord = localizacion.replace("lat/lng: (", "").replace(")", "");

                            String latlon[] = coord.split(",");
                            String lat = latlon[0];
                            String lon = latlon[1];
                            Double double_lat = Double.parseDouble(lat);
                            Double double_lon = Double.parseDouble(lon);

                            Geocoder gcd = new Geocoder(contexto, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(double_lat, double_lon, 1);
                                if (addresses.size() > 0) {
                                    countryName = addresses.get(0).getCountryName();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            final Punto p = new Punto(id, titulo, creador, fecha, coord, countryName);
                            uploadAudio(p);
                            alert.cancel();
                        }
                        else{
                            snackbar("Tienes que grabar algun sonido, joven explorador");
                        }
                    }
                    else{
                        snackbar("Ingresa un titulo al punto");
                    }
                }catch (Exception e){
                }

            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });


    }








    private void agregegarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.explorer);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        if (marcador != null) {
            marcador.remove();
            marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Mi ubicacion")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            mMap.animateCamera(miUbicacion);
        } else {
            marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Mi ubicacion")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            snackbar("Oh oh, algo no ha salido del todo bien");
        }
        recorder.start();
    }

    private void stopRecording() {

       try {
           recorder.stop();
           recorder.release();
       }catch (Exception e){
           snackbar("Oh oh, algo no ha salido del todo bien");
           recorder=null;
       }

    }

    private void uploadAudio(final Punto p) {


        final StorageReference filepath=mSorage.child("Audio").child(p.getId()+".3gp");
        Uri uri= Uri.fromFile(new File(fileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos");
                        p.setUrl(uri.toString());
                        bbdd.child(p.getId()).setValue(p);
                        Intent i = new Intent(contexto,Main2Activity.class);
                        startActivity(i);
                        finish();

                    }
                });
            }
        });

    }
    private void snackbar(String mensaje){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),mensaje , Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
