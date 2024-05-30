package com.example.profundizacion.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profundizacion.Product;
import com.example.profundizacion.ProductActivity;
import com.example.profundizacion.R;
import com.example.profundizacion.db.DbProducts;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Asigna los datos del producto a los campos de texto


        holder.name.setText(product.getName());
        holder.price.setText("PRECIO: $ "  + String.valueOf(product.getPrice()));
        holder.quantity.setText("UNIDADES: " +String.valueOf(product.getQuantity()));

        // Resto del código...


        // Agrega un oyente de clic al botón "Editar"
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la posición actual del elemento
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    // Si el elemento ya no está en el adaptador, retorna
                    return;
                }

                // Obtiene el producto en la posición actual
                Product currentProduct = productList.get(currentPosition);

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("PRODUCT_ID", currentProduct.getId());
                context.startActivity(intent);
            }
        });

        // Agrega un oyente de clic al botón "Eliminar"
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la posición actual del elemento
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    // Si el elemento ya no está en el adaptador, retorna
                    return;
                }

                // Obtiene el producto en la posición actual
                Product currentProduct = productList.get(currentPosition);

                // Crea una instancia de DbProducts
                DbProducts dbProducts = new DbProducts(context);
                // Llama a la función para eliminar el producto de la base de datos
                boolean isDeleted = dbProducts.eliminarProducto(currentProduct.getId());
                if (isDeleted) {
                    // Actualiza la lista de productos en el adaptador
                    productList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                } else {
                    // Muestra un mensaje de error si el producto no se pudo eliminar
                    Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        Button deleteButton; // Botón de eliminar
        Button editButton; // Botón de editar

        public ProductViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.product_name);
            price = view.findViewById(R.id.product_price);
            quantity = view.findViewById(R.id.product_cant);
            deleteButton = view.findViewById(R.id.delete_btn); // Inicializa el botón de eliminar
            editButton = view.findViewById(R.id.edit_btn); // Inicializa el botón de editar
        }
    }
}