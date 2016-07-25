package com.municipalidadavda.rn.global;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.municipalidadavda.R;
import com.municipalidadavda.modelo.global.Noticia;

import java.io.IOException;
import java.net.URL;
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


        //Obteniendo instancia de la Tarea en la posición actual
        Noticia item = getItem(position);

        Id.setText(item.getID());
        postTitle.setText(item.getPost_title());

        Log.d("HTML",item.getPost_content());

        Spanned s = Html.fromHtml(item.getPost_content(),getImageHTML(),null);

        postContent.setText(s);

        return v;

    }

    public Html.ImageGetter getImageHTML(){
        Html.ImageGetter ig = new Html.ImageGetter(){
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


}