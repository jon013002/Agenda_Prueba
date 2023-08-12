package com.example.agenda.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

public class ViewHolder_Nota extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolder_Nota.clickListener mClickListener;

    public interface clickListener{
        void onItemClick(View view, int position); /* Se ejecuta al presion en el item */
        void onItemLongClick(View view, int position); /* Se ejecuta al mantener presionado en el item */
    }

    public void setOnClickListener(ViewHolder_Nota.clickListener clickListener){
        mClickListener = clickListener;
    }
    public ViewHolder_Nota(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });
    }

    public void setearDatos(Context context, String id_nota, String Uid_usuario, String Correo_usuario, String Fecha_hora_registro,
                            String Titulo, String Descripcion, String Fecha_nota, String Estado){

        // DECLARAR LAS VISTAS
        TextView id_nota_item, uid_usuario_item, correo_usuario_item, fecha_hora_registro, titulo_item,
                descripcion_item, fecha_item, estado_item;

        // ESTABLECER LA CONEXIÓN CON EL ITEM
        id_nota_item = mView.findViewById(R.id.id_nota_item);
        uid_usuario_item = mView.findViewById(R.id.uid_usuario_item);
        correo_usuario_item = mView.findViewById(R.id.correo_usuario_item);
        fecha_hora_registro = mView.findViewById(R.id.fecha_hora_registro);
        titulo_item = mView.findViewById(R.id.titulo_item);
        descripcion_item = mView.findViewById(R.id.descripcion_item);
        fecha_item = mView.findViewById(R.id.fecha_item);
        estado_item = mView.findViewById(R.id.estado_item);

        // SETEAR LA INFORMACIÓN DENTRO DEL ITEM
        id_nota_item.setText(id_nota);
        uid_usuario_item.setText(Uid_usuario);
        correo_usuario_item.setText(Correo_usuario);
        fecha_hora_registro.setText(Fecha_hora_registro);
        titulo_item.setText(Titulo);
        descripcion_item.setText(Descripcion);
        fecha_item.setText(Fecha_nota);
        estado_item.setText(Estado);
    }
}
