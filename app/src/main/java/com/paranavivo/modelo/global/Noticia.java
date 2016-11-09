package com.paranavivo.modelo.global;

import android.text.Spanned;

/**
 * Creado por Matias Báscolo
 */
public class Noticia {
    private String id;
    private String titulo;
    private String subtitulo;
    private String contenido;
    private Spanned spanned;

    public Noticia(){
    }

    public Noticia(String id, String titulo, String subtitulo, String contenido) {
        this.id = id;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.contenido = contenido;
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

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Spanned getSpanned() {
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }
}
