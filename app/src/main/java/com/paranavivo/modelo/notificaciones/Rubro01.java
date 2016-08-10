package com.paranavivo.modelo.notificaciones;

/**
 * Created by Claudio on 05/06/2016.
 */
public class Rubro01 {

    private String codigo;
    private String nrocta;
    private String descripcion;

    public Rubro01() {
    }

    public Rubro01( String nrocta, String codigo, String descripcion) {
        this.codigo = codigo;
        this.nrocta = nrocta;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNrocta() {
        return nrocta;
    }

    public void setNrocta(String nrocta) {
        this.nrocta = nrocta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
