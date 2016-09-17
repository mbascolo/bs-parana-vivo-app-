package com.paranavivo.modelo.global;

import android.text.Spanned;

/**
 * Creado por Matias Báscolo
 */
public class Noticia {
    private String id;
    private String titulo;
    private String descripcion;
    private Spanned spanned;

    public Noticia(){
    }

    public Noticia(String id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        //this.pathImagenBannerMiniatura = pathImagenBannerMiniatura;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Spanned getSpanned() {
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }
}
