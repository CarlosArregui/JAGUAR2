package com.jagoar.jaguar2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Activity de inicio de sesión.
 */

public class ActivityLogin extends AppCompatActivity {

    //Atributos de la clase
    private CheckBox checkBoxRememberMe;
    private Context context;
    private Button sign_up,log_in;
    private EditText emailLog;
    private EditText passLog;
    private TextView passforget;
    private LinearLayout layoutSnack;
    private FirebaseAuth mAuth;
    private  String idCliente;
    private static final int RC_SIGN_IN = 9001;
    private SharedPreferences mPrefs;
    private static final String prefs_name="PrefsFile";
    /*
     * En el onCreate inicializamos los atributos, añadimos los listener de los botones y otras cosas
     * de firebase.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        context=this;
        layoutSnack =(LinearLayout)findViewById(R.id.layout_login);
        emailLog=(EditText)findViewById(R.id.etLog_email);
        passLog=(EditText)findViewById(R.id.etLog_pass);
        sign_up =(Button)findViewById(R.id.sign_up);
        log_in =(Button)findViewById(R.id.log_in);
        passforget=(TextView)findViewById(R.id.tvPassForget);
        checkBoxRememberMe=(CheckBox)findViewById(R.id.cbRemember);

        //Este botón esta destinado al inicio de sesión de google



        /*
         * mAuth es el atributo de Firebase destinado a la autentificación.
         * idCliente es el id que tiene el proyecto destinado a la autentificación con google. (No confundir con las demás IDs)
         */

        mAuth = FirebaseAuth.getInstance();
        idCliente="309577478544-ap1gfufh3aqa6k7m7lb1sl5lcdce531q.apps.googleusercontent.com";

        final Intent I = new Intent(this,Registro.class);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(I);
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogearUsuario();
            }
        });
        passforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    alertDialog();            }
        });

        /*
         * Estas lineas obtienen un token del idCliente y el correo de tu cuenta de google.
         */



        /*
         * Se inicializa el objeto de SharedPreferences y se pasan por parametros prefs_name que indica el nombre de la colección
          * MODE_PRIVATE donde indicamos que solo nuestra aplicacion tiene acceso a nuestras preferencias

         */

        mPrefs=getSharedPreferences(prefs_name,MODE_PRIVATE);
        getPreferencesData();

    }
    /*
     * En este método se modifica la coleccion con getters y setters añadiendolos con las keys a los EditText
     */

    private void getPreferencesData() {
        SharedPreferences sharedPreferences=getSharedPreferences(prefs_name,MODE_PRIVATE);
        if (sharedPreferences.contains("pref_name")){
            String u=sharedPreferences.getString("pref_name", "not found");
            emailLog.setText(u);
        }
        if (sharedPreferences.contains("pref_pass")){
            String p=sharedPreferences.getString("pref_pass", "not found");
            passLog.setText(p);
        }
        if (sharedPreferences.contains("checkbox")){
            Boolean b=sharedPreferences.getBoolean("checkbox", false);
            checkBoxRememberMe.setChecked(b);
        }
    }


    /**
     * El método de LogearUsuario() obtiene los Strings ingresados en el campo de email y pass para poder realizar el logeo.
     */
    private void LogearUsuario() {

        // El .trim es para eliminar espacios al principio y al final de la palabra

        String correo=emailLog.getText().toString().trim();
        String contra=passLog.getText().toString().trim();


        /*
         * Si el campo de correo o de contraseña están vacios llaman al método snackbar() que crea un
         * snackbar para avisar al usuario.
         */

        if(TextUtils.isEmpty(correo)){
            snackbar("Ingresa un correo");
            return;
        }
        if(TextUtils.isEmpty(contra)){
            snackbar("Ingresa una contraseña");
            return;
        }
        /*
         *Esta condición se cumple si el checkbox es true, al ser true se crea un objeto de SharedPreferences editor
         * para editar en este caso crear tres keys, el nombre, la contraseña y el checkbox cogiendo los datos
         * de los Edit Text y el Checkbox complementando y completando la coleccion de mPrefs, en el caso de que
         * el checkbox no sea true, se limpia mPrefs.
         */
        if (checkBoxRememberMe.isChecked()){
            Boolean boolIsCheked=checkBoxRememberMe.isChecked();
            SharedPreferences.Editor editor=mPrefs.edit();
            editor.putString("pref_name", correo);
            editor.putString("pref_pass", contra);
            editor.putBoolean("checkbox", boolIsCheked);
            editor.apply();

        }else{
            mPrefs.edit().clear().apply();
        }
        /*
         * mAuth llama al metodo signInWithEmailAndPassword() donde se pasan como parametros el correo
         * y la contraseña. Una vez hecho se comprueban los datos que estén en Firebase. Se crea un objeto tipo
         * FirebaseUser que coge el actual usuario que esta intentando entrar, si el correo enviado está verificado
         * se finaliza con exito el log y se pasa al siguiente activity.
         */

        mAuth.signInWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try{
                            FirebaseUser user=mAuth.getCurrentUser();
                            if (!user.isEmailVerified()) {
                                snackbar("No has verificado el correo");
                            }else{
                                if (task.isSuccessful()) {
                                    Intent I = new Intent(context,MainActivity.class);
                                    startActivity(I);
                                    snackbar("Logeado");


                                } else{
                                    snackbar("No has ingresado los datos correctamente.");
                                }
                            }
                        }catch (Exception e){
                            snackbar("Me huele a mi que la cuenta no existe");
                        }



                    }
                });
        emailLog.getText().clear();
        passLog.getText().clear();
    }
    /*
     * En este metodo se crea un Intent y llama al predefinido de google para visualizar tus cuentas y lo inicializa
     */










    private void snackbar(String message){
//        final Snackbar snackbar = Snackbar
//                .make(layoutSnack, message, Snackbar.LENGTH_LONG);
//        View snackView=snackbar.getView();
//
//        snackbar.show();
    }

    private void alertDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        LayoutInflater inflater=this.getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.alert_dialog_passforget, null);
        dialog.setView(dialogView);
        final EditText editText_passForget=(EditText)dialogView.findViewById(R.id.et_insertemail);

        final Button boton_enviarforget=(Button)dialogView.findViewById(R.id.btn_pass);
        boton_enviarforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringEmailForget=editText_passForget.getText().toString().trim();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(stringEmailForget)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // do something when mail was sent successfully.
                                } else {
                                    // ...
                                }
                            }
                        });

            }
        });

        AlertDialog b=dialog.create();
        b.show();
    }

}
