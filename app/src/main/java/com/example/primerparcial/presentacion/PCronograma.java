package com.example.primerparcial.presentacion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.primerparcial.R;
import com.example.primerparcial.negocio.NCliente;
import com.example.primerparcial.negocio.NCronograma;
import com.example.primerparcial.negocio.NRutina;
import com.example.primerparcial.utils.ClienteAdapter;
import com.example.primerparcial.utils.RutinaAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PCronograma extends AppCompatActivity implements View.OnClickListener{
    private int id = 0;
    private NCliente nCliente;
    private NRutina nRutina;
    private int idCliente = -1;
    private int idRutina = -1;
    private String nombreCliente;
    private String nombreRutina;
    private NCronograma nCronograma;
    private String fecha;
    private Context context;
    private Spinner spClientes, spRutinas;
    private Button btnInsertar, btnModificar, btnBorrar;
    private EditText txtFecha;
    private ListView listViewConsultar;
    private List<String> nombres;
    private List<String> fechas;
    private List<Integer> idCronogramas;
    private List<Integer> idClientes;
    private List<Integer> idRutinas;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcronograma);
        iniciar();
    }

    public void iniciar() {
        context = this;
        txtFecha = findViewById(R.id.et_fecha);
        btnInsertar = findViewById(R.id.btn_insertar_cronograma);
        btnModificar = findViewById(R.id.btn_modificar_cronograma);
        btnBorrar = findViewById(R.id.btn_borrar_cronograma);
        listViewConsultar = findViewById(R.id.lv_items_cronograma);

        spClientes = findViewById(R.id.spinner_cliente);
        spRutinas = findViewById(R.id.spinner_rutina);

        nCliente = new NCliente();
        nRutina = new NRutina();
        nCronograma = new NCronograma();
        nCronograma.iniciarBD(this);
        nCliente .iniciarBD(this);
        nRutina.iniciarBD(this);

        crearCalendario();
        crearSpinnerCliente();
        crearSpinnerRutina();

        configurarBotones();

        consultarCronograma();

        Intent intent = getIntent();
        Bundle bolsa = intent.getExtras();
        if (bolsa != null){
            id = bolsa.getInt("id", 0);
            fecha = bolsa.getString("fecha", "");
            if (id != 0){
                txtFecha.setText(fecha);
                btnInsertar.setEnabled(false);
            } else {
                btnModificar.setEnabled(false);
                btnBorrar.setEnabled(false);
            }
        }
    }

    private void consultarCronograma() {
        nombres = new ArrayList<>();
        fechas = new ArrayList<>();
        idCronogramas = new ArrayList<>();
        idClientes = new ArrayList<>();
        idRutinas = new ArrayList<>();
        List<NCronograma> listaCronogramas = nCronograma.consultar();
        for (NCronograma cronograma : listaCronogramas){
            fechas.add(String.valueOf(cronograma.getdCronograma().getFecha()));
            idCronogramas.add(cronograma.getdCronograma().getIdCronograma());
            idClientes.add(cronograma.getdCronograma().getIdCliente());
            idRutinas.add(cronograma.getdCronograma().getIdRutina());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fechas);
        listViewConsultar.setAdapter(adapter);
        listViewConsultar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cronogramaIdSeleccionado(i);
            }
        });
    }

    private void cronogramaIdSeleccionado(int i) {
        id = idCronogramas.get(i);
        fecha = nombres.get(i);
        Bundle bolsa = new Bundle();
        bolsa.putInt("id", id);
        bolsa.putString("fecha", fecha);
        iniciarNuevaActividad(bolsa);
    }

    private void iniciarNuevaActividad(Bundle bolsa) {
        Intent intent = new Intent(context, PCronograma.class);
        intent.putExtras(bolsa);
        startActivity(intent);
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
    }

    public void crearCalendario(){
        EditText etFecha = findViewById(R.id.et_fecha);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén la fecha actual
                final Calendar calendar = Calendar.getInstance();
                int anio = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);

                // Muestra el DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(PCronograma.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Mes +1 porque enero es 0
                                String fechaSeleccionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                etFecha.setText(fechaSeleccionada);
                            }
                        }, anio, mes, dia);
                datePickerDialog.show();
            }
        });
    }

    private void crearSpinnerCliente(){
        List<NCliente> listaClientes = new ArrayList<>();
        listaClientes.add(new NCliente(-1, "Seleccionar Cliente", "", ""));
        listaClientes.addAll(nCliente.consultar());

        ClienteAdapter adapter = new ClienteAdapter(this, listaClientes);

        spClientes.setAdapter(adapter);

        spClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NCliente clienteSeleccionado = (NCliente) parent.getSelectedItem();

                if (clienteSeleccionado.getdCliente().getIdCliente() == -1) {
                    mostrarMensaje("Por favor, selecciona un cliente");
                } else {
                    idCliente = clienteSeleccionado.getdCliente().getIdCliente();
                    nombreCliente = clienteSeleccionado.getdCliente().getNombre();
                    Bundle bolsa = new Bundle();
                    bolsa.putInt("idCliente", idCliente);
                    bolsa.putString("nombreCliente", nombreCliente);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void crearSpinnerRutina(){
        List<NRutina> listaRutinas = new ArrayList<>();
        listaRutinas.add(new NRutina(-1, "Seleccionar Rutina"));
        listaRutinas.addAll(nRutina.consultar());

        RutinaAdapter adapter = new RutinaAdapter(this, listaRutinas);

        spRutinas.setAdapter(adapter);

        spRutinas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                NRutina rutinaSeleccionado = (NRutina) parent.getSelectedItem();

                if (rutinaSeleccionado.getdRutina().getIdRutina() == -1) {
                    mostrarMensaje("Por favor, selecciona una Rutina");
                } else {
                    idRutina = rutinaSeleccionado.getdRutina().getIdRutina();
                    nombreRutina = rutinaSeleccionado.getdRutina().getNombre();
                    Bundle bolsa = new Bundle();
                    bolsa.putInt("idRutina", idRutina);
                    bolsa.putString("nombreRutina", nombreRutina);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void borrarCronograma() {
        nCronograma.borrar(id);
        limpiarCampos();
        consultarCronograma();
        mostrarMensaje("Cronograma eliminado.");
    }

    private void modificarCronograma() {
        String fecha = txtFecha.getText().toString().trim();
        if (!fecha.isEmpty()){
            nCronograma.modificar(id, fecha, idCliente, idRutina);
            reiniciarActividad();
            mostrarMensaje("Cronograma actualizado: " + fecha);
        } else {
            mostrarMensaje("La fecha del cronograma no puede estar vacía.");
        }
    }

    private void insertarCronograma() {
        String fecha = txtFecha.getText().toString().trim();

        if (!fecha.isEmpty() && idCliente != -1 && idRutina != -1 ) {
            nCronograma.insertar(fecha, idCliente, idRutina);
            reiniciarActividad();
            mostrarMensaje("Cronograma guardado: " + fecha);
        } else {
            mostrarMensaje("Los datos no pueden estar vacíos.");
        }
    }

    private void reiniciarActividad() {
        limpiarCampos();
        consultarCronograma();
    }

    private void limpiarCampos() {
        id = 0;
        txtFecha.setText("");
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
        if (id == R.id.btn_insertar_cronograma) {
            insertarCronograma();
        } else if (id == R.id.btn_modificar_cronograma) {
            modificarCronograma();
        } else if (id == R.id.btn_borrar_cronograma) {
            borrarCronograma();
        }
    }
}