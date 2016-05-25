package com.municipalidadavda.Noticias;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.municipalidadavda.R;

public class DetalleProducto extends AppCompatActivity {


    TextView tituloPool_dp, descPool_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        tituloPool_dp = (TextView)findViewById(R.id.textoTitulo);
        descPool_dp = (TextView)findViewById(R.id.textoDescripcion);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) { //veo si tiene los datos

            String datoTitulo = (String)extras.get("nroVta");
            String datoDescripcion = (String)extras.get("tituloVta");

            tituloPool_dp.setText(datoTitulo);
            descPool_dp.setText(datoDescripcion);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_producto, menu);
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



}
