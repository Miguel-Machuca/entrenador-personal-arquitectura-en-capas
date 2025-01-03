package com.example.primerparcial.dato;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AsistenteBD extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "primer_parcial.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_EJERCICIO = "ejercicio";
    public static final String TABLE_RUTINA = "rutina";
    public static final String TABLE_DETALLERUTINA = "detalle_rutina";
    public static final String TABLE_CRONOGRAMA = "cronograma";
    public static final String TABLE_CLIENTE = "cliente";
    public static final String TABLE_OBJETIVO = "objetivo";
    public static final String TABLE_DETALLECLIENTE = "detalle_cliente";

    public static final String COLUMN_ID_EJERCICIO = "id_ejercicio";
    public static final String COLUMN_ID_RUTINA = "id_rutina";
    public static final String COLUMN_ID_CRONOGRAMA = "id_cronograma";
    public static final String COLUMN_ID_CLIENTE = "id_cliente";
    public static final String COLUMN_ID_OBJETIVO = "id_objetivo";
    public static final String COLUMN_ID_DETALLECLIENTE = "id_detalle_cliente";

    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDO = "apellido";
    public static final String COLUMN_CELULAR = "celular";
    public static final String COLUMN_URLIMAGEN = "url_imagen";
    public static final String COLUMN_CANTIDADSERIE = "cantidad_serie";
    public static final String COLUMN_CANTIDADREPETICION = "cantidad_repeticion";
    public static final String COLUMN_DURACIONREPOSO = "duracion_reposo";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static AsistenteBD instance;

    public static synchronized AsistenteBD getInstance(Context context) {
        if (instance == null) {
            instance = new AsistenteBD(context.getApplicationContext());
        }
        return instance;
    }

    public AsistenteBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTableEjercicio());
        db.execSQL(getCreateTableRutina());
        db.execSQL(getCreateTableDetalleRutina());
        db.execSQL(getCreateTableCronograma());
        db.execSQL(getCreateTableCliente());
        db.execSQL(getCreateTableObjetivo());
        db.execSQL(getCreateTableDetalleCliente());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    private String getCreateTableEjercicio() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_EJERCICIO + "(" +
                COLUMN_ID_EJERCICIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_URLIMAGEN + " TEXT);";
    }

    private String getCreateTableRutina() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_RUTINA + "(" +
                COLUMN_ID_RUTINA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL);";
    }

    private String getCreateTableDetalleRutina() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_DETALLERUTINA + " (" +
                COLUMN_ID_RUTINA + " INTEGER NOT NULL, " +
                COLUMN_ID_EJERCICIO + " INTEGER NOT NULL, " +
                COLUMN_CANTIDADSERIE + " INTEGER NOT NULL, " +
                COLUMN_CANTIDADREPETICION + " INTEGER NOT NULL, " +
                COLUMN_DURACIONREPOSO + " INTEGER NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_ID_RUTINA + ", " + COLUMN_ID_EJERCICIO + "), " +
                "FOREIGN KEY (" + COLUMN_ID_RUTINA + ") REFERENCES " + TABLE_RUTINA + "(" + COLUMN_ID_RUTINA + "), " +
                "FOREIGN KEY (" + COLUMN_ID_EJERCICIO + ") REFERENCES " + TABLE_EJERCICIO + "(" + COLUMN_ID_EJERCICIO + "));";
    }

    private String getCreateTableCronograma() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_CRONOGRAMA + "(" +
                COLUMN_ID_CRONOGRAMA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FECHA + " DATE, " +
                COLUMN_ID_CLIENTE + " INTEGER NOT NULL, " +
                COLUMN_ID_RUTINA + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_ID_CLIENTE + ") REFERENCES " + TABLE_CLIENTE + "(" + COLUMN_ID_CLIENTE + "), " +
                "FOREIGN KEY (" + COLUMN_ID_RUTINA + ") REFERENCES " + TABLE_RUTINA + "(" + COLUMN_ID_RUTINA + "));";
    }

    private String getCreateTableCliente() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTE + "(" +
                COLUMN_ID_CLIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_APELLIDO + " TEXT NOT NULL, " +
                COLUMN_CELULAR + " TEXT NOT NULL);";
    }

    private String getCreateTableObjetivo() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_OBJETIVO + "(" +
                COLUMN_ID_OBJETIVO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL);";
    }

    private String getCreateTableDetalleCliente() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_DETALLECLIENTE + "(" +
                COLUMN_ID_CLIENTE + " INTEGER NOT NULL, " +
                COLUMN_ID_OBJETIVO + " INTEGER NOT NULL, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_ID_CLIENTE + ", " + COLUMN_ID_OBJETIVO + "), " +
                "FOREIGN KEY (" + COLUMN_ID_CLIENTE + ") REFERENCES " + TABLE_CLIENTE + "(" + COLUMN_ID_CLIENTE + "), " +
                "FOREIGN KEY (" + COLUMN_ID_OBJETIVO + ") REFERENCES " + TABLE_OBJETIVO + "(" + COLUMN_ID_OBJETIVO + "));";
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EJERCICIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUTINA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLERUTINA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRONOGRAMA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJETIVO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLECLIENTE);
    }
}
