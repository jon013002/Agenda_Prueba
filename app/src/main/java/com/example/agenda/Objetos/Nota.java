package com.example.agenda.Objetos;

public class Nota {

    String id_nota, Uid_usuario, Correo_usuario,Fecha_hora_actual, Titulo, Descripcion, Fecha_nota, Estado;

    public Nota() {
    }

    public Nota(String id_nota, String uid_usuario, String correo_usuario, String fecha_hora_actual, String titulo, String descripcion, String fecha_nota, String estado) {
        this.id_nota = id_nota;
        Uid_usuario = uid_usuario;
        Correo_usuario = correo_usuario;
        Fecha_hora_actual = fecha_hora_actual;
        Titulo = titulo;
        Descripcion = descripcion;
        Fecha_nota = fecha_nota;
        Estado = estado;
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getUid_usuario() {
        return Uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        Uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return Correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        Correo_usuario = correo_usuario;
    }

    public String getFecha_hora_actual() {
        return Fecha_hora_actual;
    }

    public void setFecha_hora_actual(String fecha_hora_actual) {
        Fecha_hora_actual = fecha_hora_actual;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFecha_nota() {
        return Fecha_nota;
    }

    public void setFecha_nota(String fecha_nota) {
        Fecha_nota = fecha_nota;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
