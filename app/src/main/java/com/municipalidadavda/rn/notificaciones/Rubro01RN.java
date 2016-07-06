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

        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"01","Aire y Sol"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"02","América"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"03","Avellaneda Oeste"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"04","Belgrano"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"05","Constitución"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"06","Cooperación"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"07","Don Pedro"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"08","El Carmen"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"09","El Timbó"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"10","Itatí"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"11","Libretad"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"12","Lourdes"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"13","Martín Fierro"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"14","Moussy"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"15","Norte"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"16","Nuevo"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"17","Loteo 64"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"18","Padre Celso"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"19","Port Arthur"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"20","Progresar"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"21","San Martín"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"22","Santa Ana"));
        lista.add(new Rubro01(context.getResources().getString(R.string.NROCTA_SERVER_PUSH),"23","Sartor"));

        return  lista;
    }
}
