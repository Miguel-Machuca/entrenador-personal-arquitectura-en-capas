package com.example.primerparcial.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.primerparcial.R;

import java.util.List;

public class ListEjercicioAdapter extends ArrayAdapter<Object[]> {

    public ListEjercicioAdapter(Context context, List<Object[]> objetos) {
        super(context, 0, objetos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] ejercicio = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);
        }

        TextView textViewEjercicio = convertView.findViewById(R.id.et_agregado_ejercicio);
        // Los otros TextViews ya no se necesitan
        TextView textViewNombre = convertView.findViewById(R.id.et_agregado_ejercicio_nombre);
        // TextView textViewRepeticion = convertView.findViewById(R.id.et_cantidad_repeticion);
        // TextView textViewReposo = convertView.findViewById(R.id.et_reposo_entre_serie);

        // Configurar el texto del TextView con los datos del objeto
        if (ejercicio != null) {
            String textoConcatenado = String.format("%d series de %d repeciones y un descanso de %d segundos",
                    ejercicio[1], ejercicio[2], ejercicio[3]);
            textViewNombre.setText(String.valueOf(ejercicio[4]));
            textViewEjercicio.setText(textoConcatenado);
        }

        return convertView;
    }

}
