package com.example.proyecto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class venta extends AppCompatActivity {

    private EditText codigo, articulos, etPago;
    private TextView nombre, total, pago, cambio;
    private double totalVenta = 0.0;
    private RadioButton tarjeta, efectivo;
    private ArrayList<String> codigosArticulos = new ArrayList<>();
    private Button btnPagar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_venta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        codigo = findViewById(R.id.etCodigoVenta);
        nombre = findViewById(R.id.tvNombre);
        total = findViewById(R.id.tvTotal);
        articulos = findViewById(R.id.etArticulos);
        pago = findViewById(R.id.tvPago);
        etPago = findViewById(R.id.etPago);
        cambio = findViewById(R.id.tvCambio);
        efectivo = findViewById(R.id.rbEfectivo);
        tarjeta = findViewById(R.id.rbTarjeta);
        btnPagar = findViewById(R.id.btnPagar);

        pago.setVisibility(View.INVISIBLE);
        etPago.setVisibility(View.INVISIBLE);
        cambio.setVisibility(View.INVISIBLE);
        btnPagar.setVisibility(View.INVISIBLE);

        codigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizar();
                if (codigo.getText().toString().isEmpty())
                    nombre.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pago.setVisibility(View.VISIBLE);
                etPago.setVisibility(View.VISIBLE);
                cambio.setVisibility(View.VISIBLE);
                btnPagar.setVisibility(View.VISIBLE);
            }
        });

        tarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pago.setVisibility(View.INVISIBLE);
                etPago.setVisibility(View.INVISIBLE);
                cambio.setVisibility(View.INVISIBLE);
                btnPagar.setVisibility(View.VISIBLE);
            }
        });

        etPago.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cambio.setText(String.format("Cambio: $%.2f", calcularCambio()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public double calcularCambio() {
        double cambioDinero = 0.0;
        double pago = 0.0;
        String txtPago = etPago.getText().toString();

        if (!txtPago.isEmpty()) {
            pago = Double.parseDouble(txtPago);
            cambioDinero = pago - totalVenta;
            if (cambioDinero < 0) {
                return 0.0;
            }
        } else {
            Toast.makeText(this, "Ingresa el pago", Toast.LENGTH_SHORT).show();
        }
        return cambioDinero;
    }

    public void actualizar() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();
        String txtCodigo = codigo.getText().toString();

        if (!txtCodigo.isEmpty()) {
            Cursor fila = BaseDeDatabase.rawQuery
                    ("select nombre from articulos where codigo =" +
                            txtCodigo, null);
            if (fila.moveToFirst()) {
                nombre.setText("Artículo:\n" + fila.getString(0));
                BaseDeDatabase.close();
            } else {
                nombre.setText("");
            }
        }
        BaseDeDatabase.close();
    }

    public void pagar(View view) {
        if (efectivo.isChecked()) {
            if (!etPago.getText().toString().isEmpty()) {
                Toast.makeText(this, "Gracias por su compra", Toast.LENGTH_SHORT).show();
                restarUnidades();
                articulos.setText("");
                nombre.setText("Artículo: ");
                total.setText("Total: $0");
                cambio.setText("Cambio: $0");
                pago.setVisibility(View.INVISIBLE);
                etPago.setVisibility(View.INVISIBLE);
                cambio.setVisibility(View.INVISIBLE);
                etPago.setText("");
                cambio.setText("");
                efectivo.setChecked(false);
                codigo.setText("");
                totalVenta = 0;
            } else {
                Toast.makeText(this, "Ingresa la cantidad de dinero a pagar", Toast.LENGTH_SHORT).show();
            }
        } else if (tarjeta.isChecked()) {
            Toast.makeText(this, "Gracias por su compra", Toast.LENGTH_SHORT).show();
            restarUnidades();
            articulos.setText("");
            nombre.setText("Artículo: ");
            total.setText("Total: $0");
            cambio.setText("Cambio: $0");
            tarjeta.setChecked(false);
            codigo.setText("");
            totalVenta = 0;
        } else {
            Toast.makeText(this, "Selecciona método de pago", Toast.LENGTH_SHORT).show();
        }
    }

    public void restarUnidades() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        for (String articulo : codigosArticulos) {
            Cursor fila = database.rawQuery("SELECT cantidad FROM articulos WHERE codigo = ?", new String[]{articulo});
            if (fila.moveToFirst()) {
                int cantidadActual = fila.getInt(0);
                if (cantidadActual > 0) {
                    ContentValues valores = new ContentValues();
                    valores.put("cantidad", cantidadActual - 1);
                    database.update("articulos", valores, "codigo = ?", new String[]{articulo});
                } else {
                    Toast.makeText(this, "El artículo con código " + articulo + " no tiene existencias disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        }
        database.close();
        codigosArticulos.clear();
    }

    public void agregarVenta(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        String txtCodigo = codigo.getText().toString();
        if (!txtCodigo.isEmpty()) {
            Cursor fila = database.rawQuery
                    ("select venta, nombre from articulos where codigo =" +
                            txtCodigo, null);
            if (fila.moveToFirst()) {
                double venta = Double.parseDouble(fila.getString(0));
                String nombreArticulo = fila.getString(1);
                totalVenta += venta;
                total.setText(String.format("Total: $%.2f", totalVenta));

                String txtArticulos = articulos.getText().toString();
                if (!txtArticulos.isEmpty()) {
                    txtArticulos += "\n";
                }
                txtArticulos += nombreArticulo;

                articulos.setText(txtArticulos);
                database.close();
                codigosArticulos.add(txtCodigo);
            } else {
                Toast.makeText(this, "No existe el artículo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingresa el código del artículo", Toast.LENGTH_SHORT).show();
        }
    }

}