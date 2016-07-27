package com.municipalidadavda.activity.global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.municipalidadavda.R;
import com.municipalidadavda.activity.seguridad.Login;
import com.municipalidadavda.utils.ActivityBase;

public class Bienvenida extends ActivityBase {

    private boolean logueado;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        context = this;
        //borrarPreferencias();

        Button leerNoticias = (Button)findViewById(R.id.btn_leer_noticias);
        leerNoticias.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Bienvenida.this,Principal.class));
            }
        });

        Button cambiarPerfil = (Button)findViewById(R.id.btn_perfil);
        cambiarPerfil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Bienvenida.this,Perfil.class));
            }
        });

    }

    @Override
    protected void onResume(){

        super.onResume();

        prefs = getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        logueado = prefs.getBoolean(getResources().getString(R.string.PROPERTY_LOGUEADO), false);

        if(!logueado){
            iniciarLogin();
        }
    }

    private void borrarPreferencias(){

        prefs = context.getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getResources().getString(R.string.PROPERTY_LOGUEADO), false);
        editor.commit();
    }


    private void iniciarLogin(){

        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
    }
}
