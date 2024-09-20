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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.primerparcial.R;
import com.example.primerparcial.negocio.NCliente;
import com.example.primerparcial.negocio.NEjercicio;
import com.example.primerparcial.negocio.NObjetivo;
import com.example.primerparcial.negocio.NRutina;
import com.example.primerparcial.utils.ObjetivoAdapter;
import com.example.primerparcial.utils.ListObjetivoAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PCliente extends AppCompatActivity implements View.OnClickListener{
    private NCliente nCliente;
    private NObjetivo nObjetivo;
    private int id = 0;
    private int idObjetivo = -1;
    private String nombreCliente;
    private String nombreObjetivo;
    private Context context;
    private Spinner spObjetivos;
    private EditText txtNombreCliente, txtApellidoCliente, txtCelularCliente, txtDescripcion;
    private Button btnInsertar, btnModificar, btnBorrar, btnAgregar, btnSacar;
    private ListView listViewConsultarCliente;
    private ListView listViewConsultarObjetivo;
    private List<Object[]> objetivos;
    private List<String> nombres;
    private List<String> apellidos;
    private List<String> celulares;
    private List<Integer> idClientes;
    private ListObjetivoAdapter adapter;
    private Object[] objetivoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcliente);
        iniciar();
    }

    private void iniciar() {
        context = this;
        txtNombreCliente = findViewById(R.id.et_nombre_cliente);
        txtApellidoCliente = findViewById(R.id.et_apellido_cliente);
        txtCelularCliente = findViewById(R.id.et_celular_cliente);
        txtDescripcion = findViewById(R.id.et_descripcion);

        btnInsertar = findViewById(R.id.btn_insertar_cliente);
        btnModificar = findViewById(R.id.btn_modificar_cliente);
        btnBorrar = findViewById(R.id.btn_borrar_cliente);
        btnAgregar = findViewById(R.id.btn_agregar_objetivo);
        btnSacar = findViewById(R.id.btn_sacar_objetivo);

        spObjetivos = findViewById(R.id.spinner_objetivo);

        listViewConsultarCliente = findViewById(R.id.lv_items_cliente);
        listViewConsultarObjetivo = findViewById(R.id.lv_items_objetivo_cliente);

        nCliente = new NCliente();
        nCliente.iniciarBD(this);
        nObjetivo = new NObjetivo();
        nObjetivo.iniciarBD(this);
        nObjetivo.consultar();
        objetivos = new ArrayList<>();

        adapter = new ListObjetivoAdapter(this, objetivos);
        listViewConsultarObjetivo.setAdapter(adapter);


        crearSpinner();
        configurarBotones();
        consultarClientes();


        Intent intent = getIntent();
        Bundle bolsa = intent.getExtras();

        listViewConsultarObjetivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                objetivoSeleccionado = (Object[]) parent.getItemAtPosition(position);
            }
        });
        if (bolsa != null) {
            id = bolsa.getInt("id", 0);
            nombreCliente = bolsa.getString("nombreC", "");
            if (id != 0) {
                txtNombreCliente.setText(nombreCliente);
                btnInsertar.setEnabled(false); // Deshabilitar el botón de Insertar
                btnModificar.setEnabled(true); // Habilitar el botón de Modificar
                btnBorrar.setEnabled(true); // Habilitar el botón de Borrar
            } else {
                btnModificar.setEnabled(false); // Deshabilitar el botón de Modificar
                btnBorrar.setEnabled(false); // Deshabilitar el botón de Borrar
                btnInsertar.setEnabled(true); // Habilitar el botón de Insertar
            }
        }
    }

    private void consultarClientes() {
        nombres = new ArrayList<>();
        apellidos = new ArrayList<>();
        celulares = new ArrayList<>();
        idClientes = new ArrayList<>();

        List<String> nombre_completo = new ArrayList<>();
        List<NCliente> listaClientes = nCliente.consultar();

        for (NCliente cliente : listaClientes) {
            nombres.add(cliente.getdCliente().getNombre());
            apellidos.add(cliente.getdCliente().getApellido());
            celulares.add(cliente.getdCliente().getCelular());
            idClientes.add(cliente.getdCliente().getIdCliente());
            nombre_completo.add(cliente.getdCliente().getNombre() + " " + cliente.getdCliente().getApellido() + " - celular: " + cliente.getdCliente().getCelular());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombre_completo);
        listViewConsultarCliente.setAdapter(adapter);
        listViewConsultarCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {
                clienteIdSeleccionado(posicion);
            }
        });
    }

    private void clienteIdSeleccionado(int posicion) {
        id = idClientes.get(posicion);
        nombreCliente = nombres.get(posicion);

        objetivos = nCliente.listarObjetivo(id);
        for (int i = 0; i < objetivos.size(); i++) {
            Object[] obj = objetivos.get(i);
            Object[] nuevoObjetivo = Arrays.copyOf(obj, obj.length + 1);

            nuevoObjetivo[obj.length] = nObjetivo.buscar((int) obj[0]).getdObjetivo().getNombre();

            objetivos.set(i, nuevoObjetivo);
        }
        adapter = new ListObjetivoAdapter(this, objetivos);
        listViewConsultarObjetivo.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        txtNombreCliente.setText(nombreCliente);
        txtApellidoCliente.setText(apellidos.get(posicion));
        txtCelularCliente.setText(celulares.get(posicion));

        btnInsertar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnBorrar.setEnabled(true);

        Bundle bolsa = new Bundle();
        bolsa.putInt("id", id);
        bolsa.putString("nombreC", nombreCliente);
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        btnSacar.setOnClickListener(this);
    }

    private void crearSpinner(){
        List<NObjetivo> listaObjetivos = new ArrayList<>();
        listaObjetivos.add(new NObjetivo(-1, "Seleccionar Objetivo"));
        listaObjetivos.addAll(nObjetivo.consultar());

        ObjetivoAdapter adapter = new ObjetivoAdapter(this, listaObjetivos);

        spObjetivos.setAdapter(adapter);

        spObjetivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NObjetivo objetivoSeleccionado = (NObjetivo) parent.getSelectedItem();

                if (objetivoSeleccionado.getdObjetivo().getIdObjetivo() == -1) {
                    mostrarMensaje("Por favor, selecciona un objetivo");
                } else {
                    idObjetivo = objetivoSeleccionado.getdObjetivo().getIdObjetivo();
                    nombreObjetivo = objetivoSeleccionado.getdObjetivo().getNombre();
                    Bundle bolsa = new Bundle();
                    bolsa.putInt("idObjetivo", idObjetivo);
                    bolsa.putString("nombreObjetivo", nombreObjetivo);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void sacarObjetivo() {
        if (objetivoSeleccionado != null) {

            objetivos.remove(objetivoSeleccionado);
            adapter.notifyDataSetChanged();

            spObjetivos.setSelection(0);
            txtDescripcion.setText("");
            idObjetivo = -1;

            objetivoSeleccionado = null;
        } else {
            mostrarMensaje("Por favor, selecciona un objetivo para eliminar");
        }
    }

    private void agregarObjetivo() {
        String descripcion = txtDescripcion.getText().toString().trim();

        if (idObjetivo == -1) {
            mostrarMensaje("Por favor, selecciona un objetivo válido");
            return;
        }
        try {
            objetivos.add(new Object[]{
                    idObjetivo,
                    descripcion,
                    nombreObjetivo
            });
            adapter.notifyDataSetChanged();

            txtDescripcion.setText("");
            spObjetivos.setSelection(0);
            idObjetivo = -1;

        } catch (Exception e) {
            mostrarMensaje("Error al agregar el objetivo: " + e.getMessage());
        }
    }


    private void borrarCliente() {
        nCliente.borrar(id);
        limpiarCampos();
        objetivos.clear();
        adapter.notifyDataSetChanged();
        consultarClientes();
        mostrarMensaje("Cliente eliminado.");
    }

    private void modificarCliente() {
        String nombre = txtNombreCliente.getText().toString().trim();
        String apellido = txtApellidoCliente.getText().toString().trim();
        String celular = txtCelularCliente.getText().toString().trim();

        if (!nombre.isEmpty() && !apellido.isEmpty() && !celular.isEmpty() ) {
            nCliente.modificar(id, nombre, apellido, celular, objetivos);

            nCliente.eliminarObjetivosPorCliente(id);

            for (Object[] objetivo : objetivos) {
                int idObjetivo = (int) objetivo[0];
                String descripcion = (String) objetivo[1];

                nCliente.agregarObjetivoACliente(id, idObjetivo, descripcion);
            }

            reiniciarActividad();

            Intent intent = new Intent(context, PCliente.class);
            startActivity(intent);
            finish();
        } else {
            mostrarMensaje("Los datos del cliente no pueden estar vacíos.");
        }
    }

    private void insertarCliente() {
        String nombre = txtNombreCliente.getText().toString().trim();
        String apellido = txtApellidoCliente.getText().toString().trim();
        String celular = txtCelularCliente.getText().toString().trim();

        if (!nombre.isEmpty() && !apellido.isEmpty() && !celular.isEmpty() ) {
            nCliente.insertar(nombre, apellido, celular, objetivos);
            reiniciarActividad();

            Intent intent = new Intent(context, PCliente.class);
            startActivity(intent);
            finish();
        } else {
            mostrarMensaje("Los datos del cliente no pueden estar vacíos.");
        }
    }

    private void reiniciarActividad() {
        limpiarCampos();
        objetivos.clear();
        consultarClientes();
    }

    private void limpiarCampos() {
        id = 0;
        txtNombreCliente.setText("");
        txtApellidoCliente.setText("");
        txtCelularCliente.setText("");
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_insertar_cliente) {
            insertarCliente();
        } else if (id == R.id.btn_modificar_cliente) {
            modificarCliente();
        } else if (id == R.id.btn_borrar_cliente) {
            borrarCliente();
        } else if (id == R.id.btn_agregar_objetivo) {
            agregarObjetivo();
        } else if (id == R.id.btn_sacar_objetivo) {
            sacarObjetivo();
        }
    }


}