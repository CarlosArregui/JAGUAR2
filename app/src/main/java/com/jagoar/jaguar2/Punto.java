package com.jagoar.jaguar2;

public class Punto {
    String id;
    String titulo;
    String creador;
    String fecha;
    String coord;
    String countryName;

    public Punto() {
    }

    public Punto(String id, String titulo, String creador, String fecha, String coord, String countryName) {
        this.id = id;
        this.titulo = titulo;
        this.creador = creador;
        this.fecha = fecha;
        this.coord = coord;
        this.countryName = countryName;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
