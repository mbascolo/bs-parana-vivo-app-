package com.paranavivo.rn.notificaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paranavivo.R;
import com.paranavivo.modelo.notificaciones.Rubro01;

import java.util.ArrayList;

public class Rubro01Adapter extends ArrayAdapter<Rubro01> {
    public Rubro01Adapter(Context context, ArrayList<Rubro01> users) {
       super(context, 0, users);
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
        Rubro01 rubro01 = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.login_spinner_item, parent, false);
        }

        TextView barrio = (TextView) convertView.findViewById(R.id.logBarrio);
        barrio.setText(rubro01.getDescripcion());
        return convertView;

        /**
        LayoutInflater inflater = getLayoutInflater();
        View mySpinner = inflater.inflate(R.layout.custom_spinner, parent, false);
        TextView main_text = (TextView) mySpinner .findViewById(R.id.text_main_seen);
        main_text.setText(spinnerValues[position]);
        TextView subSpinner = (TextView) mySpinner .findViewById(R.id.sub_text_seen);
        subSpinner.setText(spinnerSubs[position]);
        ImageView left_icon = (ImageView) mySpinner .findViewById(R.id.left_pic);
        left_icon.setImageResource(total_images[position]);

        return mySpinner;
         **/
    }

}