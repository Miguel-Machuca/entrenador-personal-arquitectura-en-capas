package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DCronograma {
    private int idCronograma;
    private int idCliente;
    private int idRutina;
    private String fecha;
    private SQLiteDatabase database;

    public DCronograma() {
        this(-1, "", -1, -1);
    }

    public DCronograma(int idCronograma, String fecha, int idCliente, int idRutina) {
        this.idCronograma = idCronograma;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idRutina = idRutina;
    }

    public boolean insertar(String fecha, int idCliente, int idRutina) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_FECHA, fecha);
            values.put(AsistenteBD.COLUMN_ID_CLIENTE, idCliente);
            values.put(AsistenteBD.COLUMN_ID_RUTINA, idRutina);
            database.insertOrThrow(AsistenteBD.TABLE_CRONOGRAMA, null, values);
        });
    }

    public boolean modificar(int idCronograma, String fecha, int idCliente, int idRutina) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_FECHA, fecha); // Convierte Date a String si es necesario
            values.put(AsistenteBD.COLUMN_ID_CLIENTE, idCliente);
            values.put(AsistenteBD.COLUMN_ID_RUTINA, idRutina);
            database.update(AsistenteBD.TABLE_CRONOGRAMA, values,
                    AsistenteBD.COLUMN_ID_CRONOGRAMA + " = ?",
                    new String[]{String.valueOf(idCronograma)});
        });
    }

    public boolean borrar(int idCronograma) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_CRONOGRAMA,
                    AsistenteBD.COLUMN_ID_CRONOGRAMA + " = ?",
                    new String[]{String.valueOf(idCronograma)});
        });
    }

    public List<DCronograma> consultar() {
        List<DCronograma> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_CRONOGRAMA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idCronograma = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CRONOGRAMA));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_FECHA));
                    int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CLIENTE));
                    int idRutina = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_RUTINA));

                    //Date fechaParsed = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);

                    lista.add(new DCronograma(idCronograma, fecha, idCliente, idRutina));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public DCronograma buscar(int idCronograma) {
        DCronograma cronograma = null;
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_CRONOGRAMA,
                    new String[]{AsistenteBD.COLUMN_ID_CRONOGRAMA, AsistenteBD.COLUMN_FECHA, AsistenteBD.COLUMN_ID_CLIENTE, AsistenteBD.COLUMN_ID_RUTINA},
                    AsistenteBD.COLUMN_ID_CRONOGRAMA + " = ?",
                    new String[]{String.valueOf(idCronograma)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CRONOGRAMA));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_FECHA));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_CLIENTE));
                int idRutina = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_RUTINA));

                //Date fechaParsed = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
                cronograma = new DCronograma(id, fecha, idCliente, idRutina);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cronograma;
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

    public int getIdCronograma() {
        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {
        this.idCronograma = idCronograma;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
}
