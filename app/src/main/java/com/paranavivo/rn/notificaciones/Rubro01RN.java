package com.paranavivo.rn.notificaciones;

import android.content.Context;

import com.paranavivo.R;
import com.paranavivo.modelo.notificaciones.Rubro01;

import java.util.ArrayList;

/**
 * Created by Claudio on 05/06/2016.
 */
public class Rubro01RN {

    private Context context;

    public Rubro01RN(Context context) {
        this.context = context;
    }

    public Rubro01 getRubro01(String codigo){
        return null;
    }

    public ArrayList<Rubro01> getLista(){

        ArrayList<Rubro01> lista = new ArrayList<Rubro01>();

        lista.add(new Rubro01(2,"Pescador"));
        lista.add(new Rubro01(3,"Prensa"));
        return  lista;
    }
}
