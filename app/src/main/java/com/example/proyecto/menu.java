package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void venta(View view) {
        Intent venta = new Intent(this, com.example.proyecto.venta.class);
        startActivity(venta);
    }

    public void agregar(View view) {
        Intent agregar = new Intent(this, com.example.proyecto.agregar.class);
        startActivity(agregar);
    }

    public void modificar(View view) {
        Intent modificar = new Intent(this, com.example.proyecto.modificar.class);
        startActivity(modificar);
    }

    public void inventario(View view) {
        Intent inventario = new Intent(this, com.example.proyecto.inventario.class);
        startActivity(inventario);
    }
}