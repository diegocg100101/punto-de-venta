package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {

    private EditText user, password;
    private Button huella;
    private Executor executor;
    private androidx.biometric.BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = findViewById(R.id.etUsuario);
        password = findViewById(R.id.etPassword);

        huella = findViewById(R.id.huella);
        executor = ContextCompat.getMainExecutor(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            biometricPrompt = new BiometricPrompt(this,
                    executor, new BiometricPrompt.AuthenticationCallback() {

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {

                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(Login.this, "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
                }

                //Autenticación exitosa.
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(Login.this, "Autenticación correcta", Toast.LENGTH_SHORT).show();
                    Intent menu = new Intent(Login.this, com.example.proyecto.menu.class);
                    startActivity(menu);
                }

                //Intento fallido sin errores críticos.
                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(Login.this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                }
            });
        }
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesión por medio de huella")
                .setSubtitle("Coloca tu dedo para continuar")
                .setNegativeButtonText("Cancelar")
                .build();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,

                    systemBars.bottom);
            return insets;
        });
    }

    public void ingresar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        String usuario = user.getText().toString();
        String contrasenna = password.getText().toString();

        if(!usuario.isEmpty() && !contrasenna.isEmpty()) {
            Cursor fila = database.rawQuery("select password from users where user = ?", new String[]{usuario});
            if (fila.moveToFirst()) {
                if (fila.getString(0).equals(contrasenna)) {
                    Toast.makeText(this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();
                    Intent menu = new Intent(this, com.example.proyecto.menu.class);
                    database.close();
                    startActivity(menu);
                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    database.close();
                }
            } else {
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                database.close();
            }
        } else {
            Toast.makeText(this, "Ingresa los campos", Toast.LENGTH_SHORT).show();
            database.close();
        }
    }

    public void ingresarHuella(View view) {
        biometricPrompt.authenticate(promptInfo);
    }

    public void registrarse(View view) {
        Intent registrarse = new Intent(this, signup.class);
        startActivity(registrarse);
    }
}