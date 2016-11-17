package com.paranavivo.activity.global;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.paranavivo.modelo.global.Noticia;
import com.paranavivo.R;
import com.paranavivo.rn.global.NoticiaRN;
import com.paranavivo.rn.notificaciones.NotificacionesRN;
import com.paranavivo.utils.ActivityBase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.text.Html.ImageGetter;


public class Principal extends ActivityBase implements View.OnClickListener {

    private ListView lista;
    private ArrayAdapter adaptador;

    private NoticiaRN noticiasRN;
    private NotificacionesRN notificacionesRN;
    private boolean logueado;
    private String usuario;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        context = this;
        noticiasRN = new NoticiaRN(this);
        notificacionesRN = new NotificacionesRN(this);

        lista = (ListView) findViewById(R.id.listaAnimales);

        if(isNetworkConnected()){


            CargarNoticiasTask cn = new CargarNoticiasTask();
            cn.execute();

        }else{
            Toast.makeText(this,"No tienes conexión a internet",Toast.LENGTH_LONG).show();
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.paranavivo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.paranavivo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    protected void onResume(){

        super.onResume();


    }

    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent intent = new Intent(context, Bienvenida.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public class CargarNoticiasTask extends AsyncTask<Void, Void, List<Noticia>> {

        private HttpURLConnection con;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Actualizando noticias");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected List<Noticia> doInBackground(Void... arg0) {
            List<Noticia> noticias = null;

            try {

                URL url = new URL(getResources().getString(R.string.URL_REST_SERVER)+"appnoti/lista");

                // Establecer la conexión
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if (statusCode != 200) {
                    noticias = new ArrayList<>();
                    noticias.add(new Noticia("Error", "Noticias Surubi", "En este momento no tenemos noticias disponibles", ""));

                } else {

                    System.err.println("PASA POR ACAAAA");

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    noticias = noticiasRN.leerFlujoJson(in);

                    for(Noticia item: noticias){

                        Spanned s = Html.fromHtml(item.getDetalle(),getImageHTML(),null);
                        item.setSpanned(s);
                    }
                }

            } catch (Exception e) {
                Log.d("Error parseando noticias noticias",e.getMessage());
                System.err.println("Error "+e);

            } finally {
                con.disconnect();
            }
            return noticias;
        }

        @Override
        protected void onPostExecute(final List<Noticia> noticias) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if (noticias != null) {
                adaptador = new AdaptadorNoticia(context, noticias);
                lista.setAdapter(adaptador);

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                        Intent intent = new Intent(context, NoticiaSeleccionada.class);
                        intent.putExtra("NOT_TITULO", noticias.get(position).getTitulo());
                        intent.putExtra("NOT_DESCRIPCION", noticias.get(position).getDescripcion());
                        intent.putExtra("NOT_DETALLE", noticias.get(position).getDetalle());

                        startActivity(intent);
                    }
                });

            } else {
                Toast.makeText(
                        context,
                        "No es posible mostrar las noticias en estos momentos",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            if(pd!=null) pd.dismiss();
        }

    }

    public class AdaptadorNoticia extends ArrayAdapter<Noticia> {

        public AdaptadorNoticia(Context context, List<Noticia> objects) {

            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            //Obteniendo una instancia del inflater
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Salvando la referencia del View de la fila
            View v = convertView;

            //Comprobando si el View no existe
            if (null == convertView) {
                //Si no existe, entonces inflarlo
                v = inflater.inflate(
                        R.layout.principal_lista,
                        parent,
                        false);
            }

            //Obteniendo instancias de los elementos
            TextView txtTitulo = (TextView)v.findViewById(R.id.txtTitulo);
            TextView txtSubtitulo = (TextView)v.findViewById(R.id.txtSubtitulo);


            //Obteniendo instancia de la Tarea en la posición actual
            Noticia item = getItem(position);

            System.err.println(item.getDescripcion());

            //Id.setText(item.getId());
            txtTitulo.setText(item.getTitulo());
            txtSubtitulo.setText(item.getDescripcion());

            return v;

        }
    }

    public ImageGetter getImageHTML(){
        ImageGetter ig = new ImageGetter(){
            public Drawable getDrawable(String source) {
                try{

                    Drawable d = Drawable.createFromStream(new URL(source).openStream(), "src name");
                    d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
                    return d;
                }catch(IOException e){
                    Log.d("IOException",e.getMessage());
                    return null;
                }
            }
        };
        return ig;
    }

    private void cargarContenidoHTML(final Noticia item) {

        Thread networkThread = new Thread() {
            @Override
            public void run() {

                try {

                    Spanned s = Html.fromHtml(item.getDetalle(),getImageHTML(),null);
                    item.setSpanned(s);

                } catch (Exception e) {

                    //System.err.println("Error cargando imagenes: " + e);
                }
            }
        };
        networkThread.start();
    }


}
