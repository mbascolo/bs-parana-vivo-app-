package com.municipalidadavda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.municipalidadavda.GCM.MainActivityGcm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);


        String[] items = new String[] {
                "Seleccione su barrio","Bo Aire y Sol","Bo Martín Fierro","Bo Padre Celso María Milanesio","Bo Libertad","Bo Don Pedro",
                "Bo Coperación","Bo América","Bo Sartor","Bo Norte", "Bo Nuevo","Bo Belgrano Este","Bo Belgrano Oeste",
                "Bo Constitución", "Bo Itatí","Bo Lourdes",
                "Bo Port Artur","Bo San Martín"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, items);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dynamicSpinner.setAdapter(adapter);




        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    //Botón ingresar
    public void onIngresar(View v) {
        Intent ingreso = new Intent(LoginActivity.this,MainActivityGcm.class);
        startActivity(ingreso);
    }


   // @Override
   // public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
   //     getMenuInflater().inflate(R.menu.menu_login, menu);
   //     return true;
   // }

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
}
