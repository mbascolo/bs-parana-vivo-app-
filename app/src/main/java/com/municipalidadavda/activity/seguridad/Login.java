package com.municipalidadavda.activity.seguridad;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.municipalidadavda.R;
import com.municipalidadavda.modelo.notificaciones.Rubro01;
import com.municipalidadavda.modelo.notificaciones.UsuarioPush;
import com.municipalidadavda.rn.notificaciones.NotificacionesRN;
import com.municipalidadavda.rn.notificaciones.Rubro01Adapter;
import com.municipalidadavda.rn.notificaciones.Rubro01RN;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Login extends ActivityBase implements View.OnClickListener {

    private static final String URL_REGISTRO = "usuario-push/registrar";
    private static final String URL_RUBRO01 = "rubro01/lista/00001";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    private TelephonyManager manager;
    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private String imei;
    Spinner dynamicSpinner;
    private TextView resultado;
    private ProgressDialog pd;
    private NotificacionesRN notificacionesRN;
    private Rubro01RN rubro01RN;
    SharedPreferences prefs;
    private GoogleCloudMessaging gcm;
    private Context context;
    private UsuarioPush usuarioPush;
    int versionRegistrada;
    long expirationTime;

    private Rubro01[]  rubros;

    private boolean logueado;

    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        notificacionesRN = new NotificacionesRN(this);
        rubro01RN = new Rubro01RN(this);

        context = this;
        cargarPreferencias();

        btnIngresar = (Button) findViewById(R.id.btn_ingresar);
        btnIngresar.setOnClickListener(this);


        // Construimos la fuente de datos
        ArrayList<Rubro01> rubros01 = rubro01RN.getLista();
        // Creamos el adaptador para convertir a las vistas
        Rubro01Adapter adapter = new Rubro01Adapter(this, rubros01);

        dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);
        dynamicSpinner.setAdapter(adapter);

        //adapter.setDropDownViewResource(R.layout.login_spinner_dropdown_item);



        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", ((Rubro01) parent.getItemAtPosition(position)).toString());

                //Rubro01 r = (Rubro01) parent.getItemAtPosition(position);
                //usuarioPush.setCodRubro01(r.getCodigo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        nombre = (EditText) findViewById(R.id.logNombre);
        apellido = (EditText) findViewById(R.id.logApellido);
        email = (EditText) findViewById(R.id.logEmail);

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

                    email.requestFocus();
                    action = true;
                }
                return action;
            }
        });

        email.setOnEditorActionListener(new OnEditorActionListener() {
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

        if(!puedoIngresar()){
            return;
        }

        resultado = (TextView) findViewById(R.id.logResultado);

        usuarioPush = new UsuarioPush();
        usuarioPush.setEmail(email.getText().toString());
        usuarioPush.setNombre(nombre.getText().toString());
        usuarioPush.setApellido(apellido.getText().toString());
        usuarioPush.setCodRubro01(((Rubro01)dynamicSpinner.getSelectedItem()).getCodigo());
        usuarioPush.setNrocta(getResources().getString(R.string.NROCTA_SERVER_PUSH));

        verificarRegistroGCM(usuarioPush);
    }

    private boolean puedoIngresar(){

        if(nombre.getText()==null || nombre.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese su nombre y vuelva a intentar", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(apellido.getText()==null || apellido.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese su apellido y vuelva a intentar", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(email.getText()==null || email.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese su e-mail y vuelva a intentar", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(dynamicSpinner.getSelectedItem()==null){
            Toast.makeText(this, "Seleccine el barrio donde vive", Toast.LENGTH_SHORT).show();
            return false;
        }

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
            pd.setTitle("Registrando");
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
                //imei = manager.getDeviceId();

                //Nos registramos en los servidores de GCM
                usuarioPush.setIdRegistro(gcm.register(context.getResources().getString(R.string.PROJECT_ID)));
                //usuarioPush.setImei(imei);

                Log.d("TareaRegistroGCM", "Registrado en GCM: registration_id=" + usuarioPush.getIdRegistro());

                //Nos registramos en nuestro servidor
                registrado = registroServidor();

                //System.err.println("registro*******************" + registrado);

                //registrado=true;
                //Guardamos los datos del registro
                if(registrado)
                {
                    setRegistrationId();
                }else{
                    Log.d("No es posible registrarse", "");
                    //Toast.makeText(context, "No es posible registrarse en este momento", Toast.LENGTH_LONG).show();
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
            registro.put("email", usuarioPush.getEmail());
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
