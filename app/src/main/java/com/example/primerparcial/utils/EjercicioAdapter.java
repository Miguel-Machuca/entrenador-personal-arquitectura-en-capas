package com.example.primerparcial.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.primerparcial.negocio.NEjercicio;

import java.util.List;

// Adaptador personalizado para el Spinner
public class EjercicioAdapter extends ArrayAdapter<NEjercicio> {

    public EjercicioAdapter(Context context, List<NEjercicio> listaEjercicios) {
        super(context, 0, listaEjercicios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        NEjercicio ejercicio = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(ejercicio.getdEjercicio().getNombre()); // Muestra solo el nombre del ejercicio

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Obtener el objeto ejercicio en la posición actual
        NEjercicio ejercicio = getItem(position);

        // Verificar si la vista está siendo reutilizada, si no, inflar la vista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        // Referenciar el TextView dentro del layout para mostrar solo el nombre
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(ejercicio.getdEjercicio().getNombre());

        return convertView;
    }
}
