package com.paranavivo.rn.seguridad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paranavivo.R;
import com.paranavivo.modelo.seguridad.TipoUsuario;

import java.util.ArrayList;

public class TipoUsuarioAdapter extends ArrayAdapter<TipoUsuario> {

    public TipoUsuarioAdapter(Context context, ArrayList<TipoUsuario> tipoUsuarios) {
       super(context, 0, tipoUsuarios);
    }

    @Override public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {

        return getCustomView(pos, cnvtView, prnt);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        TipoUsuario tipoUsuario = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.registro_spinner_item, parent, false);
        }

        TextView barrio = (TextView) convertView.findViewById(R.id.itemTipoUsuario);
        barrio.setText(tipoUsuario.getDescripcion());
        return convertView;

    }

}