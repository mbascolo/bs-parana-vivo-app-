package com.paranavivo.modelo.seguridad;

/**
 * Created by Claudio on 05/10/2016.
 */
public class TipoUsuario {

    private Integer id;
    private String descripcion;

    public TipoUsuario(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
