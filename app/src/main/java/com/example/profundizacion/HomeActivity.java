package com.example.profundizacion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profundizacion.RecyclerView.ProductAdapter;
import com.example.profundizacion.db.DbProducts;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // Declaración de variables para los botones y la lista de productos
    Button agregar_btn, mapButton;
    RecyclerView productos_list;
    DbProducts dbProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicialización de la base de datos de productos
        dbProducts = new DbProducts(HomeActivity.this);

        // Inicialización del botón de agregar
        agregar_btn = findViewById(R.id.agregar_btn);

        // Inicialización del botón del mapa
        mapButton = findViewById(R.id.map_btn);

        // Inicialización del RecyclerView
        productos_list = findViewById(R.id.productos_list);
        productos_list.setLayoutManager(new LinearLayoutManager(this));

        // Obtención de los productos de la base de datos
        List<Product> productos = dbProducts.obtenerProductos();

        // Creación de un Adapter para los productos
        ProductAdapter adapter = new ProductAdapter(productos, this);

        // Configuración del RecyclerView con el Adapter
        productos_list.setAdapter(adapter);

        // Recupera el correo electrónico del usuario de las SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String retrievedEmail = sharedPreferences.getString("email", "");
        String userName = sharedPreferences.getString("userName", "");

// Imprime el nombre de usuario para depuración
        Log.d("HomeActivity", "Retrieved user name: " + userName);
        // Imprime el correo electrónico del usuario para depuración
        Log.d("HomeActivity", "Retrieved user email: " + retrievedEmail);

        // Configuración del OnClickListener para el botón de agregar
        agregar_btn.setOnClickListener(view -> {
            // Navegación a ProductActivity cuando se hace clic en agregar_btn
            Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        // Configuración del OnClickListener para el botón del mapa
        mapButton.setOnClickListener(view -> {
            // Navegación a MapActivity cuando se hace clic en mapButton
            Intent intent = new Intent(HomeActivity.this, MapActivity.class);

            // Coloca el correo electrónico y el nombre del usuario en el Intent
            intent.putExtra("email", retrievedEmail);

            startActivity(intent);
        });
    }
}