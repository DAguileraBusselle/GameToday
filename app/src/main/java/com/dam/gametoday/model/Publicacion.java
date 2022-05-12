package com.dam.gametoday.model;

public class Publicacion {

    private String publiId;
    private String nombreUser;
    private String texto;
    private long fechaPubliMilis;
    private String userId;
    private String imagenPubli;
    private long likes;

    public Publicacion(String publiId, String nombreUser, String texto, long fechaPubliMilis, String userId, String imagenPubli, long likes) {
        this.publiId = publiId;
        this.nombreUser = nombreUser;
        this.texto = texto;
        this.fechaPubliMilis = fechaPubliMilis;
        this.userId = userId;
        this.imagenPubli = imagenPubli;
        this.likes = likes;
    }

    public long getLikes() { return likes; }

    public void setLikes(long likes) { this.likes = likes; }

    public String getPubliId() { return publiId; }

    public void setPubliId(String publiId) { this.publiId = publiId; }

    public String getUser() { return nombreUser; }

    public void setUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getTexto() { return texto; }

    public void setTexto(String texto) { this.texto = texto; }

    public long getFechaPubli() { return fechaPubliMilis; }

    public void setFechaPubli(long fechaPubli) { this.fechaPubliMilis = fechaPubli; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getImagenPubli() { return imagenPubli; }

    public void setImagenPubli(String imagenPubli) { this.imagenPubli = imagenPubli; }
}
