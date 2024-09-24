package com.example.primerparcial.negocio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.primerparcial.dato.DDetalleRutina;
import com.example.primerparcial.dato.DEjercicio;
import com.example.primerparcial.dato.DObjetivo;
import com.example.primerparcial.dato.DRutina;
import com.example.primerparcial.presentacion.PRutina;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NRutina {
    private DRutina dRutina;
    private List<DDetalleRutina> detalleRutina;
    private Context context;

    public NRutina() {
        this.dRutina = new DRutina();
    }

    public NRutina(int idRutina, String nombre) {
        this.detalleRutina = new ArrayList<>();
        this.dRutina = new DRutina(idRutina, nombre);
    }

    public void insertar(String nombre, List<Object []> detalleRutinas) {
        try {
            dRutina.insertar(nombre);
            for (Object[] dr : detalleRutinas){
                DDetalleRutina nuevoDetalle = new DDetalleRutina();
                nuevoDetalle.iniciarBD(context);
                nuevoDetalle.insertar(dRutina.getIdRutina(), (int)dr[0], (int)dr[1], (int)dr[2], (int)dr[3]);
                detalleRutina.add(nuevoDetalle);
            }
        } catch (Exception e) {
            System.err.println("Error al registrar el ejercicio: " + e.getMessage());
        }
    }

    public void modificar(int idRutina, String nombre, List<Object []> detalleRutinas) {
        try {
            dRutina.modificar(idRutina, nombre);
            for (Object[] dr : detalleRutinas){
                DDetalleRutina nuevoDetalle = new DDetalleRutina();
                nuevoDetalle.insertar(idRutina, (int)dr[0], (int)dr[1], (int)dr[2], (int)dr[3]);
                detalleRutina.add(nuevoDetalle);
            }
        } catch (Exception e) {
            System.err.println("Error al modificar el ejercicio: " + e.getMessage());
        }
    }

    public void borrar(int idRutina) {
        try {
            DDetalleRutina nuevoDetalle = new DDetalleRutina();
            nuevoDetalle.iniciarBD(context);
            List<DDetalleRutina> lista = nuevoDetalle.listarEjercicio(idRutina);

            for (DDetalleRutina dr : lista){
                nuevoDetalle.borrar(idRutina, dr.getIdEjercicio());
            }
            dRutina.borrar(idRutina);
        } catch (Exception e) {
            System.err.println("Error al borrar la rutina: " + e.getMessage());
        }
    }

    public List<NRutina> consultar() {
        List<NRutina> lista = new ArrayList<>();
        for (DRutina dr : dRutina.consultar()) {
            lista.add(new NRutina(dr.getIdRutina(), dr.getNombre()));
        }
        return lista;
    }

    public List<Object []> listarEjercicio(int idRutina){
        List<Object []> lista = new ArrayList<>();
        DDetalleRutina nuevoDetalle = new DDetalleRutina();
        nuevoDetalle.iniciarBD(context);
        List<DDetalleRutina> detalleRutina = nuevoDetalle.listarEjercicio(idRutina);
        for (DDetalleRutina dr : detalleRutina){
            lista.add(new Object[]{dr.getIdEjercicio(), dr.getCantidadSerie(), dr.getCantidadRepeticion(), dr.getDuracionReposo()});
        }

        return lista;
    }

    public void agregarEjercicioARutina(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int reposoSerie) {
        DDetalleRutina dDetalleRutina = new DDetalleRutina();
        dDetalleRutina.iniciarBD(context);
        dDetalleRutina.agregarEjercicioARutina( idRutina, idEjercicio, cantidadSerie, cantidadRepeticion, reposoSerie);
    }

    public void eliminarEjerciciosPorRutina(int idRutina) {
        DDetalleRutina dDetalleRutina = new DDetalleRutina();
        dDetalleRutina.iniciarBD(context);
        dDetalleRutina.eliminarEjerciciosPorRutina(idRutina);
    }


    public void iniciarBD(Context context) {
        this.context = context;
        this.dRutina.iniciarBD(context);
        if (this.detalleRutina != null) {
            for (DDetalleRutina ddr : detalleRutina) {
                ddr.iniciarBD(context);
            }
        } else {
            this.detalleRutina = new ArrayList<>();
        }
    }

    public NRutina buscar(int idRutina){
        try {
            if (idRutina != -1) {
                DRutina dr = dRutina.buscar(idRutina);
                if (dr != null) {
                    return new NRutina(dr.getIdRutina(), dr.getNombre());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el ejercicio: " + e.getMessage());
        }
        return null;
    }

    public DRutina getdRutina() {
        return dRutina;
    }

    public void setdRutina(DRutina dRutina) {
        this.dRutina = dRutina;
    }

    public List<DDetalleRutina> getDetalleRutina() {
        return detalleRutina;
    }

    public void setDetalleRutina(List<DDetalleRutina> detalleRutina) {
        this.detalleRutina = detalleRutina;
    }

}
