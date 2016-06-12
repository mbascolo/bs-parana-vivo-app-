package com.municipalidadavda.activity.seguridad;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.municipalidadavda.R;
import com.municipalidadavda.modelo.notificaciones.UsuarioPush;
import com.municipalidadavda.rn.notificaciones.NotificacionesRN;
import com.municipalidadavda.utils.ActivityBase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Login extends ActivityBase implements View.OnClickListener {

    private static final String URL_REGISTRO = "usuario-push/registrar";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    private TelephonyManager manager;
    private EditText nombre;
    private EditText apellido;
    Spinner dynamicSpinner;
    private TextView resultado;
    private ProgressDialog pd;
    private NotificacionesRN notificacionesRN;
    SharedPreferences prefs;
    private GoogleCloudMessaging gcm;
    private Context context;
    private UsuarioPush usuarioPush;
    int versionRegistrada;
    long expirationTime;

    private boolean logueado;

    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        notificacionesRN = new NotificacionesRN(this);

        context = this;
        cargarPreferencias();



        btnIngresar = (Button) findViewById(R.id.btn_ingresar);
        btnIngresar.setOnClickListener(this);

        dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

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

        nombre = (EditText) findViewById(R.id.logNombre);
        apellido = (EditText) findViewById(R.id.logApellido);

        nombre.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event)
            {
                boolean action = false;
                if (actionId == EditorInfo.IME_ACTION_DONE ||   actionId == EditorInfo.IME_ACTION_NEXT) {

                    apellido.requestFocus();
                    action = true;
                }
                return action;
            }
        });

        apellido.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event)
            {
                boolean action = false;
                if (actionId == EditorInfo.IME_ACTION_DONE ||   actionId == EditorInfo.IME_ACTION_NEXT) {

                    // hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    dynamicSpinner.requestFocus();
                    action = true;
                }
                return action;
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

        resultado = (TextView) findViewById(R.id.logResultado);

        usuarioPush = new UsuarioPush();
        usuarioPush.setNombre(nombre.getText().toString());
        usuarioPush.setApellido(apellido.getText().toString());
        usuarioPush.setCodRubro01("01");
        usuarioPush.setNrocta(getResources().getString(R.string.NROCTA_SERVER_PUSH));

        verificarRegistroGCM(usuarioPush);
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

    public void verificarRegistroGCM(UsuarioPush u){

        usuarioPush = u;

        Log.d("verificarRegistroGCM", "verificarRegistroGCM");

        gcm = GoogleCloudMessaging.getInstance(context);

        //Obtenemos el Registration ID guardado
        usuarioPush.setIdRegistro(getRegistrationId());

        //Si no disponemos de Registration ID comenzamos el registro
        if (usuarioPush.getIdRegistro().equals("")){

            TareaRegistroGCM tarea = new TareaRegistroGCM();
            tarea.execute();
        }else{
            finish();
        }

    }

    private class TareaRegistroGCM extends AsyncTask<String,Integer,Void> {

        String respuesta="";
        boolean registrado;

        @Override
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

        @Override
        protected Void doInBackground(String... params)
        {

            try
            {
                if (gcm == null) gcm = GoogleCloudMessaging.getInstance(context);

                manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                //Nos registramos en los servidores de GCM
                usuarioPush.setIdRegistro(gcm.register(context.getResources().getString(R.string.PROJECT_ID)));

                Log.d("TareaRegistroGCM", "Registrado en GCM: registration_id=" + usuarioPush.getIdRegistro());

                //Nos registramos en nuestro servidor
                registrado = registroServidor();

                //Guardamos los datos del registro
                if(registrado)
                {
                    setRegistrationId();
                }else{
                    Toast.makeText(context, "No es posible registrarse en este momento", Toast.LENGTH_LONG).show();
                }
            }
            catch (IOException ex){
                Log.d("TareaRegistroGCM", "Error registro en GCM:" + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //close dialog
            if(pd!=null)
                pd.dismiss();

            logueado  = prefs.getBoolean(context.getResources().getString(R.string.PROPERTY_LOGUEADO),false);

            if(logueado){
                finish();
            }
        }
    }

    private String getRegistrationId(){

        String registrationId = prefs.getString(context.getResources().getString(R.string.PROPERTY_ID_REGISTRO_GCM), "");
        String nombreGCM = prefs.getString(context.getResources().getString(R.string.PROPERTY_NOMBRE), "");
        String apellidoGCM = prefs.getString(context.getResources().getString(R.string.PROPERTY_APELLIDO), "");

        if (registrationId.length() == 0){
            //Log.d("getRegistrationId", "Registro GCM no encontrado.");
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String expirationDate = sdf.format(new Date(expirationTime));

        Log.d("getRegistrationId", "Registro GCM encontrado (usuarioGCM=" + nombreGCM +", version=" + versionRegistrada + ", expira=" + expirationDate + ")");
        Log.d("getRegistrationId", "Registro GCM encontrado (redId=" + registrationId );

        int currentVersion = getAppVersion(context);

        if (versionRegistrada != currentVersion)
        {
            Log.d("getRegistrationId", "Nueva versión de la aplicación.");
            return "";
        }
        else if (System.currentTimeMillis() > expirationTime)
        {
            Log.d("getRegistrationId", "Registro GCM expirado.");
            return "";
        }
        else if (!nombreGCM.equals(usuarioPush.getNombre()) || !apellidoGCM.equals(usuarioPush.getApellido()))
        {
            Log.d("getRegistrationId", "Nuevo nombre de usuarioGCM.");
            return "";
        }

        return registrationId;
    }



    private void setRegistrationId(){

        SharedPreferences prefs = context.getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getResources().getString(R.string.PROPERTY_NOMBRE), usuarioPush.getNombre());
        editor.putString(context.getResources().getString(R.string.PROPERTY_APELLIDO), usuarioPush.getApellido());
        editor.putString(context.getResources().getString(R.string.PROPERTY_ID_REGISTRO_GCM), usuarioPush.getIdRegistro());
        editor.putInt(context.getResources().getString(R.string.PROPERTY_APP_VERSION), appVersion);
        editor.putLong(context.getResources().getString(R.string.PROPERTY_TIEMPO_CADUCIDAD), System.currentTimeMillis() + EXPIRATION_TIME_MS);
        editor.putBoolean(context.getResources().getString(R.string.PROPERTY_LOGUEADO), true);
        editor.commit();

    }

    private boolean registroServidor(){

        boolean resul = true;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(context.getResources().getString(R.string.URL_REST_SERVER)+URL_REGISTRO);
        httpPut.setHeader("content-type", "application/json");

        try
        {
            cargarPreferencias();

            JSONObject registro = new JSONObject();
            registro.put("nombre", usuarioPush.getNombre());
            registro.put("apellido"   ,usuarioPush.getApellido());
            registro.put("nrocta", usuarioPush.getNrocta());
            registro.put("imei", usuarioPush.getImei());
            registro.put("idRegistro", usuarioPush.getIdRegistro());
            registro.put("codRubro01", usuarioPush.getCodRubro01());

            StringEntity entity = new StringEntity(registro.toString());
            httpPut.setEntity(entity);

            HttpResponse resp = httpClient.execute(httpPut);
            String respStr = EntityUtils.toString(resp.getEntity());

            if(respStr.equals("true")){
                Log.d("registroServidor", "Registro exitoso");
            }else {
                Log.d("registroServidor", "Fallo registro");
                resul = false;
            }
        }
        catch(Exception ex){

            Log.d("registroServidor", "Error registro en servidor: " + ex.getCause() + " || " + ex.getMessage());
            resul = false;
        }

        return resul;
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Error al obtener versión: " + e);
        }
    }

    private void cargarPreferencias(){

        prefs = context.getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        versionRegistrada = prefs.getInt(context.getResources().getString(R.string.PROPERTY_APP_VERSION), Integer.MIN_VALUE);
        expirationTime   = prefs.getLong(context.getResources().getString(R.string.PROPERTY_TIEMPO_CADUCIDAD), -1);
    }


}
