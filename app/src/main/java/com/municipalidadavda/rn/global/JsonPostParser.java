package com.municipalidadavda.rn.global;

import android.util.JsonReader;

import com.municipalidadavda.modelo.global.Noticia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Hermosa Programación.
 */
public class JsonPostParser {


    public List<Noticia> leerFlujoJson(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerArrayAnimales(reader);
        } finally {
            reader.close();
        }

    }



    public List<Noticia> leerArrayAnimales(JsonReader reader) throws IOException {
        // Lista temporal
        ArrayList<Noticia> animales = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            animales.add(leerAnimal(reader));
        }
        reader.endArray();
        return animales;
    }

    public Noticia leerAnimal(JsonReader reader) throws IOException {
        // Variables locales
        String id = null;
        String postTitle = null;
        String postContent = null;

        //String pathImagenBannerMiniatura = null;

        // Iniciar objeto
        reader.beginObject();

        /*
        Lectura de cada atributo
         */
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextString();

                    break;
                case "posTitle":
                    postTitle = reader.nextString();
                    break;
                case "pathImagenBannerMiniatura":
                    postContent = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Noticia(id, postTitle, postContent);
    }

}

