package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DDetalleCliente {
    private int idCliente;
    private int idObjetivo;
    private String descripcion;
    private SQLiteDatabase database;

    public DDetalleCliente() {
        this(-1, -1, "");
    }

    public DDetalleCliente(int idCliente, int idObjetivo, String descripcion) {
        this.idCliente = idCliente;
        this.idObjetivo = idObjetivo;
        this.descripcion = descripcion;
    }

    public boolean insertar(int idCliente, int idObjetivo, String descripcion){
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_ID_CLIENTE, idCliente);
            values.put(AsistenteBD.COLUMN_ID_OBJETIVO, idObjetivo);
            values.put(AsistenteBD.COLUMN_DESCRIPCION, descripcion);
            database.insertOrThrow(AsistenteBD.TABLE_DETALLECLIENTE, null, values);
        });
    }

    public boolean modificar(int idCliente, int idObjetivo, String descripcion){
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_DESCRIPCION, descripcion);
            database.update(AsistenteBD.TABLE_DETALLECLIENTE, values,
                    AsistenteBD.COLUMN_ID_CLIENTE + " = ? AND " + AsistenteBD.COLUMN_ID_OBJETIVO + " = ?",
                    new String[]{String.valueOf(idCliente), String.valueOf(idObjetivo)});
        });
    }

    public boolean borrar(int idCliente, int idObjetivo) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_DETALLECLIENTE,
                    AsistenteBD.COLUMN_ID_CLIENTE + " = ? AND " + AsistenteBD.COLUMN_ID_OBJETIVO + " = ?",
                    new String[]{String.valueOf(idCliente), String.valueOf(idObjetivo)});
        });
    }

    public List<DDetalleCliente> listarObjetivo(int idCliente) {
        List<DDetalleCliente> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selection = AsistenteBD.COLUMN_ID_CLIENTE + " = ?";
            String[] selectionArgs = {String.valueOf(idCliente)};

            cursor = database.query(
                    AsistenteBD.TABLE_DETALLECLIENTE,  // Nombre de la tabla
                    null,                             // Columnas (null = todas)
                    selection,                        // WHERE cláusula
                    selectionArgs,                    // Argumentos de selección
                    null,                             // GROUP BY
                    null,                             // HAVING
                    null                              // ORDER BY
            );

            if (cursor.moveToFirst()) {
                do {
                    int idC = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CLIENTE));
                    int idO = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_OBJETIVO));
                    String des = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_DESCRIPCION));

                    lista.add(new DDetalleCliente(idC, idO, des));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public boolean eliminarObjetivosPorCliente(int idCliente) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_DETALLECLIENTE,
                    AsistenteBD.COLUMN_ID_CLIENTE + " = ?",
                    new String[]{String.valueOf(idCliente)});
        });
    }

    public boolean agregarObjetivoACliente(int idCliente, int idObjetivo, String descripcion) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_ID_CLIENTE, idCliente);
            values.put(AsistenteBD.COLUMN_ID_OBJETIVO, idObjetivo);
            values.put(AsistenteBD.COLUMN_DESCRIPCION, descripcion);
            database.insertOrThrow(AsistenteBD.TABLE_DETALLECLIENTE, null, values);
        });
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
}
