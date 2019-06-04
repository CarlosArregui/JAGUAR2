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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_vista_user,viewGroup, false);

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
    public void onBindViewHolder(@NonNull final AdaptadorRvUsuarios.ListaUsuariosHolder listaUsuariosHolder, int i) {
        Usuario usuario =lista_usuarios_recy.get(i);
        listaUsuariosHolder.tv_usuario.setText(usuario.getNombre());

        String user= usuario.getNombre();
        final List <Punto>audios_list= new ArrayList<>();

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("puntos");
        Query q=bbdd.orderByChild("creador").equalTo(user);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //saca datos y los catualiza en la vista
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                audios_list.removeAll(audios_list);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Punto punto = snapshot.getValue(Punto.class);
                    audios_list.add(punto);
                }
              listaUsuariosHolder.tv_audios_creados.setText(Integer.toString(audios_list.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listaUsuariosHolder.i = i;
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
        TextView tv_usuario, tv_audios_creados;

        int i;

        Button btn_abrir;
        ConstraintLayout const_lay_user;
        public ListaUsuariosHolder(@NonNull View itemView) {
            super(itemView);
            tv_usuario=itemView.findViewById(R.id.tv_titulo);
            tv_audios_creados=itemView.findViewById(R.id.tv_audios_creados);

            const_lay_user=(ConstraintLayout)itemView.findViewById(R.id.const_lay_user);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            sacarAlertDialog(lista_usuarios_recy.get(this.getPosition()), v );

        }
    }
    public static void sacarAlertDialog(Usuario user, View v)
    {
        // Log.v("clicado", "posciion:"+position);

        // Evento evento=lista_eventos_recy.get(position);

        Log.v("clicado","Clase:"+ v.getClass());
        AlertDialog.Builder constructor= new AlertDialog.Builder(v.getContext());

        LayoutInflater inflador=LayoutInflater.from(v.getContext());
        final View vista=inflador.inflate(R.layout.alert_di_recy_user,null);
        constructor.setView(vista);




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