package com.example.proyecto;

import android.content.ContentValues;
import android.database.Cursor;
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

public class modificar extends AppCompatActivity {

    private EditText codigo, nombre, descripcion, costo, porcentaje, venta, cantidad;
    private TextView  pesos1, pesos2, porcentajeSigno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        codigo = findViewById(R.id.etCodigoModificar);
        nombre = findViewById(R.id.etNombreModificar);
        descripcion = findViewById(R.id.etDescripcionModificar);
        costo = findViewById(R.id.etCostoModificar);
        porcentaje = findViewById(R.id.etPorcentajeModificar);
        venta = findViewById(R.id.etVentaModificar);
        cantidad = findViewById(R.id.etCantidadModificar);

        pesos1 = findViewById(R.id.pesosModificar);
        pesos2 = findViewById(R.id.pesosModificar2);
        porcentajeSigno = findViewById(R.id.porcentajeSignoModificar);

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

        codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizar();
                if (codigo.getText().toString().isEmpty()) {
                    nombre.setText("");
                    descripcion.setText("");
                    costo.setText("");
                    porcentaje.setText("");
                    venta.setText("");
                    cantidad.setText("");
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

    public void actualizar() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();
        String txtCodigo = codigo.getText().toString();

        if (!txtCodigo.isEmpty()) {
            Cursor fila = BaseDeDatabase.rawQuery
                    ("select nombre, descripcion, costo, porcentaje, venta, cantidad from articulos where codigo =" +
                            txtCodigo, null);
            if (fila.moveToFirst()) {
                nombre.setText(fila.getString(0));
                descripcion.setText(fila.getString(1));
                costo.setText(fila.getString(2));
                porcentaje.setText(fila.getString(3));
                venta.setText(fila.getString(4));
                cantidad.setText(fila.getString(5));
                BaseDeDatabase.close();
            } else {
                nombre.setText("");
                descripcion.setText("");
                costo.setText("");
                porcentaje.setText("");
                venta.setText("");
                cantidad.setText("");
            }
        }
        BaseDeDatabase.close();
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

    public void modificar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDatabase = admin.getWritableDatabase();
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
            int res = BaseDatabase.update("articulos", registro, "codigo =" +
                    txtCodigo, null);
            BaseDatabase.close();
            if (res == 1) {
                Toast.makeText(this, "Artículo modificado correctamente",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El artículo no existe",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debes llenar todos los campos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDatabase = admin.getWritableDatabase();

        String txtCodigo = codigo.getText().toString();
        if (!txtCodigo.isEmpty()) {
            int res = BaseDatabase.delete("articulos", "codigo=" + txtCodigo,
                    null);
            BaseDatabase.close();
            codigo.setText("");
            nombre.setText("");
            descripcion.setText("");
            costo.setText("");
            porcentaje.setText("");
            venta.setText("");
            cantidad.setText("");
            if (res == 1) {
                Toast.makeText(this, "Artículo eliminado exitosamente",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El artículo no existe",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debes de introducir el código del artículo",
                    Toast.LENGTH_SHORT).show();
        }
    }
}