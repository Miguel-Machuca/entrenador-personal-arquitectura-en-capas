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

import androidx.appcompat.app.AppCompatActivity;

import com.example.primerparcial.R;
import com.example.primerparcial.negocio.NEjercicio;
import com.example.primerparcial.negocio.NRutina;
import com.example.primerparcial.utils.EjercicioAdapter;
import com.example.primerparcial.utils.ListEjercicioAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PRutina extends AppCompatActivity implements View.OnClickListener{
    private NEjercicio nEjercicio;
    private NRutina nRutina;
    private int id = 0;
    private int idEjercicio = -1;
    private String nombreRutina;
    private String nombreEjercicio;
    private Context context;
    private Spinner spEjercicios;
    private EditText txtNombreRutina, txtCantidadSerie, txtCantidadRepeticion, txtReposoSerie, txtAgregado;
    private Button btnInsertar, btnModificar, btnBorrar, btnAgregar, btnSacar;
    private ListView listViewConsultarRutina;
    private ListView listViewConsultarEjercicio;
    private List<Object[]> ejercicios;
    private List<String> nombres;
    private List<Integer> idRutinas;
    private ListEjercicioAdapter adapter;
    private Object[] ejercicioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prutina);
        iniciar();
    }

    private void iniciar() {
        context = this;
        txtNombreRutina = findViewById(R.id.et_nombre_rutina);
        txtCantidadSerie = findViewById(R.id.et_cantidad_serie);
        txtCantidadRepeticion = findViewById(R.id.et_cantidad_repeticion);
        txtReposoSerie = findViewById(R.id.et_reposo_entre_serie);

        btnInsertar = findViewById(R.id.btn_insertar_rutina);
        btnModificar = findViewById(R.id.btn_modificar_rutina);
        btnBorrar = findViewById(R.id.btn_borrar_rutina);
        btnAgregar = findViewById(R.id.btn_agregar_ejercicio);
        btnSacar = findViewById(R.id.btn_sacar_ejercicio);


        spEjercicios = findViewById(R.id.spinner_ejercicio);

        listViewConsultarRutina = findViewById(R.id.lv_items_rutina);
        listViewConsultarEjercicio = findViewById(R.id.lv_items_rutina_ejercicio); // Asegúrate de que este ID exista en tu XML

        nRutina = new NRutina();
        nRutina.iniciarBD(this);
        nEjercicio = new NEjercicio();
        nEjercicio.iniciarBD(this);
        nEjercicio.consultar();
        ejercicios = new ArrayList<>();

        adapter = new ListEjercicioAdapter(this, ejercicios);
        listViewConsultarEjercicio.setAdapter(adapter);

        crearSpinner();
        configurarBotones();
        consultarRutinas();

        Intent intent = getIntent();
        Bundle bolsa = intent.getExtras();

        listViewConsultarEjercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ejercicioSeleccionado = (Object[]) parent.getItemAtPosition(position);
            }
        });
        if (bolsa != null) {
            id = bolsa.getInt("id", 0);
            nombreRutina = bolsa.getString("nombreR", "");
            if (id != 0) {
                txtNombreRutina.setText(nombreRutina);
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

    private void crearSpinner(){
        List<NEjercicio> listaEjercicios = new ArrayList<>();
        listaEjercicios.add(new NEjercicio(-1, "Seleccionar Ejercicio", ""));
        listaEjercicios.addAll(nEjercicio.consultar());

        EjercicioAdapter adapter = new EjercicioAdapter(this, listaEjercicios);

        spEjercicios.setAdapter(adapter);

        spEjercicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NEjercicio ejercicioSeleccionado = (NEjercicio) parent.getSelectedItem();

                if (ejercicioSeleccionado.getdEjercicio().getIdEjercicio() == -1) {
                    mostrarMensaje("Por favor, selecciona un ejercicio");
                } else {
                    idEjercicio = ejercicioSeleccionado.getdEjercicio().getIdEjercicio();
                    nombreEjercicio = ejercicioSeleccionado.getdEjercicio().getNombre();
                    Bundle bolsa = new Bundle();
                    bolsa.putInt("idEjercicio", idEjercicio);
                    bolsa.putString("nombreEjercicio", nombreEjercicio);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void insertarRutina() {
        String nombre = txtNombreRutina.getText().toString().trim();

        if (!nombre.isEmpty()) {
            nRutina.insertar(nombre, ejercicios);
            reiniciarActividad();

            Intent intent = new Intent(context, PRutina.class);
            startActivity(intent);
            finish();
        } else {
            mostrarMensaje("El nombre de la rutina no puede estar vacío.");
        }
    }

    private void modificarRutina() {
        String nombre = txtNombreRutina.getText().toString().trim();

        if (!nombre.isEmpty()) {
            nRutina.modificar(id, nombre, ejercicios);

            nRutina.eliminarEjerciciosPorRutina(id);

            for (Object[] ejercicio : ejercicios) {
                int idEjercicio = (int) ejercicio[0];
                int cantidadSerie = (int) ejercicio[1];
                int cantidadRepeticion = (int) ejercicio[2];
                int reposoSerie = (int) ejercicio[3];

                nRutina.agregarEjercicioARutina(id, idEjercicio, cantidadSerie, cantidadRepeticion, reposoSerie);
            }

            reiniciarActividad();

            Intent intent = new Intent(context, PRutina.class);
            startActivity(intent);
            finish();
        } else {
            mostrarMensaje("El nombre de la rutina no puede estar vacío.");
        }
    }

    private void borrarRutina() {
        nRutina.borrar(id);
        limpiarCampos();
        ejercicios.clear();
        adapter.notifyDataSetChanged();
        consultarRutinas();
        mostrarMensaje("Rutina eliminada.");
    }

    private void agregarEjercicio() {
        String serie = txtCantidadSerie.getText().toString().trim();
        String repeticion = txtCantidadRepeticion.getText().toString().trim();
        String reposo = txtReposoSerie.getText().toString().trim();

        if (idEjercicio == -1 || serie.isEmpty() || repeticion.isEmpty() || reposo.isEmpty()) {
            mostrarMensaje("Por favor, completa todos los campos");
            return;
        }

        try {
            ejercicios.add(new Object[]{
                    idEjercicio,
                    Integer.parseInt(serie),
                    Integer.parseInt(repeticion),
                    Integer.parseInt(reposo),
                    nombreEjercicio
            });
            adapter.notifyDataSetChanged();

            txtCantidadSerie.setText("");
            txtCantidadRepeticion.setText("");
            txtReposoSerie.setText("");
            spEjercicios.setSelection(0);
            idEjercicio = -1;

        } catch (NumberFormatException e) {
            mostrarMensaje("Por favor, ingresa números válidos");
        }
    }

    private void sacarEjercicio() {
        if (ejercicioSeleccionado != null) {

            ejercicios.remove(ejercicioSeleccionado);
            adapter.notifyDataSetChanged();

            spEjercicios.setSelection(0);
            txtCantidadSerie.setText("");
            txtCantidadRepeticion.setText("");
            txtReposoSerie.setText("");
            idEjercicio = -1;

            ejercicioSeleccionado = null;
        } else {
            mostrarMensaje("Por favor, selecciona un ejercicio para eliminar");
        }
    }


    private void consultarRutinas() {
        nombres = new ArrayList<>();
        idRutinas = new ArrayList<>();
        List<NRutina> listaRutinas = nRutina.consultar();

        for (NRutina rutina : listaRutinas) {
            nombres.add(rutina.getdRutina().getNombre());
            idRutinas.add(rutina.getdRutina().getIdRutina());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listViewConsultarRutina.setAdapter(adapter);
        listViewConsultarRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int posicion, long id) {
                rutinaIdSeleccionado(posicion);
            }
        });
    }

    private void rutinaIdSeleccionado(int posicion) {
        id = idRutinas.get(posicion);
        nombreRutina = nombres.get(posicion);

        ejercicios = nRutina.listarEjercicio(id);
        for (int i = 0; i < ejercicios.size(); i++) {
            Object[] ejer = ejercicios.get(i);
            Object[] nuevoEjercicio = Arrays.copyOf(ejer, ejer.length + 1);

            nuevoEjercicio[ejer.length] = nEjercicio.buscar((int) ejer[0]).getdEjercicio().getNombre();

            ejercicios.set(i, nuevoEjercicio);
        }
        adapter = new ListEjercicioAdapter(this, ejercicios);
        listViewConsultarEjercicio.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        txtNombreRutina.setText(nombreRutina);

        btnInsertar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnBorrar.setEnabled(true);

        Bundle bolsa = new Bundle();
        bolsa.putInt("id", id);
        bolsa.putString("nombreR", nombreRutina);
    }

    private void iniciarNuevaActividad(Bundle bolsa) {
        Intent intent = new Intent(context, PRutina.class);
        intent.putExtras(bolsa);
        startActivity(intent);
    }

    private void limpiarCampos() {
        id = 0;
        txtNombreRutina.setText("");
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }

    private void reiniciarActividad() {
        limpiarCampos();
        ejercicios.clear();
        consultarRutinas();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnAgregar.setOnClickListener(this);
        btnSacar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_insertar_rutina) {
            insertarRutina();
        } else if (id == R.id.btn_modificar_rutina) {
            modificarRutina();
        } else if (id == R.id.btn_borrar_rutina) {
            borrarRutina();
        } else if (id == R.id.btn_agregar_ejercicio) {
            agregarEjercicio();
        } else if (id == R.id.btn_sacar_ejercicio) {
            sacarEjercicio();
        }
    }
}