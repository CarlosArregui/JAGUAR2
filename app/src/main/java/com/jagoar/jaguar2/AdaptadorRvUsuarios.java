package com.jagoar.jaguar2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdaptadorRvUsuarios extends RecyclerView.Adapter<AdaptadorRvUsuarios.ListaUsuariosHolder> implements InterfazClickRV {
    @NonNull
    static List<Usuario> lista_usuarios_recy;
    Context contexto;
    private static InterfazClickRV itemListener;
    private View.OnClickListener listener;
    public AdaptadorRvUsuarios(List<Usuario> lista_usuarios) {
        this.lista_usuarios_recy=lista_usuarios;
    }


    @Override
    public AdaptadorRvUsuarios.ListaUsuariosHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_vista_add,viewGroup, false);

        // viewGroup.setOnClickListener(this);
        AdaptadorRvUsuarios.ListaUsuariosHolder usuarios = new AdaptadorRvUsuarios.ListaUsuariosHolder(v);
        return usuarios;
    }
    View.OnClickListener oyente=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    public void onBindViewHolder(@NonNull AdaptadorRvUsuarios.ListaUsuariosHolder listaUsuariosHolder, int i) {
        Usuario usuario =lista_usuarios_recy.get(i);

        listaUsuariosHolder.i=i;
        // listaPuntosHolder.const_lay.setOnClickListener(oyente);

    }

    @Override
    public int getItemCount() {
        return lista_usuarios_recy.size();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }


    public static class ListaUsuariosHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_titulo_re, tv_fecha;
        ImageView imagen;
        int i;

        Button btn_abrir;
        ConstraintLayout const_lay;
        public ListaUsuariosHolder(@NonNull View itemView) {
            super(itemView);
            tv_titulo_re=itemView.findViewById(R.id.tv_titulo);
            tv_fecha=itemView.findViewById(R.id.tv_fecha);

            const_lay=(ConstraintLayout)itemView.findViewById(R.id.constraint_lay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sacarAlertDialog(lista_usuarios_recy.get(this.getPosition()), v );

        }
    }
    public static void sacarAlertDialog(Usuario punto, View v)
    {
        // Log.v("clicado", "posciion:"+position);

        // Evento evento=lista_eventos_recy.get(position);

        Log.v("clicado","Clase:"+ v.getClass());
        AlertDialog.Builder constructor= new AlertDialog.Builder(v.getContext());

        LayoutInflater inflador=LayoutInflater.from(v.getContext());
        final View vista=inflador.inflate(R.layout.alert_di_recy,null);
        constructor.setView(vista);

        TextView tv_titulo= vista.findViewById(R.id.tv_titulo);

        TextView tv_fecha_hora= vista.findViewById(R.id.tv_fecha);
        TextView tv_descripcion= vista.findViewById(R.id.tv_autor);




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