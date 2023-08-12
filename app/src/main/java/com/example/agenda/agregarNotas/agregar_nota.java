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

import com.example.agenda.Objetos.Nota;
import com.example.agenda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class agregar_nota extends AppCompatActivity {

    TextView uid_usuario, correo_usuario, fecha_hora_actual, fecha, estado;
    EditText titulo, descripcion;
    Button btn_calendario;

    int dia, mes, anio;

    DatabaseReference BD_Firebase;

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

        // BD
        BD_Firebase = FirebaseDatabase.getInstance().getReference();
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

    private void agregar_nota(){

        //Obtener los datos
        String Uid_usuario = uid_usuario.getText().toString();
        String Correo_usuario = correo_usuario.getText().toString();
        String Fecha_hora_actual = fecha_hora_actual.getText().toString();
        String Titulo = titulo.getText().toString();
        String Descripcion = descripcion.getText().toString();
        String Fecha = fecha.getText().toString();
        String Estado = estado.getText().toString();

        // Validar datos
        if (!Uid_usuario.equals("") && !Correo_usuario.equals("") && !Fecha_hora_actual.equals("") &&
        !Titulo.equals("") && !Descripcion.equals("") && !Fecha.equals("") && !Estado.equals("")) {

            Nota nota = new Nota(Correo_usuario + "/" + Fecha_hora_actual,
                    Uid_usuario,
                    Correo_usuario,
                    Fecha_hora_actual,
                    Titulo,
                    Descripcion,
                    Fecha,
                    Estado);

            String nota_usuario = BD_Firebase.push().getKey();

            // Establecer nombre de la BD
            String nombre_BD = "Notas_Publicadas";

            BD_Firebase.child(nombre_BD).child(nota_usuario).setValue(nota);

            Toast.makeText(this, "La nota se ha agregado exitosamente", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }else {
            Toast.makeText(this, "Llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

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
            agregar_nota();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}