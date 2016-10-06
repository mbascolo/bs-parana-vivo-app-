package com.paranavivo.modelo.notificaciones;

/**
 * Created by Claudio on 05/06/2016.
 */
public class Rubro01 {

    private int id;
    private String descripcion;

    public Rubro01() {
    }

    public Rubro01( int codigo, String descripcion) {
        this.id = codigo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
