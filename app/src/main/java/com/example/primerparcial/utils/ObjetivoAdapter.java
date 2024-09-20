package com.example.primerparcial.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.primerparcial.negocio.NObjetivo;

import java.util.List;

public class ObjetivoAdapter extends ArrayAdapter<NObjetivo> {

    public ObjetivoAdapter(Context context, List<NObjetivo> listaObjetivos) {
        super(context, 0, listaObjetivos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NObjetivo objetivo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(objetivo.getdObjetivo().getNombre());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Obtener el objeto ejercicio en la posición actual
        NObjetivo objetivo = getItem(position);

        // Verificar si la vista está siendo reutilizada, si no, inflar la vista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        // Referenciar el TextView dentro del layout para mostrar solo el nombre
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(objetivo.getdObjetivo().getNombre());

        return convertView;
    }
}
