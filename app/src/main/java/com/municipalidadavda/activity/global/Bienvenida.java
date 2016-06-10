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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        context = this;

        Button leerNoticias = (Button)findViewById(R.id.btn_leer_noticias);
        leerNoticias.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Bienvenida.this,Principal.class));
            }
        });

    }

    @Override
    protected void onResume(){

        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        logueado = prefs.getBoolean(getResources().getString(R.string.PROPERTY_LOGUEADO), false);

        if(!logueado){
            iniciarLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bienvenida, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void iniciarLogin(){

        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
    }
}
