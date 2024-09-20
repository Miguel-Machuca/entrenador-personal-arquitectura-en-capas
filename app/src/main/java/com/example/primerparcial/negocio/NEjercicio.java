package com.example.primerparcial.negocio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.primerparcial.dato.DEjercicio;
import com.example.primerparcial.presentacion.PEjercicio;

import java.util.ArrayList;
import java.util.List;

public class NEjercicio {
    private DEjercicio dEjercicio;

    public NEjercicio() {
        this.dEjercicio = new DEjercicio();
    }

    public NEjercicio(int idEjercicio, String nombre, String urlImagen) {
        this.dEjercicio = new DEjercicio(idEjercicio, nombre, urlImagen
        ) ;
    }

    public void insertar(String nombre, String urlImagen){
        try {
            dEjercicio.insertar(nombre, urlImagen);
        } catch (Exception e) {
            System.err.println("Error al registrar el ejercicio: " + e.getMessage());
        }
    }

    public void modificar(int idEjercicio, String nombre, String urlImagen){
        try {
            dEjercicio.modificar(idEjercicio, nombre, urlImagen);
        } catch (Exception e) {
            System.err.println("Error al actualizar el ejercicio: " + e.getMessage());
        }
    }

    public void borrar(int idEjercicio){
        try {
            dEjercicio.borrar(idEjercicio);
        } catch (Exception e) {
            System.err.println("Error al eliminar el ejercicio: " + e.getMessage());
        }
    }

    public List<NEjercicio> consultar() {
        List<NEjercicio> lista = new ArrayList<>();
        for (DEjercicio de : dEjercicio.consultar()) {
            lista.add(new NEjercicio(de.getIdEjercicio(), de.getNombre(), de.getUrlImagen()));
        }
        return lista;
    }

    public NEjercicio buscar(int idEjercicio) {
        try {
            if (idEjercicio != -1) {
                DEjercicio de = dEjercicio.buscar(idEjercicio);
                if (de != null) {
                    return new NEjercicio(de.getIdEjercicio(), de.getNombre(), de.getUrlImagen());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el ejercicio: " + e.getMessage());
        }
        return null;
    }

    public DEjercicio getdEjercicio() {
        return dEjercicio;
    }

    public void setdEjercicio(DEjercicio dEjercicio) {
        this.dEjercicio = dEjercicio;
    }

    public void setDatabase(SQLiteDatabase database){
        this.dEjercicio.setDatabase(database);
    }

    public SQLiteDatabase getDatabase(){
        return this.getdEjercicio().getDatabase();
    }

    public void iniciarBD(Context context) {
        this.dEjercicio.iniciarBD(context);
    }
}
