package com.dam.gametoday.model;

import java.util.Date;

public class Publicacion {

    private String nombreUser;
    private String texto;
    private long fechaPubliMilis;

    public Publicacion(String nombreUser, String texto, long fechaPubliMilis) {
        this.nombreUser = nombreUser;
        this.texto = texto;
        this.fechaPubliMilis = fechaPubliMilis;
    }

    public String getUser() { return nombreUser; }

    public void setUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getTexto() { return texto; }

    public void setTexto(String texto) { this.texto = texto; }

    public long getFechaPubli() { return fechaPubliMilis; }

    public void setFechaPubli(long fechaPubli) { this.fechaPubliMilis = fechaPubli; }

}
