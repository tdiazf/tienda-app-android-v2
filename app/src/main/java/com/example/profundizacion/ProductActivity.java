package com.example.profundizacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.profundizacion.db.DbProducts;

public class ProductActivity extends AppCompatActivity {

    EditText name_input, descripcion_input, precio_input, cantidad_input;
    Button agregar_actualizar_btn;
    DbProducts dbProducts;
    int productoId = 0; // Variable para almacenar el ID del producto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_add_edit);

        dbProducts = new DbProducts(ProductActivity.this);

        name_input = findViewById(R.id.product_name);
        descripcion_input = findViewById(R.id.descripcion_input); // Nuevo EditText para la descripción
        precio_input = findViewById(R.id.precio_input);
        cantidad_input = findViewById(R.id.cantidad_input);
        agregar_actualizar_btn = findViewById(R.id.agregar_editar_btn);

        // Obtener el ID del producto del Intent
        Intent intent = getIntent();
        productoId = intent.getIntExtra("PRODUCT_ID", 0);

        agregar_actualizar_btn.setOnClickListener(view -> {
            String nombre = name_input.getText().toString();
            String descripcion = descripcion_input.getText().toString(); // Recoger el dato de la descripción
            String precio = precio_input.getText().toString();
            String cantidad = cantidad_input.getText().toString();

            if (cantidad.matches("\\d+")) { // Comprueba si la cadena contiene solo dígitos
                if (productoId == 0) {
                    // Si productoId es 0, estamos agregando un nuevo producto
                    long id = dbProducts.insertarProducto(nombre, descripcion, precio, Integer.parseInt(cantidad)); // Añadir la descripción al método insertarProducto
                    if (id > 0) {
                        Toast.makeText(ProductActivity.this, "PRODUCTO AGREGADO", Toast.LENGTH_LONG).show();
                        limpiar();
                        Intent intentHome = new Intent(ProductActivity.this, HomeActivity.class);
                        startActivity(intentHome); // Iniciar la actividad
                    } else {
                        Toast.makeText(ProductActivity.this, "ERROR AL AGREGAR PRODUCTO", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Si productoId no es 0, estamos actualizando un producto existente
                    boolean isUpdated = dbProducts.actualizarProducto(nombre, descripcion, precio, Integer.parseInt(cantidad), productoId); // Añadir la descripción al método actualizarProducto
                    if (isUpdated) {
                        Toast.makeText(ProductActivity.this, "PRODUCTO ACTUALIZADO", Toast.LENGTH_LONG).show();
                        limpiar();
                        Intent intentHome = new Intent(ProductActivity.this, HomeActivity.class);
                        startActivity(intentHome); // Iniciar la actividad
                    } else {
                        Toast.makeText(ProductActivity.this, "ERROR AL ACTUALIZAR PRODUCTO", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(ProductActivity.this, "ERROR: La cantidad debe ser un número entero", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void limpiar() {
        name_input.setText("");
        descripcion_input.setText(""); // Limpiar el EditText de la descripción
        precio_input.setText("");
        cantidad_input.setText("");
        productoId = 0; // Restablecer productoId a 0 después de agregar o actualizar
    }
}