package com.municipalidadavda.rn.notificaciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.municipalidadavda.R;
import com.municipalidadavda.modelo.notificaciones.UsuarioPush;

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



/**
 * Created by Claudio on 19/05/2016.
 */
public class NotificacionesRN {

    private static final String URL_REGISTRO = "usuario-push/registrar";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
    private TelephonyManager manager;
    SharedPreferences prefs;
    private GoogleCloudMessaging gcm;
    private Context context;
    private UsuarioPush usuarioPush;
    int versionRegistrada;
    long expirationTime;

    public NotificacionesRN(Context context) {

        this.context = context;
        cargarPreferencias();
    }

    private void cargarPreferencias(){

        prefs = context.getSharedPreferences("MunicipalidadAvellaneda", Context.MODE_PRIVATE);
        versionRegistrada = prefs.getInt(context.getResources().getString(R.string.PROPERTY_APP_VERSION), Integer.MIN_VALUE);
        expirationTime   = prefs.getLong(context.getResources().getString(R.string.PROPERTY_TIEMPO_CADUCIDAD), -1);
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
        }

    }

    private class TareaRegistroGCM extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";

            try
            {
                if (gcm == null) gcm = GoogleCloudMessaging.getInstance(context);


                manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                //usuarioPush.setImei(manager.getDeviceId());

                //Nos registramos en los servidores de GCM
                usuarioPush.setIdRegistro(gcm.register(context.getResources().getString(R.string.PROJECT_ID)));

                Log.d("TareaRegistroGCM", "Registrado en GCM: registration_id=" + usuarioPush.getIdRegistro());

                //Nos registramos en nuestro servidor
                boolean registrado = registroServidor();

                //Guardamos los datos del registro
                if(registrado)
                {
                    setRegistrationId();
                }
            }
            catch (IOException ex){
                Log.d("TareaRegistroGCM", "Error registro en GCM:" + ex.getMessage());
            }
            return msg;
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

}
