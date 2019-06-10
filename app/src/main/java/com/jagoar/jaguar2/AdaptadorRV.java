package com.jagoar.jaguar2;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
//Comentar esta clase adaptador podria causar cambios en las leyes espacio-temporales de la fisica, asi que no lo hare.
public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ListaPuntosHolder> implements InterfazClickRV {
    @NonNull
    static List<Punto> lista_eventos_recy;
    SharedPref sharedpref;
    MediaPlayer mediaPlayer;
    View v;
    private static InterfazClickRV itemListener;
    public AdaptadorRV(List<Punto> lista_puntos) {
        this.lista_eventos_recy=lista_puntos;
    }


    @Override
    public ListaPuntosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_vista_add,viewGroup, false);
        sharedpref = new SharedPref(v.getContext());
        if(sharedpref.loadNightModeState()==true) {
            v.getContext().setTheme(R.style.darkTtheme);
        }else  v.getContext().setTheme(R.style.AppThemes);

        ListaPuntosHolder puntos = new ListaPuntosHolder(v);
        return puntos;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListaPuntosHolder listaPuntosHolder, int i) {
        final Punto punto =lista_eventos_recy.get(i);
        listaPuntosHolder.tv_titulo_re.setText(punto.getTitulo());
        listaPuntosHolder.tv_fecha.setText(punto.getFecha());
        listaPuntosHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;

                    }
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(punto.getUrl());
                    snackbar("Reproduciendo "+punto.getTitulo());
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
        });
        listaPuntosHolder.i=i;

    }



    @Override
    public int getItemCount() {
        return lista_eventos_recy.size();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }


    public class ListaPuntosHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView tv_titulo_re, tv_fecha;
        ImageButton btnPlay;
        Button btnVolverBorr, btnAceptarBorr;
        int i;
        ConstraintLayout const_lay;
        public ListaPuntosHolder(@NonNull View itemView) {
            super(itemView);
            tv_titulo_re=itemView.findViewById(R.id.tv_titulo);
            tv_fecha=itemView.findViewById(R.id.tv_fecha);
            btnPlay=itemView.findViewById(R.id.btn_play);
            const_lay=(ConstraintLayout)itemView.findViewById(R.id.constraint_lay);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            sacarAlertDialog(lista_eventos_recy.get(this.getPosition()), v );

        }

        @Override
        public boolean onLongClick(View v) {
            sacarAlertDialogBorrar(lista_eventos_recy.get(this.getPosition()), v );

            return true;
        }





        private void sacarAlertDialogBorrar(final Punto punto, View v) {
            final AlertDialog.Builder constructor= new AlertDialog.Builder(v.getContext());

            LayoutInflater inflador=LayoutInflater.from(v.getContext());
            final View vista=inflador.inflate(R.layout.alert_di_recy_borrar,null);
            constructor.setView(vista);

            btnVolverBorr=vista.findViewById(R.id.btn_borr_volv);
            btnAceptarBorr=vista.findViewById(R.id.btn_borr_add);
            final AlertDialog alert=constructor.create();
            btnAceptarBorr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageReference mSorage= FirebaseStorage.getInstance().getReference();
                    final StorageReference filepath=mSorage.child("Audio").child(punto.getId()+".3gp");
                    filepath.delete();
                    DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos").child(punto.getId());
                    bbdd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                dataSnapshot.getRef().removeValue();
                                notifyItemRemoved(lista_eventos_recy.indexOf(punto));
                                lista_eventos_recy.remove(punto);
                                alert.cancel();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
           btnVolverBorr.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   alert.cancel();
               }
           });


            alert.show();

        }
    }
    public static void sacarAlertDialog(Punto punto, View v)
    {
        AlertDialog.Builder constructor= new AlertDialog.Builder(v.getContext());

        LayoutInflater inflador=LayoutInflater.from(v.getContext());
        final View vista=inflador.inflate(R.layout.alert_di_recy,null);
        SharedPref sharedpref= new SharedPref(vista.getContext());
        if(sharedpref.loadNightModeState()==true) {
            vista.getContext().setTheme(R.style.darkTtheme);
        }else  vista.getContext().setTheme(R.style.AppThemes);
        constructor.setView(vista);

        TextView tv_titulo= vista.findViewById(R.id.tv_titulo);
        Button btnPlay= vista.findViewById(R.id.volvInfo);
        TextView tv_fecha_hora= vista.findViewById(R.id.tv_fecha);
        TextView tv_autor= vista.findViewById(R.id.tv_autor);
        final AlertDialog alert=constructor.create();
        tv_titulo.setText(punto.getTitulo());
        tv_autor.setText(punto.getCountryName());

        tv_fecha_hora.setText(punto.getFecha());
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
    }
    private void snackbar(String audio){
        Snackbar snackbar = Snackbar.make(v, audio, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}