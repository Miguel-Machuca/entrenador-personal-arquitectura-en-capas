package com.example.primerparcial.presentacion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.primerparcial.R;
import com.example.primerparcial.negocio.NEjercicio;

import java.util.ArrayList;
import java.util.List;

public class PEjercicio extends AppCompatActivity implements View.OnClickListener {
    private int id = 0;
    private String nombreEjercicio;
    private Context context;
    private EditText txtNombreEjercicio;
    private Button btnInsertar, btnModificar, btnBorrar;
    private ListView listViewConsultar;
    private NEjercicio nEjercicio;
    private List<String> nombres;
    private List<Integer> idEjercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pejercicio);
        iniciar();
    }

    private void iniciar() {
        context = this;
        txtNombreEjercicio = findViewById(R.id.txt_nombre_ejercicio);
        btnInsertar = findViewById(R.id.btn_insertar_ejercicio);
        btnModificar = findViewById(R.id.btn_modificar_ejercicio);
        btnBorrar = findViewById(R.id.btn_borrar_ejercicio);
        listViewConsultar = findViewById(R.id.lv_items_ejercicio);

        nEjercicio = new NEjercicio();
        nEjercicio.iniciarBD(this);

        configurarBotones();

        consultarEjercicios();

        Intent intent = getIntent();
        Bundle bolsa = intent.getExtras();
        if (bolsa != null) {
            id = bolsa.getInt("id", 0);
            nombreEjercicio = bolsa.getString("nombreE", "");
            if (id != 0) {
                txtNombreEjercicio.setText(nombreEjercicio);
                btnInsertar.setEnabled(false);
            } else {
                btnModificar.setEnabled(false);
                btnBorrar.setEnabled(false);
            }
        }
    }

    private void insertarEjercicio() {
        String nombre = txtNombreEjercicio.getText().toString().trim();
        if (!nombre.isEmpty()) {
            nEjercicio.insertar(nombre, "ruta_de_la_imagen");
            reiniciarActividad();
            mostrarMensaje("Ejercicio guardado: " + nombre);
        } else {
            mostrarMensaje("El nombre del ejercicio no puede estar vacío.");
        }
    }

    private void modificarEjercicio() {
        String nombre = txtNombreEjercicio.getText().toString().trim();
        if (!nombre.isEmpty()) {
            nEjercicio.modificar(id, nombre, "url modificada");
            reiniciarActividad();
            mostrarMensaje("Ejercicio actualizado: " + nombre);
        } else {
            mostrarMensaje("El nombre del ejercicio no puede estar vacío.");
        }
    }

    private void borrarEjercicio() {
        nEjercicio.borrar(id);
        limpiarCampos();
        consultarEjercicios();
        mostrarMensaje("Ejercicio eliminado.");
    }

    private void consultarEjercicios() {
        nombres = new ArrayList<>();
        idEjercicios = new ArrayList<>();
        List<NEjercicio> listaEjercicios = nEjercicio.consultar();

        for (NEjercicio ejercicio : listaEjercicios) {
            nombres.add(ejercicio.getdEjercicio().getNombre());
            idEjercicios.add(ejercicio.getdEjercicio().getIdEjercicio());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listViewConsultar.setAdapter(adapter);
        listViewConsultar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {
                ejercicioIdSeleccionado(posicion);
            }
        });
    }

    private void limpiarCampos() {
        id = 0;
        txtNombreEjercicio.setText("");
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }

    private void ejercicioIdSeleccionado(int posicion) {
        id = idEjercicios.get(posicion);
        nombreEjercicio = nombres.get(posicion);
        Bundle bolsa = new Bundle();
        bolsa.putInt("id", id);
        bolsa.putString("nombreE", nombreEjercicio);
        iniciarNuevaActividad(bolsa);
    }

    private void iniciarNuevaActividad(Bundle bolsa) {
        Intent intent = new Intent(context, PEjercicio.class);
        intent.putExtras(bolsa);
        startActivity(intent);
    }

    private void reiniciarActividad() {
        limpiarCampos();
        consultarEjercicios();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_insertar_ejercicio) {
            insertarEjercicio();
        } else if (id == R.id.btn_modificar_ejercicio) {
            modificarEjercicio();
        } else if (id == R.id.btn_borrar_ejercicio) {
            borrarEjercicio();
        }
    }
}
