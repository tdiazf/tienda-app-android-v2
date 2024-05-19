package com.example.profundizacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.profundizacion.db.DbContactos;

public class Register extends CreateDb {

    EditText name_input, telefono_input, mail_input, password_input;
    Button register_btn;
    DbContactos dbContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbContactos = new DbContactos(Register.this);

        name_input = findViewById(R.id.name_input);
        telefono_input = findViewById(R.id.telefono_input);
        mail_input = findViewById(R.id.mail_input);
        password_input = findViewById(R.id.password_input);
        register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(view -> {
            String name = name_input.getText().toString();
            String telefono = telefono_input.getText().toString();
            String mail = mail_input.getText().toString();
            String password = password_input.getText().toString();

            if (dbContactos.validateUser(mail, password)) {
                // Si el correo y la contraseña coinciden, actualizamos los datos del usuario
                boolean isUpdated = dbContactos.updateUser(name, telefono, mail);
                if (isUpdated) {
                    Toast.makeText(Register.this, "DATOS ACTUALIZADOS", Toast.LENGTH_LONG).show();
                    limpiar();

                    // Redireccionar al usuario a la vista de login
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, "ERROR AL ACTUALIZAR DATOS", Toast.LENGTH_LONG).show();
                }
            } else {
                // Si el correo no existe en la base de datos, intentamos insertar un nuevo usuario
                long id = dbContactos.insertarUsuario(name, telefono, mail, password);
                if (id > 0) {
                    Toast.makeText(Register.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                    limpiar();

                    // Redireccionar al usuario a la vista de login
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void limpiar() {
        name_input.setText("");
        mail_input.setText("");
        telefono_input.setText("");
        mail_input.setText("");
        password_input.setText("");
    }


}
