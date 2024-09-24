package com.example.primerparcial.negocio;

import android.content.Context;

import com.example.primerparcial.dato.DCliente;
import com.example.primerparcial.dato.DDetalleCliente;
import com.example.primerparcial.dato.DDetalleRutina;
import com.example.primerparcial.dato.DEjercicio;
import com.example.primerparcial.dato.DRutina;

import java.util.ArrayList;
import java.util.List;

public class NCliente {
    private DCliente dCliente;
    private List<DDetalleCliente> detalleCliente;
    private Context context;

    public NCliente() {
        this.dCliente = new DCliente();
    }

    public NCliente(int idCliente, String nombre, String apellido, String celular) {
        this.detalleCliente = new ArrayList<>();
        this.dCliente = new DCliente(idCliente, nombre, apellido, celular);
    }

    public void insertar(String nombre, String apellido, String celular, List<Object []> detalleClientes) {
        try {
            dCliente.insertar(nombre, apellido, celular);
            for (Object[] cl : detalleClientes){
                DDetalleCliente nuevoDetalle = new DDetalleCliente();
                nuevoDetalle.iniciarBD(context);
                nuevoDetalle.insertar(dCliente.getIdCliente(), (int)cl[0], (String) cl[1]);
                detalleCliente.add(nuevoDetalle);
            }
        } catch (Exception e) {
            System.err.println("Error al registrar el cliente: " + e.getMessage());
        }
    }

    public void modificar(int idCliente, String nombre, String apellido, String celular, List<Object []> detalleClientes) {
        try {
            dCliente.modificar(idCliente, nombre, apellido, celular);
            for (Object[] cl : detalleClientes){
                DDetalleCliente nuevoDetalle = new DDetalleCliente();
                nuevoDetalle.insertar(idCliente, (int)cl[0], (String)cl[1]);
                detalleCliente.add(nuevoDetalle);
            }
        } catch (Exception e) {
            System.err.println("Error al modificar al cliente: " + e.getMessage());
        }
    }

    public void borrar(int idCliente) {
        try {
            DDetalleCliente nuevoDetalle = new DDetalleCliente();
            nuevoDetalle.iniciarBD(context);
            List<DDetalleCliente> lista = nuevoDetalle.listarObjetivo(idCliente);

            for (DDetalleCliente cl : lista){
                nuevoDetalle.borrar(idCliente, cl.getIdObjetivo());
            }
            dCliente.borrar(idCliente);
        } catch (Exception e) {
            System.err.println("Error al borrar al cliente: " + e.getMessage());
        }
    }

    public List<NCliente> consultar() {
        List<NCliente> lista = new ArrayList<>();
        for (DCliente cl : dCliente.consultar()) {
            lista.add(new NCliente(cl.getIdCliente(), cl.getNombre(), cl.getApellido(), cl.getCelular()));
        }
        return lista;
    }

    public List<Object []> listarObjetivo(int idCliente){
        List<Object []> lista = new ArrayList<>();
        DDetalleCliente nuevoDetalle = new DDetalleCliente();
        nuevoDetalle.iniciarBD(context);
        List<DDetalleCliente> detalleCliente = nuevoDetalle.listarObjetivo(idCliente);
        for (DDetalleCliente cl : detalleCliente){
            lista.add(new Object[]{cl.getIdObjetivo(), cl.getDescripcion()});
        }

        return lista;
    }

    public void agregarObjetivoACliente(int idCliente, int idObjetivo, String descripcion) {
        DDetalleCliente dDetalleCliente = new DDetalleCliente();
        dDetalleCliente.iniciarBD(context);
        dDetalleCliente.agregarObjetivoACliente( idCliente, idObjetivo, descripcion);
    }

    public void eliminarObjetivosPorCliente(int idCliente) {
        DDetalleCliente dDetalleCliente = new DDetalleCliente();
        dDetalleCliente.iniciarBD(context);
        dDetalleCliente.eliminarObjetivosPorCliente(idCliente);
    }

    public NCliente buscar(int idCliente) {
        try {
            if (idCliente != -1) {
                DCliente dc = dCliente.buscar(idCliente);
                if (dc != null) {
                    return new NCliente(dc.getIdCliente(), dc.getNombre(), dc.getApellido(), dc.getCelular());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar al cliente: " + e.getMessage());
        }
        return null;
    }

    public void iniciarBD(Context context) {
        this.context = context;
        this.dCliente.iniciarBD(context);
        if (this.detalleCliente != null) {
            for (DDetalleCliente ddc : detalleCliente) {
                ddc.iniciarBD(context);
            }
        } else {
            this.detalleCliente = new ArrayList<>();
        }
    }

    public DCliente getdCliente() {
        return dCliente;
    }

    public void setdCliente(DCliente dCliente) {
        this.dCliente = dCliente;
    }

    public List<DDetalleCliente> getDetalleCliente() {
        return detalleCliente;
    }

    public void setDetalleCliente(List<DDetalleCliente> detalleCliente) {
        this.detalleCliente = detalleCliente;
    }
}
