package com.example.profundizacion.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NOMBRE = "agenda.db";
    public static final String TABLE_CONTACTOS = "t_contactos";
    public static final String TABLE_PRODUCTOS = "t_productos"; // Nueva tabla para productos

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONTACTOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "telefono TEXT NOT NULL," +
                "mail TEXT NOT NULL," +
                "password TEXT)");

        // Crear la nueva tabla para productos
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PRODUCTOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT," +
                "cantidad INT," +
                "precio INT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Verificar si la tabla t_productos existe antes de intentar eliminarla
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='t_productos'", null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getInt(0) > 0) {
                // Si la tabla t_productos existe, eliminarla
                sqLiteDatabase.execSQL("DROP TABLE " + TABLE_PRODUCTOS);
            }
            cursor.close();
        }

        // Eliminar la tabla t_contactos
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CONTACTOS);

        // Crear las tablas nuevamente
        onCreate(sqLiteDatabase);
    }
}