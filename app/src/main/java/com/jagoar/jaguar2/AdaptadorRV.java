package com.jagoar.jaguar2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
//Comentar esta clase adaptador podria causar cambios en las leyes espacio-temporales de la fisica, asi que no lo hare.
public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ListaPuntosHolder> implements InterfazClickRV {
    @NonNull
    static List<Punto> lista_eventos_recy;
    SharedPref sharedpref;
    Context contexto;
    private static InterfazClickRV itemListener;
    private View.OnClickListener listener;
    public AdaptadorRV(List<Punto> lista_puntos) {
        this.lista_eventos_recy=lista_puntos;
    }


    @Override
    public ListaPuntosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_vista_add,viewGroup, false);
        sharedpref = new SharedPref(v.getContext());
        if(sharedpref.loadNightModeState()==true) {
            v.getContext().setTheme(R.style.darkTtheme);
        }else  v.getContext().setTheme(R.style.AppThemes);
        // viewGroup.setOnClickListener(this);
        ListaPuntosHolder puntos = new ListaPuntosHolder(v);
        return puntos;
    }
    View.OnClickListener oyente=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    public void onBindViewHolder(@NonNull final ListaPuntosHolder listaPuntosHolder, int i) {
        final Punto punto =lista_eventos_recy.get(i);
        listaPuntosHolder.tv_titulo_re.setText(punto.getTitulo());
        listaPuntosHolder.tv_fecha.setText(punto.getFecha());
        listaPuntosHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer= new MediaPlayer();
                try{
                    mediaPlayer.setDataSource(punto.getUrl());
                    Log.v("uriuri",punto.getUrl());
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (mp.isPlaying()){
                                mp.stop();
                            }
                            mp.start();
                        }
                    });
                    mediaPlayer.prepare();
                }catch (Exception  e){
                    e.printStackTrace();
                    Log.v("uriuri",punto.getUrl());
                }

            }
        });
        listaPuntosHolder.i=i;
        // listaPuntosHolder.const_lay.setOnClickListener(oyente);

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
        ImageView imagen;
        int i;

        Button btn_abrir;
        ConstraintLayout const_lay;
        public ListaPuntosHolder(@NonNull View itemView) {
            super(itemView);
            tv_titulo_re=itemView.findViewById(R.id.tv_titulo);
            tv_fecha=itemView.findViewById(R.id.tv_fecha);
            btnPlay=itemView.findViewById(R.id.btn_play);
            const_lay=(ConstraintLayout)itemView.findViewById(R.id.constraint_lay);
            btnVolverBorr=itemView.findViewById(R.id.btn_borr_volv);
            btnAceptarBorr=itemView.findViewById(R.id.btn_borr_add);
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
            AlertDialog.Builder constructor= new AlertDialog.Builder(v.getContext());

            LayoutInflater inflador=LayoutInflater.from(v.getContext());
            final View vista=inflador.inflate(R.layout.alert_di_recy_borrar,null);
            constructor.setView(vista);

            constructor.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("ALERT","has clicado borrar");
                    DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos").child(punto.getId());
                    bbdd.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                dataSnapshot.getRef().removeValue();
                                notifyItemRemoved(lista_eventos_recy.indexOf(punto));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
            constructor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("ALERT","has clicado cancelar");


                }
            });
            AlertDialog alert=constructor.create();
            alert.show();

        }
    }
    public static void sacarAlertDialog(Punto punto, View v)
    {
        // Log.v("clicado", "posciion:"+position);

        // Evento evento=lista_eventos_recy.get(position);

        Log.v("clicado","Clase:"+ v.getClass());
        AlertDialog.Builder constructor= new AlertDialog.Builder(v.getContext());

        LayoutInflater inflador=LayoutInflater.from(v.getContext());
        final View vista=inflador.inflate(R.layout.alert_di_recy,null);
        SharedPref sharedpref= new SharedPref(vista.getContext());
        if(sharedpref.loadNightModeState()==true) {
            vista.getContext().setTheme(R.style.darkTtheme);
        }else  vista.getContext().setTheme(R.style.AppThemes);
        constructor.setView(vista);

        TextView tv_titulo= vista.findViewById(R.id.tv_titulo);
        Button btnPlay= vista.findViewById(R.id.btn_play);
        TextView tv_fecha_hora= vista.findViewById(R.id.tv_fecha);
        TextView tv_autor= vista.findViewById(R.id.tv_autor);

        tv_titulo.setText(punto.getTitulo());
        tv_autor.setText(punto.getCreador());

        tv_fecha_hora.setText(punto.getFecha());

        constructor.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ALERT","has clicado aceptar");
            }
        });
        constructor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ALERT","has clicado cancelar");

            }
        });
        AlertDialog alert=constructor.create();
        alert.show();
    }
}