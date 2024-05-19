package com.example.profundizacion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.profundizacion.Login;
import com.example.profundizacion.Register;

public class DbContactos extends DbHelper {

    Context context;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarUsuario(String name, String telefono, String mail, String password) {
        long id = 0;

        try {
            SQLiteDatabase db = null;
            DbHelper dbHelper = new DbHelper(context);
            db = dbHelper.getWritableDatabase();

            // Verificar si el correo electrónico ya existe
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " WHERE mail=?", new String[]{mail});
            if (cursor.moveToFirst()) {
                // Si el correo electrónico ya existe, mostrar un mensaje de error y no insertar un nuevo usuario
                Toast.makeText(context, "USUARIO YA EXISTENTE.", Toast.LENGTH_LONG).show();
                id = -1;
            } else {
                // Si el correo electrónico no existe, insertar un nuevo usuario
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("telefono", telefono);
                values.put("mail", mail);
                values.put("password", password);

                id = db.insert(TABLE_CONTACTOS, null, values);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    public boolean validateUser(String username, String password) {
        boolean isValid = false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " WHERE mail=? AND password=?", new String[]{username, password});

        if (cursor.getCount() > 0) {
            isValid = true;
        }

        cursor.close();
        db.close();

        return isValid;
    }

    public Cursor getUserInfo(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " WHERE mail=?", new String[]{email});
        return cursor;
    }

    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTOS, "mail = ?", new String[]{email}) > 0;
    }

    public boolean updateUser(String name, String telefono, String mail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("telefono", telefono);

        // Actualizar la fila en la base de datos donde el correo electrónico coincide
        int rows = db.update(TABLE_CONTACTOS, values, "mail = ?", new String[]{mail});

        // Si rows es mayor que 0, entonces se actualizó al menos una fila
        return rows > 0;
    }
}
