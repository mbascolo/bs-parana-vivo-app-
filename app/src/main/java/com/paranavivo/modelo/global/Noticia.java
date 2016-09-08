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

    public Noticia(String ID, String post_title, String post_content) {
        this.id = ID;
        this.titulo = post_title;
        this.descripcion = post_content;
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
