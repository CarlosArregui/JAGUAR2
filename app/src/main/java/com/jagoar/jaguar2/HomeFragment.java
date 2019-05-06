package com.jagoar.jaguar2;


import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView rv;
    List<Punto> puntos;
    AdaptadorRV adapter;
    Context contexto;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home, container, false);

    }
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        rv = getView().findViewById(R.id.recycler_home);
        rv.setLayoutManager(new LinearLayoutManager(contexto));
        puntos = new ArrayList<>();

        FirebaseDatabase firebase = FirebaseDatabase.getInstance();


        firebase.getReference().child("puntos").addValueEventListener(new ValueEventListener() {
            @Override
            //saca datos y los catualiza en la vista
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                puntos.removeAll(puntos);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Punto punto = snapshot.getValue(Punto.class);
                    puntos.add(punto);
                }
                adapter = new AdaptadorRV(puntos);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}