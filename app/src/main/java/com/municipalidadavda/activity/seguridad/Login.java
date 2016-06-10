package com.municipalidadavda.activity.seguridad;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.municipalidadavda.R;
import com.municipalidadavda.modelo.notificaciones.UsuarioPush;
import com.municipalidadavda.rn.notificaciones.NotificacionesRN;
import com.municipalidadavda.utils.ActivityBase;

public class Login extends ActivityBase implements View.OnClickListener {

    private TextView nombre;
    private TextView apellido;
    private TextView resultado;
    private ProgressDialog pd;
    private Activity context;
    private NotificacionesRN notificacionesRN;

    private boolean logueado;

    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        notificacionesRN = new NotificacionesRN(this);

        btnIngresar = (Button) findViewById(R.id.btn_ingresar);
        btnIngresar.setOnClickListener(this);

        Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        String[] items = new String[] {
                "Seleccione su barrio",
                "Bo Aire y Sol",
                "Bo Martín Fierro",
                "Bo Padre Celso María Milanesio",
                "Bo Libertad",
                "Bo Don Pedro",
                "Bo Coperación",
                "Bo América",
                "Bo Sartor",
                "Bo Norte",
                "Bo Nuevo",
                "Bo Belgrano Este",
                "Bo Belgrano Oeste",
                "Bo Constitución",
                "Bo Itatí",
                "Bo Lourdes",
                "Bo Port Artur",
                "Bo San Martín"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.login_spinner_item, items);

        adapter.setDropDownViewResource(R.layout.login_spinner_dropdown_item);
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


    @Override
    protected void onResume(){

        super.onResume();
        Toast.makeText(this, "Ingrese sus datos para iniciar sesión", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {

        nombre = (TextView) findViewById(R.id.logNombre);
        apellido = (TextView) findViewById(R.id.logApellido);
        resultado = (TextView) findViewById(R.id.logResultado);

        UsuarioPush usuarioPush = new UsuarioPush();
        usuarioPush.setNombre(nombre.getText().toString());
        usuarioPush.setApellido(apellido.getText().toString());
        usuarioPush.setCodRubro01("01");
        usuarioPush.setNrocta(getResources().getString(R.string.NROCTA_SERVER_PUSH));

        notificacionesRN.verificarRegistroGCM(usuarioPush);

        SharedPreferences prefs = getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        logueado = prefs.getBoolean(getResources().getString(R.string.PROPERTY_LOGUEADO), false);

        Log.d("logueado", String.valueOf(logueado));

        if(logueado){
            Log.d("Pasa por aca","Login exitoso");
            finish();
        }else{
            Toast.makeText(this, "No es posible registrarse en este momento", Toast.LENGTH_LONG).show();
        }
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
