package com.municipalidadavda.Noticias;

/**
 * Creado por Matias Báscolo
 */
public class Post {
    private String ID;
    private String post_title;
    private String post_content;

    public Post(){
    }

    public Post(String ID, String post_title, String post_content) {
        this.ID = ID;
        this.post_title = post_title;
        this.post_content = post_content;
        //this.pathImagenBannerMiniatura = pathImagenBannerMiniatura;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
}
