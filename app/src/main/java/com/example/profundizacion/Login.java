package com.example.profundizacion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profundizacion.db.DbContactos;

public class Login extends AppCompatActivity {

    EditText username_input, password_input;
    Button login_btn;
    Button linkRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);

        login_btn = findViewById(R.id.login_btn);
        linkRegister = findViewById(R.id.linkRegister);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DbContactos dbContactos = new DbContactos(Login.this);
                    Boolean isValidLogin = dbContactos.validateUser(username_input.getText().toString(), password_input.getText().toString());

                    if (isValidLogin != null && isValidLogin) {
                        Toast.makeText(Login.this, "LOGIN SUCCESSFUL", Toast.LENGTH_LONG).show();

                        String userName = null;
                        String userEmail = null;

                        Cursor cursor = dbContactos.getUserInfo(username_input.getText().toString());
                        if (cursor.moveToFirst()) {
                            int nameColumnIndex = cursor.getColumnIndex("name");
                            int mailColumnIndex = cursor.getColumnIndex("mail");
                            if (nameColumnIndex != -1) {
                                userName = cursor.getString(nameColumnIndex);
                            }
                            if (mailColumnIndex != -1) {
                                userEmail = cursor.getString(mailColumnIndex);
                            }
                        }
                        cursor.close();

                        // Guarda el correo electrónico y el nombre de usuario en las SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", userEmail);
                        editor.putString("userName", userName);
                        editor.apply();

                        // Imprime el correo electrónico y el nombre de usuario para depuración
                        Log.d("LoginActivity", "Saved user email: " + userEmail);
                        Log.d("LoginActivity", "Saved user name: " + userName);

                        // Iniciar HomeActivity
                        Intent intent = new Intent(Login.this, HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Login.this, "CONTRASEÑA EQUIVOCADA", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Login.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }


}