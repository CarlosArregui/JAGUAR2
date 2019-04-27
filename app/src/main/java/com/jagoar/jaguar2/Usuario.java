package com.jagoar.jaguar2;

import java.util.ArrayList;

public class Usuario {
    String correo,nombre;
    ArrayList<String> eventos_creados;
    ArrayList<String> eventos_guardados;


    public Usuario(String correo, String nombre, ArrayList<String> eventos_creados, ArrayList<String> eventos_guardados) {
        this.correo = correo;
        this.nombre = nombre;
        this.eventos_creados = eventos_creados;
        this.eventos_guardados = eventos_guardados;
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

    public ArrayList<String> getEventos_guardados() {
        return eventos_guardados;
    }

    public void setEventos_guardados(ArrayList<String> eventos_guardados) {
        this.eventos_guardados = eventos_guardados;
    }
}