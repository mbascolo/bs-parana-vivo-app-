package com.municipalidadavda.rn.global;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.municipalidadavda.modelo.global.Noticia;

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

    public List<Noticia> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia de la clase Gson
        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Noticia> noticias = new ArrayList<>();

        // Iniciar el array
        reader.beginArray();

        while (reader.hasNext()) {
            // Lectura de objetos
            Noticia noticia = gson.fromJson(reader, Noticia.class);
            noticias.add(noticia);
        }


        reader.endArray();
        reader.close();
        return noticias;
    }

}
