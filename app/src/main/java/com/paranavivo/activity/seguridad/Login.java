package com.paranavivo.activity.seguridad;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.paranavivo.R;
import com.paranavivo.activity.global.SinConexion;
import com.paranavivo.modelo.notificaciones.Rubro01;
import com.paranavivo.modelo.notificaciones.UsuarioPush;
import com.paranavivo.rn.notificaciones.NotificacionesRN;
import com.paranavivo.rn.notificaciones.Rubro01Adapter;
import com.paranavivo.rn.notificaciones.Rubro01RN;
import com.paranavivo.utils.ActivityBase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Login extends ActivityBase implements View.OnClickListener {

    private TextView usuario;
    private TextView password;
    private TextView resultado;
    private ProgressDialog pd;
    private Activity context;
    private NotificacionesRN notificacionesRN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        context=this;
        notificacionesRN = new NotificacionesRN(this);
        Button bLogin = (Button) findViewById(R.id.btn_ingresar);

        bLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume(){

        super.onResume();
        Toast.makeText(this, "Ingrese sus datos para iniciar sesión", Toast.LENGTH_SHORT).show();

    }

    private void GuardarPreferencias(){

        SharedPreferences pref = getSharedPreferences(context.getResources().getString(R.string.PROJECT_ID), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(getResources().getString(R.string.PROPERTY_USUARIO),usuario.getText().toString());
        editor.putString(getResources().getString(R.string.PROPERTY_PASSWORD),password.getText().toString());
        editor.putBoolean(getResources().getString(R.string.PROPERTY_LOGUEADO), true);
        editor.commit();
    }

    @Override
    public void onClick(View v) {

        usuario = (TextView) findViewById(R.id.logEmail);
        password = (TextView) findViewById(R.id.logPassword);
        resultado = (TextView) findViewById(R.id.logResultado);
        LoginTask bt=new LoginTask();
        bt.execute(getResources().getString(R.string.URL_REST_SERVER)+"usuario/login/" + usuario.getText() + "/" + password.getText());
    }

    //background process to download the file from internet
    private class LoginTask extends AsyncTask<String,Integer,Void> {

        String respuesta="";

        protected void onPreExecute(){
            super.onPreExecute();

            //display progress dialog
            pd = new ProgressDialog(context);
            pd.setTitle("Verificando acceso");
            pd.setMessage("Aguarde un momento");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();
        }

        protected Void doInBackground(String...params){
            URL url;
            try {
                //create url object to point to the file location on internet
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                //get InputStream instance
                InputStream is=con.getInputStream();
                //create BufferedReader object
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                //read content of the file line by line
                while((line=br.readLine())!=null){
                    respuesta+=line;
                }
                br.close();

            }catch (Exception e) {
                e.printStackTrace();
                //close dialog if error occurs
                if(pd!=null) pd.dismiss();
                Intent intent = new Intent(context, SinConexion.class);
                startActivity(intent);
            }
            return null;
        }

        protected void onPostExecute(Void result){
            //close dialog
            if(pd!=null)
                pd.dismiss();

            if(respuesta!=null && !respuesta.isEmpty()){

                if(respuesta.equals("Autorizado")){
                    GuardarPreferencias();
                    notificacionesRN.verificarRegistroGCM();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), respuesta, Toast.LENGTH_SHORT).show();
                    resultado.setText(respuesta);
                }
            }else{
                Toast.makeText(Login.this,"Ingrese su usuario y clave",Toast.LENGTH_SHORT).show();
                resultado.setText("Ingrese su usuario y clave");
            }
        }
    }
}
