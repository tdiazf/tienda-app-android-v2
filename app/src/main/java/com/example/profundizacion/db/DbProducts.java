package com.example.profundizacion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.profundizacion.Product; // Asegúrate de que esta es la ruta correcta a tu clase Product

import java.util.ArrayList; // Importa la clase ArrayList
import java.util.List; // Importa la clase List

public class DbProducts extends DbHelper {

    Context context;

    public DbProducts(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarProducto(String nombre, String descripcion, String precio, int cantidad) {
        long id = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("precio", precio);
        values.put("cantidad", cantidad);

        id = db.insert(TABLE_PRODUCTOS, null, values);

        return id;
    }

    public boolean actualizarProducto(String nombre, String descripcion, String precio, int cantidad, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("precio", precio);
        values.put("cantidad", cantidad);

        int rows = db.update(TABLE_PRODUCTOS, values, "id = ?", new String[]{String.valueOf(id)});

        return rows > 0;
    }

    public List<Product> obtenerProductos() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("nombre");
                int priceIndex = cursor.getColumnIndex("precio");
                int quantityIndex = cursor.getColumnIndex("cantidad");

                if (idIndex != -1 && nameIndex != -1 && priceIndex != -1 && quantityIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    double price = cursor.getDouble(priceIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    productList.add(new Product(id, name, price, quantity));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    public boolean eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTOS, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}