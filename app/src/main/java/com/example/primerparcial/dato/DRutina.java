package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DRutina {
    private int idRutina;
    private String nombre;
    private SQLiteDatabase database;

    public DRutina() {
        this.idRutina = -1;
        this.nombre = "";
    }

    public DRutina(int idRutina, String nombre) {
        this.idRutina = idRutina;
        this.nombre = nombre;
    }

    public boolean insertar(String nombre) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            long idLong = database.insertOrThrow(AsistenteBD.TABLE_RUTINA, null, values);
            int id = (int) idLong;
            setIdRutina(id);
            setNombre(nombre);
        });
    }

    public boolean modificar(int idRutina, String nombre) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_NOMBRE, nombre);
            database.update(AsistenteBD.TABLE_RUTINA, values,
                    AsistenteBD.COLUMN_ID_RUTINA + " = ?",
                    new String[]{String.valueOf(idRutina)});
        });
    }

    public boolean borrar(int idRutina) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_RUTINA,
                    AsistenteBD.COLUMN_ID_RUTINA + " = ?",
                    new String[]{String.valueOf(idRutina)});
        });
    }

    public List<DRutina> consultar() {
        List<DRutina> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_RUTINA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idE = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_RUTINA));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                    lista.add(new DRutina(idE, nom));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public DRutina buscar(int idRutina) {
        DRutina rutina = null;
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_RUTINA,
                    new String[]{AsistenteBD.COLUMN_ID_RUTINA, AsistenteBD.COLUMN_NOMBRE},
                    AsistenteBD.COLUMN_ID_RUTINA + " = ?",
                    new String[]{String.valueOf(idRutina)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_RUTINA));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_NOMBRE));
                rutina = new DRutina(id, nombre);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rutina;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
