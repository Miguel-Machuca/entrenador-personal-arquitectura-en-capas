package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DEjercicio {
    private int idEjercicio;
    private String nombre;
    private String urlImagen;
    private SQLiteDatabase database;

    public DEjercicio() {
        this(-1, "", "");
    }

    public DEjercicio(int idEjercicio, String nombre, String urlImagen) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.urlImagen = urlImagen;
    }

    public boolean insertar(String nombre, String urlImagen) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            values.put(AsistenteBD.COLUMN_URLIMAGEN, urlImagen);
            database.insertOrThrow(AsistenteBD.TABLE_EJERCICIO, null, values);
        });
    }

    public boolean modificar(int idEjercicio, String nombre, String urlImagen) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            values.put(AsistenteBD.COLUMN_URLIMAGEN, urlImagen);
            database.update(AsistenteBD.TABLE_EJERCICIO, values,
                    AsistenteBD.COLUMN_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idEjercicio)});
        });
    }

    public boolean borrar(int idEjercicio) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_EJERCICIO,
                    AsistenteBD.COLUMN_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idEjercicio)});
        });
    }

    public List<DEjercicio> consultar() {
        List<DEjercicio> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_EJERCICIO, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idE = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_EJERCICIO));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_URLIMAGEN));
                    lista.add(new DEjercicio(idE, nom, url));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public DEjercicio buscar(int idEjercicio) {
        DEjercicio ejercicio = null;
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_EJERCICIO,
                    new String[]{AsistenteBD.COLUMN_ID_EJERCICIO, AsistenteBD.COLUMN_NOMBRE, AsistenteBD.COLUMN_URLIMAGEN},
                    AsistenteBD.COLUMN_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idEjercicio)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_EJERCICIO));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                String urlImagen = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_URLIMAGEN));
                ejercicio = new DEjercicio(id, nombre, urlImagen);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ejercicio;
    }

    // **** Getters y Setters **** //
    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
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
