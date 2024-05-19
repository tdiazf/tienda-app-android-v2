package com.example.profundizacion;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.profundizacion.db.DbHelper;

import java.io.File;

public class CreateDb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File dbFile = getDatabasePath("agenda.db");
        if (!dbFile.exists()) {
            createDatabase();
        }
    }

    private void createDatabase() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            Toast.makeText(this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_LONG).show();
        }
    }
}