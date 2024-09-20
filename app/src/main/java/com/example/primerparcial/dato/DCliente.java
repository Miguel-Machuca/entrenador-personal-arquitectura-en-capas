package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DCliente {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String celular;
    private SQLiteDatabase database;

    public DCliente() {
        this(-1, "", "", "");
    }

    public DCliente(int id, String nombre, String apellido, String celular) {
        this.idCliente = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
    }

    public boolean insertar(String nombre, String apellido, String celular){
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            values.put(AsistenteBD.COLUMN_APELLIDO, apellido);
            values.put(AsistenteBD.COLUMN_CELULAR, celular);
            long idLong = database.insertOrThrow(AsistenteBD.TABLE_CLIENTE, null, values);
            int id = (int) idLong;
            setIdCliente(id);
            setNombre(nombre);
            setApellido(apellido);
            setCelular(celular);
        });
    }

    public boolean modificar(int idCliente, String nombre, String apellido, String celular){
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            values.put(AsistenteBD.COLUMN_APELLIDO, apellido);
            values.put(AsistenteBD.COLUMN_CELULAR, celular);
            database.update(AsistenteBD.TABLE_CLIENTE, values,
                    AsistenteBD.COLUMN_ID_CLIENTE + " = ?",
                    new String[]{String.valueOf(idCliente)});
        });
    }

    public boolean borrar(int idCliente){
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_CLIENTE,
                    AsistenteBD.COLUMN_ID_CLIENTE + " = ?",
                    new String[]{String.valueOf(idCliente)});
        });
    }

    public List<DCliente> consultar(){
        List<DCliente> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_CLIENTE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idC = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CLIENTE));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                    String ape = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_APELLIDO));
                    String cel = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CELULAR));
                    lista.add(new DCliente(idC, nom, ape, cel));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public DCliente buscar(int idCliente){
        DCliente cliente = null;
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_CLIENTE,
                    new String[]{AsistenteBD.COLUMN_ID_CLIENTE, AsistenteBD.COLUMN_NOMBRE, AsistenteBD.COLUMN_APELLIDO, AsistenteBD.COLUMN_CELULAR},
                    AsistenteBD.COLUMN_ID_CLIENTE + " = ?",
                    new String[]{String.valueOf(idCliente)},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CLIENTE));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_APELLIDO));
                String celular = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CELULAR));
                cliente = new DCliente(id, nombre, apellido, celular);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void iniciarBD(Context context) {
        SQLiteDatabase database = null;
        try {
            AsistenteBD admin = new AsistenteBD(context);
            database = admin.getWritableDatabase();
            this.setDatabase(database);
        } catch (Exception e) {
            System.err.println("Error al iniciar la base de datos: " + e.getMessage());
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    private boolean executeTransaction(Runnable operation) {
        database.beginTransaction();
        try {
            operation.run();
            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            database.endTransaction();
        }
    }
}
