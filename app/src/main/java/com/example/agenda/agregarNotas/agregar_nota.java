package com.example.agenda.agregarNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class agregar_nota extends AppCompatActivity {

    TextView uid_usuario, correo_usuario, fecha_hora_actual, fecha, estado;
    EditText titulo, descripcion;
    Button btn_calendario;

    int dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        inicializarVariables();
        obtenerDatos();
        obtener_fecha_hora_actual();

        btn_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.DAY_OF_MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(agregar_nota.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anioSeleccionado, int mesSeleccionado, int diaSeleccionado) {

                        String diaFormateado, mesFormateado;

                        // OBTENER DIA
                        if (diaSeleccionado < 10){
                            diaFormateado = "0" + String.valueOf(diaSeleccionado);
                        }else {
                            diaFormateado = String.valueOf(diaSeleccionado);
                        }

                        // OBTENER EL MES
                        int Mes = mesSeleccionado + 1;

                        if (Mes < 10){
                            mesFormateado = "0" + String.valueOf(Mes);
                        }else {
                            mesFormateado = String.valueOf(Mes);
                        }

                        fecha.setText(diaFormateado + "/" + mesFormateado + "/" + anioSeleccionado);

                    }
                }
                ,anio,mes,dia);

                datePickerDialog.show();

            }
        });

    }

    private void inicializarVariables(){
        // TextView
        uid_usuario = findViewById(R.id.uid_usuario);
        correo_usuario = findViewById(R.id.correo_usuario);
        fecha_hora_actual = findViewById(R.id.fecha_hora_actual);
        fecha = findViewById(R.id.fecha);
        estado = findViewById(R.id.estado);

        // EditText
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);

        // Button
        btn_calendario = findViewById(R.id.btn_calendario);
    }

    private void obtenerDatos(){
        String uid_recuperado = getIntent().getStringExtra("uid");
        String correo_recuperado = getIntent().getStringExtra("Correo");

        uid_usuario.setText(uid_recuperado);
        correo_usuario.setText(correo_recuperado);
    }

    private void obtener_fecha_hora_actual(){
        String fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a",
                Locale.getDefault()).format(System.currentTimeMillis());

        fecha_hora_actual.setText(fecha_hora_registro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agenda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.agregar_nota_BD){
            Toast.makeText(this, "Nota agregada con éxito", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}