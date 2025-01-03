package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DObjetivo {
    private int idObjetivo;
    private String nombre;
    private SQLiteDatabase database;

    public DObjetivo() {
        this(-1, "");
    }

    public DObjetivo(int idObjetivo, String nombre) {
        this.idObjetivo = idObjetivo;
        this.nombre = nombre;
    }

    public boolean insertar(String nombre) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            database.insertOrThrow(AsistenteBD.TABLE_OBJETIVO, null, values);
        });
    }

    public boolean modificar(int idObjetivo, String nombre) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            database.update(AsistenteBD.TABLE_OBJETIVO, values,
                    AsistenteBD.COLUMN_ID_OBJETIVO + " = ?",
                    new String[]{String.valueOf(idObjetivo)});
        });
    }

    public boolean borrar(int idObjetivo) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_OBJETIVO,
                    AsistenteBD.COLUMN_ID_OBJETIVO + " = ?",
                    new String[]{String.valueOf(idObjetivo)});
        });
    }

    public List<DObjetivo> consultar() {
        List<DObjetivo> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_OBJETIVO, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idE = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_OBJETIVO));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                    lista.add(new DObjetivo(idE, nom));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public DObjetivo buscar(int idObjetivo) {
        DObjetivo objetivo = null;
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_OBJETIVO,
                    new String[]{AsistenteBD.COLUMN_ID_OBJETIVO, AsistenteBD.COLUMN_NOMBRE},
                    AsistenteBD.COLUMN_ID_OBJETIVO + " = ?",
                    new String[]{String.valueOf(idObjetivo)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_OBJETIVO));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                objetivo = new DObjetivo(id, nombre);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return objetivo;
    }    

    public int getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void iniciarBD(Context context){
        SQLiteDatabase database = null;
        try {
            AsistenteBD admin = new AsistenteBD(context);
            database = admin.getWritableDatabase();
            this.setDatabase(database);
        } catch (Exception e) {
            System.err.println("Error al iniciar la base de datos: " + e.getMessage());
            if (database != null && database.isOpen()) {
                database.close(); // Cerrar la base de datos si ocurrió un error
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
