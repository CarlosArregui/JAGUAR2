package com.jagoar.jaguar2;

import java.util.ArrayList;

public class Usuario {
    String correo, nombre;
    ArrayList<String> eventos_creados;


    public Usuario(String correo, String nombre, ArrayList<String> eventos_creados) {
        this.correo = correo;
        this.nombre = nombre;
        this.eventos_creados = eventos_creados;

    }

    public Usuario() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getEventos_creados() {
        return eventos_creados;
    }

    public void setEventos_creados(ArrayList<String> eventos_creados) {
        this.eventos_creados = eventos_creados;
    }

}