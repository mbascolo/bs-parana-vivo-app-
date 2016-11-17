package com.paranavivo.activity.global;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paranavivo.R;

public class NoticiaSeleccionada extends AppCompatActivity   implements View.OnClickListener{

    private Button btnVolver;
    private TextView txtTitulo;
    private TextView txtDealle;
    private TextView txtDescripcion;
    protected ProgressDialog pd;
    protected Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticia_seleccionada);

        context = this;

        String titulo = (String) getIntent().getExtras().getSerializable("NOT_TITULO");
        String descripcion = (String) getIntent().getExtras().getSerializable("NOT_DESCRIPCION");
        String detalle = (String) getIntent().getExtras().getSerializable("NOT_DETALLE");

        txtTitulo = (TextView) findViewById(R.id.txtNotSeleTitulo);
        txtTitulo.setText(titulo);

        txtDescripcion = (TextView) findViewById(R.id.txtNotSeleDescripcion);
        txtDescripcion.setText(descripcion);

        txtDealle = (TextView) findViewById(R.id.txtNotSeleDetalle);
        txtDealle.setText(detalle);

        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnVolver:
                finish();
                break;
        }
    }
}
