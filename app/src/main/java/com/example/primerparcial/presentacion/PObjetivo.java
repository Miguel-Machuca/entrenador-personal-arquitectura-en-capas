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
import com.example.primerparcial.negocio.NObjetivo;

import java.util.ArrayList;
import java.util.List;

public class PObjetivo extends AppCompatActivity implements View.OnClickListener{
    private int id = 0;
    private NObjetivo nObjetivo;
    private String nombreObjetivo;
    private Context context;
    private Button btnInsertar, btnModificar, btnBorrar;
    private EditText txtNombreObjetivo;
    private ListView listViewConsultar;
    private List<NObjetivo> listaObjetivos;
    private List<String> nombres;
    private List<Integer> idObjetivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pobjetivo);
        iniciar();
    }

    public void iniciar() {
        context = this;
        txtNombreObjetivo = findViewById(R.id.txt_nombre_objetivo);
        btnInsertar = findViewById(R.id.btn_insertar_objetivo);
        btnModificar = findViewById(R.id.btn_modificar_objetivo);
        btnBorrar = findViewById(R.id.btn_borrar_objetivo);
        listViewConsultar = findViewById(R.id.lv_items_objetivo);

        nObjetivo = new NObjetivo();

        nObjetivo.iniciarBD(this);

        configurarBotones();

        consultarObjetivos();

        Intent intent = getIntent();
        Bundle bolsa = intent.getExtras();
        if (bolsa != null){
            id = bolsa.getInt("id", 0);
            nombreObjetivo = bolsa.getString("nombreO", "");
            if (id != 0){
                txtNombreObjetivo.setText(nombreObjetivo);
                btnInsertar.setEnabled(false);
            } else {
                btnModificar.setEnabled(false);
                btnBorrar.setEnabled(false);
            }
        }
    }

    public void insertarObjetivo(){
        String nombre = txtNombreObjetivo.getText().toString().trim();
        if (!nombre.isEmpty()){
            nObjetivo.insertar(nombre);
            reiniciarActividad();
            mostrarMensaje("Objetivo guardado: " + nombre);
        } else {
            mostrarMensaje("El nombre del objetivo no puede estar vacío.");
        }
    }

    public void modificarObjetivo(){
        String nombre = txtNombreObjetivo.getText().toString().trim();
        if (!nombre.isEmpty()){
            nObjetivo.modificar(id, nombre);
            reiniciarActividad();
            mostrarMensaje("Objetivo actualizado: " + nombre);
        } else {
            mostrarMensaje("El nombre del objetivo no puede estar vacío.");
        }
    }

    public void borrarObjetivo(){
        nObjetivo.borrar(id);
        limpiarCampos();
        consultarObjetivos();
        mostrarMensaje("Objetivo eliminado.");
    }

    public void consultarObjetivos(){
        nombres = new ArrayList<>();
        idObjetivos = new ArrayList<>();
        List<NObjetivo> listaObjetivos = nObjetivo.consultar();
        for (NObjetivo objetivo : listaObjetivos){
            nombres.add(objetivo.getdObjetivo().getNombre());
            idObjetivos.add(objetivo.getdObjetivo().getIdObjetivo());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listViewConsultar.setAdapter(adapter);
        listViewConsultar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                objetivoIdSeleccionado(i);
            }
        });
    }

    private void objetivoIdSeleccionado(int i) {
        id = idObjetivos.get(i);
        nombreObjetivo = nombres.get(i);
        Bundle bolsa = new Bundle();
        bolsa.putInt("id", id);
        bolsa.putString("nombreO", nombreObjetivo);
        iniciarNuevaActividad(bolsa);
    }

    private void iniciarNuevaActividad(Bundle bolsa) {
        Intent intent = new Intent(context, PObjetivo.class);
        intent.putExtras(bolsa);
        startActivity(intent);
    }

    public void limpiarCampos(){
        id = 0;
        txtNombreObjetivo.setText("");
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
    }

    private void reiniciarActividad() {
        limpiarCampos();
        consultarObjetivos();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_insertar_objetivo) {
            insertarObjetivo();
        } else if (id == R.id.btn_modificar_objetivo) {
            modificarObjetivo();
        } else if (id == R.id.btn_borrar_objetivo) {
            borrarObjetivo();
        }
    }
}