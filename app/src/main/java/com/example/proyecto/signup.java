package com.example.proyecto;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signup extends AppCompatActivity {

    private EditText user, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = findViewById(R.id.etUsuarioSignup);
        password = findViewById(R.id.etPasswordSignup);
    }

    public void registrar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        String usuario = user.getText().toString();
        String contrasenna = password.getText().toString();

        if (!usuario.isEmpty() && !contrasenna.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("user", usuario);
            registro.put("password", contrasenna);

            database.insert("users", null, registro);

            database.close();

            user.setText("");
            user.setText("");

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ingresa los campos requeridos", Toast.LENGTH_SHORT).show();
        }
    }

    public void ingresar(View view) {
        Intent ingresar = new Intent(this, Login.class);
        startActivity(ingresar);
    }
}