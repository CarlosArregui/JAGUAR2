package com.jagoar.jaguar2;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
//Comentar esta clase adaptador podria causar cambios en las leyes espacio-temporales de la fisica, asi que no lo hare.
public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ListaPuntosHolder> implements InterfazClickRV {
    @NonNull
    static List<Punto> lista_puntos_recy;
    Context contexto;
    private static InterfazClickRV itemListener;
    private View.OnClickListener listener;
    Dialog customDialog = null;

    public AdaptadorRV(List<Punto> lista_puntos) {
        this.lista_puntos_recy = lista_puntos;
    }


    @Override
    public ListaPuntosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_vista_add, viewGroup, false);

        // viewGroup.setOnClickListener(this);
        ListaPuntosHolder puntos = new ListaPuntosHolder(v);
        return puntos;
    }

    View.OnClickListener oyente = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    public void onBindViewHolder(@NonNull ListaPuntosHolder listaPuntosHolder, int i) {
        Punto punto = lista_puntos_recy.get(i);
        listaPuntosHolder.tv_titulo.setText(punto.getTitulo());
        listaPuntosHolder.tv_fecha.setText(punto.getFecha());

        listaPuntosHolder.i = i;
        // listaPuntosHolder.const_lay.setOnClickListener(oyente);

    }

    @Override
    public int getItemCount() {
        return lista_puntos_recy.size();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }


    public static class ListaPuntosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_titulo, tv_fecha,tv_hora;
        ImageView imagen;
        int i;

        Button btn_abrir;
        ConstraintLayout const_lay;

        public ListaPuntosHolder(@NonNull View itemView) {
            super(itemView);
            tv_titulo = itemView.findViewById(R.id.tv_titulo);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_hora = itemView.findViewById(R.id.tv_hora);
            imagen = itemView.findViewById(R.id.imagen);

            const_lay = (ConstraintLayout) itemView.findViewById(R.id.constraint_lay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

    public static void sacarAlertDialog(Punto punto, View v) {
    }
}