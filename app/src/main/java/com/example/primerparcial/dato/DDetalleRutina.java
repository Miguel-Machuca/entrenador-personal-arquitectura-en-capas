package com.example.primerparcial.dato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DDetalleRutina {
    private int idRutina;
    private int idEjercicio;
    private int cantidadSerie;
    private int cantidadRepeticion;
    private int duracionReposo;
    private SQLiteDatabase database;

    public DDetalleRutina() {
        this(-1, -1, -1, -1, -1);
    }

    public DDetalleRutina(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int duracionReposo) {
        this.idRutina = idRutina;
        this.idEjercicio = idEjercicio;
        this.cantidadSerie = cantidadSerie;
        this.cantidadRepeticion = cantidadRepeticion;
        this.duracionReposo = duracionReposo;
    }

    public boolean insertar(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int duracionReposo) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_ID_RUTINA, idRutina);
            values.put(AsistenteBD.COLUMN_ID_EJERCICIO, idEjercicio);
            values.put(AsistenteBD.COLUMN_CANTIDADSERIE, cantidadSerie);
            values.put(AsistenteBD.COLUMN_CANTIDADREPETICION, cantidadRepeticion);
            values.put(AsistenteBD.COLUMN_DURACIONREPOSO, duracionReposo);
            database.insertOrThrow(AsistenteBD.TABLE_DETALLERUTINA, null, values);
        });
    }

    public boolean modificar(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int duracionReposo) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_CANTIDADSERIE, cantidadSerie);
            values.put(AsistenteBD.COLUMN_CANTIDADREPETICION, cantidadRepeticion);
            values.put(AsistenteBD.COLUMN_DURACIONREPOSO, duracionReposo);
            database.update(AsistenteBD.TABLE_DETALLERUTINA, values,
                    AsistenteBD.COLUMN_ID_RUTINA + " = ? AND " + AsistenteBD.COLUMN_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idRutina), String.valueOf(idEjercicio)});
        });
    }

    public boolean borrar(int idRutina, int idEjercicio) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_DETALLERUTINA,
                    AsistenteBD.COLUMN_ID_RUTINA + " = ? AND " + AsistenteBD.COLUMN_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idRutina), String.valueOf(idEjercicio)});
        });
    }

    /*public List<DDetalleRutina> consultar() {
        List<DDetalleRutina> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_DETALLERUTINA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idR = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_RUTINA));
                    int idE = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_EJERCICIO));
                    int ser = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CANTIDADSERIE));
                    int rep = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CANTIDADREPETICION));
                    int des = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_DURACIONREPOSO));
                    lista.add(new DDetalleRutina(idR, idE, ser, rep, des));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }*/

    public List<DDetalleRutina> listarEjercicio(int idRutina) {
        List<DDetalleRutina> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            String selection = AsistenteBD.COLUMN_ID_RUTINA + " = ?";
            String[] selectionArgs = {String.valueOf(idRutina)};

            cursor = database.query(
                    AsistenteBD.TABLE_DETALLERUTINA,  // Nombre de la tabla
                    null,                             // Columnas (null = todas)
                    selection,                        // WHERE cláusula
                    selectionArgs,                    // Argumentos de selección
                    null,                             // GROUP BY
                    null,                             // HAVING
                    null                              // ORDER BY
            );

            if (cursor.moveToFirst()) {
                do {
                    int idR = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_RUTINA));
                    int idE = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_ID_EJERCICIO));
                    int ser = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CANTIDADSERIE));
                    int rep = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CANTIDADREPETICION));
                    int des = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_DURACIONREPOSO));

                    lista.add(new DDetalleRutina(idR, idE, ser, rep, des));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public boolean eliminarEjerciciosPorRutina(int idRutina) {
        return executeTransaction(() -> {
            database.delete(AsistenteBD.TABLE_DETALLERUTINA,
                    AsistenteBD.COLUMN_ID_RUTINA + " = ?",
                    new String[]{String.valueOf(idRutina)});
        });
    }

    public boolean agregarEjercicioARutina(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int reposoSerie) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(AsistenteBD.COLUMN_ID_RUTINA, idRutina);
            values.put(AsistenteBD.COLUMN_ID_EJERCICIO, idEjercicio);
            values.put(AsistenteBD.COLUMN_CANTIDADSERIE, cantidadSerie);
            values.put(AsistenteBD.COLUMN_CANTIDADREPETICION, cantidadRepeticion);
            values.put(AsistenteBD.COLUMN_DURACIONREPOSO, reposoSerie);
            database.insertOrThrow(AsistenteBD.TABLE_DETALLERUTINA, null, values);
        });
    }

/*    public DDetalleRutina buscar(int idRutina, int idEjercicio) {
        DDetalleRutina detalleRutina = null;
        Cursor cursor = null;
        try {
            cursor = database.query(AsistenteBD.TABLE_DETALLERUTINA,
                    new String[]{
                            AsistenteBD.COLUMN_ID_RUTINA,
                            AsistenteBD.COLUMN_ID_EJERCICIO,
                            AsistenteBD.COLUMN_CANTIDADSERIE,
                            AsistenteBD.COLUMN_CANTIDADREPETICION,
                            AsistenteBD.COLUMN_DURACIONREPOSO
                    },
                    AsistenteBD.COLUMN_ID_RUTINA + " = ? AND " + AsistenteBD.COLUMN_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idRutina), String.valueOf(idEjercicio)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int cantidadSerie = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CANTIDADSERIE));
                int cantidadRepeticion = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_CANTIDADREPETICION));
                int duracionReposo = cursor.getInt(cursor.getColumnIndexOrThrow(AsistenteBD.COLUMN_DURACIONREPOSO));

                detalleRutina = new DDetalleRutina(idRutina, idEjercicio, cantidadSerie, cantidadRepeticion, duracionReposo);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return detalleRutina;
    }*/

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public int getCantidadSerie() {
        return cantidadSerie;
    }

    public void setCantidadSerie(int cantidadSerie) {
        this.cantidadSerie = cantidadSerie;
    }

    public int getCantidadRepeticion() {
        return cantidadRepeticion;
    }

    public void setCantidadRepeticion(int cantidadRepeticion) {
        this.cantidadRepeticion = cantidadRepeticion;
    }

    public int getDuracionReposo() {
        return duracionReposo;
    }

    public void setDuracionReposo(int duracionReposo) {
        this.duracionReposo = duracionReposo;
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
