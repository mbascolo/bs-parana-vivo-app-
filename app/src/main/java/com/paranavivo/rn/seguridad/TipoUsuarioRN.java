package com.paranavivo.rn.seguridad;

import android.content.Context;

import com.paranavivo.modelo.seguridad.TipoUsuario;

import java.util.ArrayList;

/**
 * Created by Claudio on 05/06/2016.
 */
public class TipoUsuarioRN {

    private Context context;

    public TipoUsuarioRN(Context context) {
        this.context = context;
    }

    public TipoUsuario getTipoUsuario(int id){
        return null;
    }

    public ArrayList<TipoUsuario> getLista(){

        ArrayList<TipoUsuario> lista = new ArrayList<TipoUsuario>();
        lista.add(new TipoUsuario(2,"Pescador"));
        lista.add(new TipoUsuario(3,"Prensa"));
        lista.add(new TipoUsuario(4,"Comisión directiva"));
        return  lista;
    }
}
