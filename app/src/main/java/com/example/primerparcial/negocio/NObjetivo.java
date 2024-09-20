package com.example.primerparcial.negocio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.primerparcial.dato.DObjetivo;

import java.util.ArrayList;
import java.util.List;

public class NObjetivo {
    private DObjetivo dObjetivo;

    public NObjetivo() {
        this.dObjetivo = new DObjetivo();
    }

    public NObjetivo(int idObjetivo, String nombre) {
        this.dObjetivo = new DObjetivo(idObjetivo, nombre);
    }

    public void insertar(String nombre){
        try {
            dObjetivo.insertar(nombre);
        } catch (Exception e) {
            System.err.println("Error al registrar el objetivo: " + e.getMessage());
        }
    }

    public void modificar(int idObjetivo, String nombre){
        try {
            dObjetivo.modificar(idObjetivo, nombre);
        } catch (Exception e) {
            System.err.println("Error al actualizar el objetivo: " + e.getMessage());
        }
    }

    public void borrar(int idObjetivo){
        try {
            dObjetivo.borrar(idObjetivo);
        } catch (Exception e) {
            System.err.println("Error al eliminar el objetivo: " + e.getMessage());
        }
    }

    public List<NObjetivo> consultar() {
        List<NObjetivo> lista = new ArrayList<>();
        for (DObjetivo de : dObjetivo.consultar()) {
            lista.add(new NObjetivo(de.getIdObjetivo(), de.getNombre()));
        }
        return lista;
    }

    public NObjetivo buscar(int idObjetivo) {
        try {
            if (idObjetivo != -1) {
                DObjetivo de = dObjetivo.buscar(idObjetivo);
                if (de != null) {
                    return new NObjetivo(de.getIdObjetivo(), de.getNombre());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el objetivo: " + e.getMessage());
        }
        return null;
    }

    public DObjetivo getdObjetivo() {
        return dObjetivo;
    }

    public void setdObjetivo(DObjetivo dObjetivo) {
        this.dObjetivo = dObjetivo;
    }

/*    public void setDatabase(SQLiteDatabase database){
        this.dObjetivo.setDatabase(database);
    }

    public SQLiteDatabase getDatabase(){
        return this.getdObjetivo().getDatabase();
    }*/

    public void iniciarBD(Context context) {
        this.dObjetivo.iniciarBD(context);
    }

}
