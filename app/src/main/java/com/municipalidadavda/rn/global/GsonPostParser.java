package com.municipalidadavda.rn.global;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.municipalidadavda.modelo.global.Noticia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Hermosa Programación.
 */
public class GsonPostParser {


    public List<Noticia> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia de la clase Gson
        Gson gson = new Gson();

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Noticia> animales = new ArrayList<>();

        // Iniciar el array
        reader.beginArray();

        while (reader.hasNext()) {
            // Lectura de objetos
            Noticia noticia = gson.fromJson(reader, Noticia.class);
            animales.add(noticia);
        }


        reader.endArray();
        reader.close();
        return animales;
    }
}
