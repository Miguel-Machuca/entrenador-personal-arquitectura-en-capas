package com.example.primerparcial.presentacion;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.example.primerparcial.R;
import com.example.primerparcial.negocio.NEjercicio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PEjercicio extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 100;

    private EditText txtNombreEjercicio;
    private Button btnInsertar, btnModificar, btnBorrar, btnSubirImagen;
    private ListView listViewConsultar;
    private ImageView ivImagenEjercicio;

    private NEjercicio nEjercicio;
    private List<NEjercicio> ejercicios;

    private NEjercicio ejercicioSeleccionado;

    private Uri imagenSeleccionadaUri;

    private ActivityResultLauncher<Intent> selectImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pejercicio);
        iniciar();
    }

    private void iniciar() {
        // Inicializar vistas
        txtNombreEjercicio = findViewById(R.id.txt_nombre_ejercicio);
        btnInsertar = findViewById(R.id.btn_insertar_ejercicio);
        btnModificar = findViewById(R.id.btn_modificar_ejercicio);
        btnBorrar = findViewById(R.id.btn_borrar_ejercicio);
        btnSubirImagen = findViewById(R.id.btn_subir_imagen);
        listViewConsultar = findViewById(R.id.lv_items_ejercicio);
        ivImagenEjercicio = findViewById(R.id.iv_imagen_ejercicio);

        nEjercicio = new NEjercicio();
        nEjercicio.iniciarBD(this);

        configurarBotones();

        configurarSelectImageLauncher();

        consultarEjercicios();

        manejarIntent();

        verificarPermisos();
    }

    private void configurarSelectImageLauncher() {
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            imagenSeleccionadaUri = uri;
                            ivImagenEjercicio.setImageURI(uri);
                        }
                    }
                }
        );
    }


    private void manejarIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            int id = intent.getIntExtra("id", 0);
            String nombreEjercicio = intent.getStringExtra("nombreEjercicio");
            String imagenUrl = intent.getStringExtra("imagenUrl");

            if (id != 0) {
                ejercicioSeleccionado = nEjercicio.buscar(id);
                if (ejercicioSeleccionado != null) {
                    txtNombreEjercicio.setText(ejercicioSeleccionado.getdEjercicio().getNombre());
                    cargarImagen(ejercicioSeleccionado.getdEjercicio().getUrlImagen());
                    btnInsertar.setEnabled(false);
                    btnModificar.setEnabled(true);
                    btnBorrar.setEnabled(true);
                }
            }
        }
    }

    private void verificarPermisos() {
        List<String> permisosNecesarios = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permisosNecesarios.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permisosNecesarios.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permisosNecesarios.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permisosNecesarios.toArray(new String[0]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permisos, @NonNull int[] resultados) {
        super.onRequestPermissionsResult(requestCode, permisos, resultados);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean todosOtorgados = true;
            for (int resultado : resultados) {
                if (resultado != PackageManager.PERMISSION_GRANTED) {
                    todosOtorgados = false;
                    break;
                }
            }
            if (!todosOtorgados) {
                Toast.makeText(this, "Permisos de almacenamiento son necesarios para esta aplicación.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String guardarImagenEnAlmacenamiento(Uri uri) {
        String nombreImagen = UUID.randomUUID().toString() + ".jpg";
        String path = getExternalFilesDir(null) + "/" + nombreImagen;
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(path)) {
            if (inputStream == null) return "";
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen.", Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private void insertarEjercicio() {
        String nombre = txtNombreEjercicio.getText().toString().trim();
        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre del ejercicio no puede estar vacío.");
            return;
        }

        String imagenPath = "";
        if (imagenSeleccionadaUri != null) {
            imagenPath = guardarImagenEnAlmacenamiento(imagenSeleccionadaUri);
            if (imagenPath.isEmpty()) {
                mostrarMensaje("Error al guardar la imagen.");
                return;
            }
        }

        // Inserta el ejercicio con la ruta de la imagen (puede estar vacía)
        nEjercicio.insertar(nombre, imagenPath);
        reiniciarActividad();
        mostrarMensaje("Ejercicio guardado: " + nombre);
    }

    private void modificarEjercicio() {
        if (ejercicioSeleccionado == null) {
            mostrarMensaje("No hay ejercicio seleccionado para modificar.");
            return;
        }
        String nombre = txtNombreEjercicio.getText().toString().trim();
        if (nombre.isEmpty()) {
            mostrarMensaje("El nombre del ejercicio no puede estar vacío.");
            return;
        }

        String imagenUrl = ejercicioSeleccionado.getdEjercicio().getUrlImagen();
        if (imagenSeleccionadaUri != null) {
            String nuevaImagenPath = guardarImagenEnAlmacenamiento(imagenSeleccionadaUri);
            if (!nuevaImagenPath.isEmpty()) {
                imagenUrl = nuevaImagenPath;
            } else {
                mostrarMensaje("Error al guardar la imagen.");
                return;
            }
        }

        nEjercicio.modificar(ejercicioSeleccionado.getdEjercicio().getIdEjercicio(), nombre, imagenUrl);
        reiniciarActividad();
        mostrarMensaje("Ejercicio actualizado: " + nombre);
    }

    private void borrarEjercicio() {
        if (ejercicioSeleccionado == null) {
            mostrarMensaje("No hay ejercicio seleccionado para eliminar.");
            return;
        }
        nEjercicio.borrar(ejercicioSeleccionado.getdEjercicio().getIdEjercicio());
        reiniciarActividad();
        mostrarMensaje("dEjercicio eliminado.");
    }


    private void consultarEjercicios() {
        ejercicios = nEjercicio.consultar();
        List<String> nombres = new ArrayList<>();
        for (NEjercicio ejercicio : ejercicios) {
            nombres.add(ejercicio.getdEjercicio().getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listViewConsultar.setAdapter(adapter);
        listViewConsultar.setOnItemClickListener((parent, view, position, id) -> seleccionarEjercicio(position));
    }

    private void seleccionarEjercicio(int posicion) {
        if (posicion < 0 || posicion >= ejercicios.size()) {
            mostrarMensaje("Posición inválida seleccionada.");
            return;
        }

        ejercicioSeleccionado = ejercicios.get(posicion);
        txtNombreEjercicio.setText(ejercicioSeleccionado.getdEjercicio().getNombre());
        cargarImagen(ejercicioSeleccionado.getdEjercicio().getUrlImagen());

        btnInsertar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnBorrar.setEnabled(true);
    }

    private void cargarImagen(String urlImagen) {
        if (urlImagen != null && !urlImagen.isEmpty()) {
            File imagenFile = new File(urlImagen);
            Glide.with(this)
                    .load(imagenFile)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivImagenEjercicio);
        } else {
            ivImagenEjercicio.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private void reiniciarActividad() {
        txtNombreEjercicio.setText("");
        imagenSeleccionadaUri = null;
        ivImagenEjercicio.setImageResource(R.drawable.ic_launcher_foreground);
        btnInsertar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnBorrar.setEnabled(false);
        consultarEjercicios();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void configurarBotones() {
        btnInsertar.setOnClickListener(this);
        btnModificar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnSubirImagen.setOnClickListener(this);
    }

    private void subirImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        selectImageLauncher.launch(intent);
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
        } else if (id == R.id.btn_subir_imagen) {
            subirImagen();
        }
    }

}
