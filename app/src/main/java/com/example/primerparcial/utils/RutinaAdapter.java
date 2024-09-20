package com.example.primerparcial.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.primerparcial.negocio.NRutina;

import java.util.List;

public class RutinaAdapter extends ArrayAdapter<NRutina>{

    public RutinaAdapter(Context context, List<NRutina> listaRutinas) {
        super(context, 0, listaRutinas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NRutina rutina = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(rutina.getdRutina().getNombre()); // Muestra solo el nombre del ejercicio

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        NRutina rutina = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(rutina.getdRutina().getNombre());

        return convertView;
    }
}
