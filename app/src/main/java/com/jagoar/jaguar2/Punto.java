package com.jagoar.jaguar2;

public class Punto {
    String id;
    String titulo;
    String creador;
    String fecha;
    String coord;

    public Punto() {
    }

    public Punto(String id, String titulo, String creador, String fecha, String coord) {
        this.id = id;
        this.titulo = titulo;
        this.creador = creador;
        this.fecha = fecha;
        this.coord = coord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCoord() {
        return coord;
    }

    public void setCoord(String coord) {
        this.coord = coord;
    }
}
