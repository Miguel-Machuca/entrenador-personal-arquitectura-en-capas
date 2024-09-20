package com.example.primerparcial.negocio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.primerparcial.dato.DCronograma;

import java.util.ArrayList;
import java.util.List;

public class NCronograma {
    private DCronograma dCronograma;

    public NCronograma() {
        this.dCronograma = new DCronograma();
    }

    public NCronograma(int idCronograma, String fecha, int idCliente, int idRutina) {
        this.dCronograma = new DCronograma(idCronograma, fecha, idCliente, idRutina);
    }

    public void insertar(String fecha, int idCliente, int idRutina){
        try {
            dCronograma.insertar(fecha, idCliente, idRutina);
        } catch (Exception e) {
            System.err.println("Error al registrar el cronograma: " + e.getMessage());
        }
    }

    public void modificar(int idCronograma, String fecha, int idCliente, int idRutina){
        try {
            dCronograma.modificar(idCronograma, fecha, idCliente, idRutina);
        } catch (Exception e) {
            System.err.println("Error al actualizar el cronograma: " + e.getMessage());
        }
    }

    public void borrar(int idCronograma){
        try {
            dCronograma.borrar(idCronograma);
        } catch (Exception e) {
            System.err.println("Error al eliminar el cronograma: " + e.getMessage());
        }
    }

    public List<NCronograma> consultar() {
        List<NCronograma> lista = new ArrayList<>();
        for (DCronograma dc : dCronograma.consultar()) {
            lista.add(new NCronograma(dc.getIdCronograma(), dc.getFecha(), dc.getIdCliente(), dc.getIdRutina()));
        }
        return lista;
    }

    public NCronograma buscar(int idCronograma) {
        try {
            if (idCronograma != -1) {
                DCronograma dc = dCronograma.buscar(idCronograma);
                if (dc != null) {
                    return new NCronograma(dc.getIdCronograma(), dc.getFecha(), dc.getIdCliente(), dc.getIdRutina());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el cronograma: " + e.getMessage());
        }
        return null;
    }

    public DCronograma getdCronograma() {
        return dCronograma;
    }

    public void setdCronograma(DCronograma dCronograma) {
        this.dCronograma = dCronograma;
    }

    public void iniciarBD(Context context) {
        this.dCronograma.iniciarBD(context);
    }
}
