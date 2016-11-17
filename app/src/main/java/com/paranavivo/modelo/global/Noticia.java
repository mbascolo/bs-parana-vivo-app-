package com.paranavivo.modelo.global;

import android.text.Spanned;

/**
 * Creado por Matias Báscolo
 */
public class Noticia {
    private String id;
    private String titulo;
    //Subtítulo
    private String descripcion;
    //Contenido
    private String detalle;
    private Spanned spanned;

    public Noticia(){
    }

    public Noticia(String id, String titulo, String subtitulo, String contenido) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = subtitulo;
        this.detalle = contenido;
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

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Spanned getSpanned() {
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }
}
