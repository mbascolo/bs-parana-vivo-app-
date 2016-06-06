package com.municipalidadavda.rn.global;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.municipalidadavda.R;
import com.municipalidadavda.modelo.global.Noticia;

import java.util.List;

/**
 * Creado por Beansoft.
 */
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
        TextView Id = (TextView)v.findViewById(R.id.ID);
        TextView postTitle = (TextView)v.findViewById(R.id.postTitle);
        TextView postContent = (TextView)v.findViewById(R.id.postContent);
        //ImageView imagenAnimal = (ImageView)v.findViewById(R.id.imagenAnimal);


        //Obteniendo instancia de la Tarea en la posición actual
        Noticia item = getItem(position);

        Id.setText(item.getID());
        postTitle.setText(item.getPost_title());
        postContent.setText(Html.fromHtml(item.getPost_content()));

        //imagenAnimal.setImageResource(convertirRutaEnId(item.getPathImagenBannerMiniatura()));

        //Devolver al ListView la fila creada
        return v;

    }

    /**
     * Este método nos permite obtener el Id de un drawable a través
     * de su nombre
     * @param nombre  Nombre del drawable sin la extensión de la imagen
     *
     * @return Retorna un tipo int que representa el Id del recurso
     */
    private int convertirRutaEnId(String nombre){
        Context context = getContext();
        return context.getResources()
                .getIdentifier(nombre, "drawable", context.getPackageName());
    }
}