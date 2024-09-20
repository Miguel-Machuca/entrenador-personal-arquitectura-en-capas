package com.example.primerparcial;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.primerparcial.dato.AsistenteBD;
import com.example.primerparcial.presentacion.PCliente;
import com.example.primerparcial.presentacion.PCronograma;
import com.example.primerparcial.presentacion.PEjercicio;
import com.example.primerparcial.presentacion.PObjetivo;
import com.example.primerparcial.presentacion.PRutina;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    Button btnGestionarEjercicio, btnGestionarObjetivo, btnGestionarCliente, btnGestionarRutina, btnGestionarCronograma;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsistenteBD admin = new AsistenteBD(this);
        SQLiteDatabase db = admin.getWritableDatabase();
        init();
    }

    public void init() {
        context = getApplicationContext();
        btnGestionarEjercicio = findViewById(R.id.btn_gestionar_ejercicio);
        btnGestionarObjetivo = findViewById(R.id.btn_gestionar_objetivo);
        btnGestionarCliente = findViewById(R.id.btn_gestionar_cliente);
        btnGestionarRutina = findViewById(R.id.btn_gestionar_rutina);
        btnGestionarCronograma = findViewById(R.id.btn_gestionar_cronograma);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_gestionar_ejercicio) {
            Toast.makeText(context, "Gestionar Ejercicio", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, PEjercicio.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_objetivo) {
            Toast.makeText(context, "Gestionar Objetivo", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, PObjetivo.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_cliente) {
            Toast.makeText(context, "Gestionar Cliente", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, PCliente.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_rutina) {
            Toast.makeText(context, "Gestionar Rutina", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, PRutina.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        } else if (id == R.id.btn_gestionar_cronograma) {
            Toast.makeText(context, "Gestionar Cronograma", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, PCronograma.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("id", 0);
            i.putExtras(bolsa);
            startActivity(i);
        }

    }
}