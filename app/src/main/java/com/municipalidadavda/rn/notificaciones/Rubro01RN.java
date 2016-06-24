package com.municipalidadavda.rn.notificaciones;

import android.content.Context;

import com.municipalidadavda.R;
import com.municipalidadavda.modelo.notificaciones.Rubro01;

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

        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"01","Bo Aire y Sol"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"02","América"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"03","Belgrano Este"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"04","Belgrano Oeste"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"05","Constitución"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"06","Coperación"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"07","Don Pedro"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"08","Itatí"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"09","Libertad"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"10","Lourdes"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"11","Martín Fierro"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"12","Norte"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"13","Nuevo"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"14","Padre Celso María Milanesio"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"15","Port Artur"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"16","San Martín"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"17","Sartor"));

        return  lista;
    }
}
