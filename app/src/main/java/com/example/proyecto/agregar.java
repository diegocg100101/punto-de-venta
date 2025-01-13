package com.example.proyecto;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class agregar extends AppCompatActivity {

    private EditText codigo, nombre, descripcion, costo, porcentaje, venta, cantidad;
    private TextView pesos1, pesos2, porcentajeSigno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        codigo = findViewById(R.id.etCodigoAgregar);
        nombre = findViewById(R.id.etNombreAgregar);
        descripcion = findViewById(R.id.etDescripcionAgregar);
        costo = findViewById(R.id.etCostoAgregar);
        porcentaje = findViewById(R.id.etPorcentajeAgregar);
        venta = findViewById(R.id.etVentaAgregar);
        cantidad = findViewById(R.id.etCantidadAgregar);
        pesos1 = findViewById(R.id.pesosAgregar);
        pesos2 = findViewById(R.id.pesosAgregar2);
        porcentajeSigno = findViewById(R.id.porcentajeSignoAgregar);

        pesos1.setVisibility(View.INVISIBLE);
        pesos2.setVisibility(View.INVISIBLE);
        porcentajeSigno.setVisibility(View.INVISIBLE);


        porcentaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcular();
                if(!porcentaje.getText().toString().isEmpty()) {
                    porcentajeSigno.setVisibility(View.VISIBLE);
                } else {
                    porcentajeSigno.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        costo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcular();
                if(!costo.getText().toString().isEmpty()) {
                    pesos1.setVisibility(View.VISIBLE);
                } else {
                    pesos1.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        venta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!venta.getText().toString().isEmpty()) {
                    pesos2.setVisibility(View.VISIBLE);
                }
                else {
                    pesos2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void calcular() {
        String txtCosto = costo.getText().toString();
        String txtPorcentaje = porcentaje.getText().toString();

        if (!txtCosto.isEmpty() && !txtPorcentaje.isEmpty()) {
            double costo = Double.parseDouble(txtCosto);
            double porcentaje = Double.parseDouble(txtPorcentaje);
            double ganancia = costo + (costo * (porcentaje / 100));
            venta.setText(String.format("%.2f", ganancia));
        } else {
            venta.setText("");
        }
    }

    public void registrarArticulo(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        String txtCodigo = codigo.getText().toString();
        String txtNombre = nombre.getText().toString();
        String txtDescripcion = descripcion.getText().toString();
        String txtCosto = costo.getText().toString();
        String txtPorcentaje = porcentaje.getText().toString();
        String txtVenta = venta.getText().toString();
        String txtCantidad = cantidad.getText().toString();

        if (!txtCodigo.isEmpty() && !txtNombre.isEmpty() && !txtDescripcion.isEmpty() && !txtCosto.isEmpty()
                && !txtPorcentaje.isEmpty() && !txtVenta.isEmpty() && !txtCantidad.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("codigo", txtCodigo);
            registro.put("nombre", txtNombre);
            registro.put("descripcion", txtDescripcion);
            registro.put("costo", txtCosto);
            registro.put("porcentaje", txtPorcentaje);
            registro.put("venta", txtVenta);
            registro.put("cantidad", txtCantidad);
            database.insert("articulos", null, registro);
            database.close();
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            codigo.setText("");
            nombre.setText("");
            descripcion.setText("");
            costo.setText("");
            porcentaje.setText("");
            venta.setText("");
            cantidad.setText("");
        } else {
            Toast.makeText(this, "Ingresa correctamente los campos", Toast.LENGTH_SHORT).show();
        }
    }
}