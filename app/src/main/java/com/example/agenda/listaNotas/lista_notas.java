package com.example.agenda.listaNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.agenda.ActualizarNota.actualizar_nota;
import com.example.agenda.Objetos.Nota;
import com.example.agenda.R;
import com.example.agenda.ViewHolder.ViewHolder_Nota;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class lista_notas extends AppCompatActivity {

    RecyclerView recyclerViewNotas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Nota, ViewHolder_Nota> firebaseRecyclerAdapter;

    FirebaseRecyclerOptions<Nota> options;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        recyclerViewNotas = findViewById(R.id.recyclerviewNotas);
        recyclerViewNotas.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Notas_Publicadas");
        dialog = new Dialog(lista_notas.this);
        listarNotasUsuarios();
    }

    private void listarNotasUsuarios(){
        options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(BASE_DE_DATOS, Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota, int position, @NonNull Nota nota) {
                viewHolder_nota.setearDatos(
                        getApplicationContext(),
                        nota.getId_nota(),
                        nota.getUid_usuario(),
                        nota.getCorreo_usuario(),
                        nota.getFecha_hora_actual(),
                        nota.getTitulo(),
                        nota.getDescripcion(),
                        nota.getFecha_nota(),
                        nota.getEstado()
                );
            }

            @Override
            public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
                ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);
                viewHolder_nota.setOnClickListener(new ViewHolder_Nota.clickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(lista_notas.this, "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        // OBTENER LOS DATOS DE LA NOTA SELECCIONADA
                        String id_nota = getItem(position).getId_nota();
                        String Uid_usuario = getItem(position).getUid_usuario();
                        String Correo_usuario = getItem(position).getCorreo_usuario();
                        String Fecha_registro = getItem(position).getFecha_hora_actual();
                        String Titulo = getItem(position).getTitulo();
                        String Descripcion = getItem(position).getDescripcion();
                        String Fecha_nota = getItem(position).getFecha_nota();
                        String Estado = getItem(position).getEstado();

                        // DECLARAR LAS VISTAS
                        Button CD_Eliminar, CD_Actualizar;

                        // REALIZA LA CONEXIÓN CON EL DISEÑO
                        dialog.setContentView(R.layout.dialogo_opciones);

                        // INICIALIZAR LAS VISTAS
                        CD_Eliminar = dialog.findViewById(R.id.CD_Eliminar);
                        CD_Actualizar = dialog.findViewById(R.id.CD_Actualizar);

                        CD_Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EliminarNota(id_nota);
                                dialog.dismiss();
                            }
                        });

                        CD_Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(lista_notas.this, "Actualizar Nota", Toast.LENGTH_SHORT).show();
                                // startActivity(new Intent(lista_notas.this, actualizar_nota.class));
                                Intent intent = new Intent(lista_notas.this, actualizar_nota.class);
                                intent.putExtra("id_nota", id_nota);
                                intent.putExtra("Uid_usuario", Uid_usuario);
                                intent.putExtra("Correo_usuario", Correo_usuario);
                                intent.putExtra("Fecha_registro", Fecha_registro);
                                intent.putExtra("Titulo", Titulo);
                                intent.putExtra("Descripcion", Descripcion);
                                intent.putExtra("Fecha_nota", Fecha_nota);
                                intent.putExtra("Estado", Estado);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });
                return viewHolder_nota;
            }
        };

        linearLayoutManager = new LinearLayoutManager(lista_notas.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerViewNotas.setLayoutManager(linearLayoutManager);
        recyclerViewNotas.setAdapter(firebaseRecyclerAdapter);

    }

    private void EliminarNota(String id_nota) {

        AlertDialog.Builder builder = new AlertDialog.Builder(lista_notas.this);
        builder.setTitle("Eliminar Nota");
        builder.setMessage("¿Desea eliminar la nota?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ELIMINAR NOTA EN BD
                Query query = BASE_DE_DATOS.orderByChild("id_nota").equalTo(id_nota);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(lista_notas.this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(lista_notas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(lista_notas.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }
}