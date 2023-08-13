package com.example.agenda.ActualizarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.R;
import com.example.agenda.agregarNotas.agregar_nota;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class actualizar_nota extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView id_nota_A, uid_usuario_A, correo_usuario_A, fecha_registro_A, fecha_A, estado_A, estado_nuevo;
    EditText titulo_A, descripcion_A;
    Button btn_calendario_A;

    String id_nota_R, uid_usuario_R, correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    ImageView tarea_finalizada, tarea_no_finalizada;

    Spinner Spinner_estado;

    int dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Actuaizar Nota");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        inicializarVistas();
        recuperarDatos();
        setearDatos();
        comprobarEstadoNota();
        spinner_estado();

        btn_calendario_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarFecha();
            }
        });
    }

    private void inicializarVistas(){
        // TEXTVIEW
        id_nota_A = findViewById(R.id.id_nota_A);
        uid_usuario_A = findViewById(R.id.uid_usuario_A);
        correo_usuario_A = findViewById(R.id.correo_usuario_A);
        fecha_registro_A = findViewById(R.id.fecha_registro_A);
        fecha_A = findViewById(R.id.fecha_A);
        estado_A = findViewById(R.id.estado_A);

        // EDITTEXT
        titulo_A = findViewById(R.id.titulo_A);
        descripcion_A = findViewById(R.id.descripcion_A);

        // BUTTON
        btn_calendario_A = findViewById(R.id.btn_calendario_A);

        tarea_finalizada = findViewById(R.id.tarea_finalizada);
        tarea_no_finalizada = findViewById(R.id.tarea_no_finalizada);

        Spinner_estado = findViewById(R.id.Spinner_estado);
        estado_nuevo = findViewById(R.id.estado_nuevo);
    }

    private void recuperarDatos(){
        Bundle intent = getIntent().getExtras();

        id_nota_R = intent.getString("id_nota");
        uid_usuario_R = intent.getString("Uid_usuario");
        correo_usuario_R = intent.getString("Correo_usuario");
        fecha_registro_R = intent.getString("Fecha_registro");
        titulo_R = intent.getString("Titulo");
        descripcion_R = intent.getString("Descripcion");
        fecha_R = intent.getString("Fecha_nota");
        estado_R = intent.getString("Estado");
    }

    private void setearDatos(){
        id_nota_A.setText(id_nota_R);
        uid_usuario_A.setText(uid_usuario_R);
        correo_usuario_A.setText(correo_usuario_R);
        fecha_registro_A.setText(fecha_registro_R);
        titulo_A.setText(titulo_R);
        descripcion_A.setText(descripcion_R);
        fecha_A.setText(fecha_R);
        estado_A.setText(estado_R);
    }

    private void comprobarEstadoNota(){
        String estado_nota = estado_A.getText().toString();

        if (estado_nota.equals("No finalizado")) {
            tarea_no_finalizada.setVisibility(View.VISIBLE);
        }

        if (estado_nota.equals("Finalizado")){
            tarea_finalizada.setVisibility(View.VISIBLE);
        }
    }

    private void seleccionarFecha(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.DAY_OF_MONTH);
        anio = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(actualizar_nota.this, new DatePickerDialog.OnDateSetListener() {
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

                fecha_A.setText(diaFormateado + "/" + mesFormateado + "/" + anioSeleccionado);

            }
        }
                ,anio,mes,dia);

        datePickerDialog.show();
    }

    private void spinner_estado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.estados_notas, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_estado.setAdapter(adapter);
        Spinner_estado.setOnItemSelectedListener(this);
    }

    private void actualizarNotaBD(){
        String tituloActualizar = titulo_A.getText().toString();
        String descripcionActualizar = descripcion_A.getText().toString();
        String fechaActualizar = fecha_A.getText().toString();
        String estadoActualizar = estado_nuevo.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Notas_Publicadas");

        // Consulta
        Query query = databaseReference.orderByChild("id_nota").equalTo(id_nota_R);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("titulo").setValue(tituloActualizar);
                    ds.getRef().child("descripcion").setValue(descripcionActualizar);
                    ds.getRef().child("fecha_nota").setValue(fechaActualizar);
                    ds.getRef().child("estado").setValue(estadoActualizar);
                }

                Toast.makeText(actualizar_nota.this, "Nota actualizada con éxito", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String ESTADO_ACTUAL = estado_A.getText().toString();

        String posicion_1 = adapterView.getItemAtPosition(1).toString();

        String estado_seleccionado = adapterView.getItemAtPosition(i).toString();
        estado_nuevo.setText(estado_seleccionado);

        if (ESTADO_ACTUAL.equals("Finalizado")){
            estado_nuevo.setText(posicion_1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actualizar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actualizar_nota_BD){
            actualizarNotaBD();
            // Toast.makeText(this, "Nota Actualizada", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}