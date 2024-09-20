package com.example.primerparcial.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.primerparcial.R;

import java.util.List;

public class ListObjetivoAdapter extends ArrayAdapter<Object[]> {

    public ListObjetivoAdapter(Context context, List<Object[]> objetos) {
        super(context, 0, objetos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] objetivo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }

        TextView textViewObjetivo = convertView.findViewById(R.id.et_agregado_objetivo);
        // Los otros TextViews ya no se necesitan
        TextView textViewNombre = convertView.findViewById(R.id.et_agregado_objetivo_nombre);
        // TextView textViewRepeticion = convertView.findViewById(R.id.et_cantidad_repeticion);
        // TextView textViewReposo = convertView.findViewById(R.id.et_reposo_entre_serie);

        // Configurar el texto del TextView con los datos del objeto
        if (objetivo != null) {
            textViewNombre.setText(String.valueOf(objetivo[2]));
            textViewObjetivo.setText(String.valueOf(objetivo[1]));
        }

        return convertView;
    }

}