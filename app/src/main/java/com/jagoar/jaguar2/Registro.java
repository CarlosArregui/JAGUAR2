package com.jagoar.jaguar2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Este activity es el del registro
 */
public class Registro extends AppCompatActivity {
    private LinearLayout layoutSnack;
    private EditText email;
    private EditText pass,et_usuario;
    private EditText passConfirm;
    private Button registro;
    private String correo;
    private String contra;
    private String usuario;
    private Context contexto;
    private Boolean no_repetido=false;
    private FirebaseAuth mAuth;
    private DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Paantalla de registro
        setContentView(R.layout.activity_registro);

        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);
        contexto=this;
        layoutSnack=findViewById(R.id.layout_registro);
        mAuth = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.etEmail);
        pass=(EditText)findViewById(R.id.etPass1);
        registro=(Button)findViewById(R.id.btnRegistrar);
        passConfirm=(EditText)findViewById(R.id.etPassConfirm);
        et_usuario=(EditText)findViewById(R.id.et_usuario);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }
    private void registrarUsuario() {
        //Obtenemos los textos de los editext
        correo = email.getText().toString().trim();
        contra = pass.getText().toString().trim();
        usuario = et_usuario.getText().toString().trim();
        String contraConfirm = passConfirm.getText().toString().trim();
        //Verificacion de que no esten vacias los campos
        if (TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Se debe de ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contra)) {
            Toast.makeText(this, "Se debe de ingresar una contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        //confirmacion de contraseña
        if (!contra.equals(contraConfirm)) {
            snackbar("Las contraseñas no son iguales");
            return;
        }

        //verificacion de que el usuario no esta repetido

            FirebaseDatabase.getInstance().getReference("usuarios").child(usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.v("mensaje",String.valueOf(dataSnapshot.getChildrenCount()));
                    if (dataSnapshot.getChildrenCount()==0){
                        no_repetido=true;
                    }
                    else{
                        snackbar("El usuario ya existe");
                    }






                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                snackbar("Ha habido un error en la base de datos");

            }

        });

        //Crear usuario

        if (no_repetido){
            mAuth.createUserWithEmailAndPassword(correo, contra)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                bbdd = FirebaseDatabase.getInstance().getReference("usuarios");
                              Usuario u = new Usuario();
                                u.setCorreo(correo);
                                u.setNombre(usuario);
                                String clave = usuario;
                                bbdd.child(clave).setValue(u);
                                FirebaseUser user=mAuth.getCurrentUser();
                                user.sendEmailVerification();
                                final Intent loging = new Intent(contexto, ActivityLogin.class);
                                startActivity(loging);
                            }else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    snackbar("El correo ya está en uso");

                                }else{
                                    snackbar("Oops... Algo ha salido mal");
                                }
                            }
                        }
                    });
        }
    }

    private void snackbar(String message){
        final Snackbar snackbar = Snackbar
                .make(layoutSnack, message, Snackbar.LENGTH_LONG);
        View snackView=snackbar.getView();
       // TextView textView=snackView.findViewById(com.google.android.material.R.id.snackbar_text);
      //  textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
