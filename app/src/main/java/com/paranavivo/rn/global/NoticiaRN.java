package com.paranavivo.rn.global;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.paranavivo.modelo.global.Noticia;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio on 05/06/2016.
 */
public class NoticiaRN {

    private Context context;
    private HttpURLConnection con;

    public NoticiaRN(Context context) {
        this.context = context;
    }

    public List<Noticia> leerFlujoJson(InputStream in) {
        // Nueva instancia de la clase Gson
        Gson gson = new Gson();
        List<Noticia> noticias = new ArrayList<>();

        try{
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            // Iniciar el array
            reader.beginArray();

            while (reader.hasNext()) {
                // Lectura de objetos
                Noticia noticia = gson.fromJson(reader, Noticia.class);
                noticias.add(noticia);

                System.err.println("Noticia " + noticia.getTitulo());
            }

            reader.endArray();
            reader.close();

        }catch (Exception e){
            System.err.println("Error "+e );
        }

        return noticias;
    }

}
