package com.example.primerparcial.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.primerparcial.negocio.NCliente;

import java.util.List;

public class ClienteAdapter extends ArrayAdapter<NCliente> {
    public ClienteAdapter(Context context, List<NCliente> listaClientes) {
        super(context, 0, listaClientes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NCliente cliente = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        // Concatenar nombre y apellido
        textView.setText(cliente.getdCliente().getNombre() + " " + cliente.getdCliente().getApellido());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        NCliente cliente = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        // Concatenar nombre y apellido también en el dropdown
        textView.setText(cliente.getdCliente().getNombre() + " " + cliente.getdCliente().getApellido());

        return convertView;
    }
}
