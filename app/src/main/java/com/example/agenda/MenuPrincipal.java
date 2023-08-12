package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.agregarNotas.agregar_nota;
import com.example.agenda.listaNotas.lista_notas;
import com.example.agenda.notasArchivadas.notas_archivadas;
import com.example.agenda.perfil.perfil_usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    Button agregarNotas, listaNotas, archivados, perfil, acercaDe,  cerrarSesion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView uidPrincipal, nombrePrincipal, correoPrincipal;
    ProgressBar progressBarDatos;

    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        uidPrincipal = findViewById(R.id.uidPrincipal);
        nombrePrincipal = findViewById(R.id.nombrePrincipal);
        correoPrincipal = findViewById(R.id.correoPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        agregarNotas = findViewById(R.id.agregarNotas);
        listaNotas = findViewById(R.id.listaNotas);
        archivados = findViewById(R.id.archivados);
        perfil = findViewById(R.id.perfil);
        acercaDe = findViewById(R.id.acercaDe);
        cerrarSesion = findViewById(R.id.cerrarSesion);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        agregarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Obtenemos la información de los textviews*/
                String uid_usuario = uidPrincipal.getText().toString();
                String correo_usuario = correoPrincipal.getText().toString();

                /*Pasamos datos a la siguiente actividad*/
                Intent intent = new Intent(MenuPrincipal.this, agregar_nota.class);
                intent.putExtra("uid", uid_usuario);
                intent.putExtra("Correo", correo_usuario);
                startActivity(intent);

            }
        });

        listaNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, lista_notas.class));
                Toast.makeText(MenuPrincipal.this, "Listar Notas", Toast.LENGTH_SHORT).show();
            }
        });

        archivados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, notas_archivadas.class));
                Toast.makeText(MenuPrincipal.this, "Notas Archivadas", Toast.LENGTH_SHORT).show();
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPrincipal.this, perfil_usuario.class));
                Toast.makeText(MenuPrincipal.this, "Perfil Usuario", Toast.LENGTH_SHORT).show();
            }
        });

        acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuPrincipal.this, "Acerca de", Toast.LENGTH_SHORT).show();
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirAplicacion();
            }
        });
    }

    private void comprobarInicioSesion(){
        if(user != null){
            // El usuario a iniciado sesión
            cargaDeDatos();
        }else {
            // Dirige al MainActivity
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        comprobarInicioSesion();
        super.onStart();
    }

    private void cargaDeDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Si el usuario existe
                if(snapshot.exists()){
                    // EL progressBar se oculta
                    progressBarDatos.setVisibility(View.GONE);

                    // Los TextViews se muestran
                    uidPrincipal.setVisibility(View.VISIBLE);
                    nombrePrincipal.setVisibility(View.VISIBLE);
                    correoPrincipal.setVisibility(View.VISIBLE);

                    // Obtener los datos
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombres").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    // Setear los datos en los respectivos TextViews
                    uidPrincipal.setText(uid);
                    nombrePrincipal.setText(nombre);
                    correoPrincipal.setText(correo);

                    // Habilitar los botones del menú
                    agregarNotas.setEnabled(true);
                    listaNotas.setEnabled(true);
                    archivados.setEnabled(true);
                    perfil.setEnabled(true);
                    acercaDe.setEnabled(true);
                    cerrarSesion.setEnabled(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void salirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show();
    }
}