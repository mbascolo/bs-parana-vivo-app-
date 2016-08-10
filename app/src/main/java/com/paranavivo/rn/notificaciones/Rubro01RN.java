package com.paranavivo.rn.notificaciones;

import android.content.Context;

import com.paranavivo.R;
import com.paranavivo.modelo.notificaciones.Rubro01;

import java.util.ArrayList;
import java.util.List;

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

        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"01","Participante"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"02","Parte de Prensa"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"03","Comisión"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"03","Público"));
        return  lista;
    }
}
