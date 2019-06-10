package com.jagoar.jaguar2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BuscarFragment extends Fragment implements SearchView.OnQueryTextListener {

    RecyclerView rv;
    List<Usuario> usuarios;
    AdaptadorRvUsuarios adapter;
    Context contexto;
    TextView et_user;
    ImageButton btn_buscar;
    String current_user;
    LinearLayout layoutSnack;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contexto = container.getContext();
        return inflater.inflate(R.layout.fragment_buscar, container, false);

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (getArguments() != null) {
            current_user = getArguments().getString("currentUser");

        }

        rv = getView().findViewById(R.id.rv_usuarios);
        rv.setLayoutManager(new LinearLayoutManager(contexto));
        usuarios = new ArrayList<>();

        et_user = (EditText) getView().findViewById(R.id.et_usuario);
        btn_buscar = getView().findViewById(R.id.btn_buscar);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_buscado = et_user.getText().toString();

                FirebaseDatabase.getInstance().getReference("usuarios").child(user_buscado).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Log.v("mensaje", String.valueOf(dataSnapshot.getChildrenCount()));
                        if (dataSnapshot.getChildrenCount() > 1) {
                            Intent showMap = new Intent(contexto, ShowMapActivity.class);
                            showMap.putExtra("currentUser", user_buscado);
                            startActivity(showMap);

                        } else {
                            snackbar();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        snackbar();

                    }

                });


            }
        });

        DatabaseReference bbdd = FirebaseDatabase.getInstance().getReference("usuarios");
        Query q = bbdd.orderByChild("nombre");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //saca datos y los catualiza en la vista
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarios.removeAll(usuarios);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario user = snapshot.getValue(Usuario.class);
                    usuarios.add(user);
                }
                adapter = new AdaptadorRvUsuarios(usuarios);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void snackbar() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Usuario no encontrado", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
