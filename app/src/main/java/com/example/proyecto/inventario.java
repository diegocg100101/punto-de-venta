package com.example.proyecto;

import android.database.Cursor;
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

public class inventario extends AppCompatActivity {

    private EditText consulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        consulta = findViewById(R.id.etConsulta);
        consultar();
    }

    public void consultar() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDatabase = admin.getReadableDatabase();

        Cursor cursor = BaseDatabase.rawQuery("SELECT * FROM articulos", null);

        if (cursor.getCount() > 0) {
            StringBuilder resultado = new StringBuilder();
            while (cursor.moveToNext()) {
                String codigo = cursor.getString(cursor.getColumnIndexOrThrow("codigo"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                String costo = cursor.getString(cursor.getColumnIndexOrThrow("costo"));
                String porcentaje = cursor.getString(cursor.getColumnIndexOrThrow("porcentaje"));
                String venta = cursor.getString(cursor.getColumnIndexOrThrow("venta"));
                String cantidad = cursor.getString(cursor.getColumnIndexOrThrow("cantidad"));

                resultado.append("Código: ").append(codigo).append("\n")
                        .append("Nombre: ").append(nombre).append("\n")
                        .append("Descripción: ").append(descripcion).append("\n")
                        .append("Costo: $").append(costo).append("\n")
                        .append("Porcentaje: %").append(porcentaje).append("\n")
                        .append("Venta: $").append(venta).append("\n")
                        .append("Cantidad: ").append(cantidad).append("\n")
                        .append("----------------------\n");
            }
            consulta.setText(resultado.toString());
        } else {
            Toast.makeText(this, "No hay artículos en la base de datos.", Toast.LENGTH_SHORT).show();
            consulta.setText("");
        }
        cursor.close();
        BaseDatabase.close();
    }
}