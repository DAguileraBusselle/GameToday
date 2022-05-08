package com.dam.gametoday.model;

import java.util.Date;

public class Publicacion {

    private String nombreUser;
    private String texto;

    public Publicacion(String nombreUser, String texto) {
        this.nombreUser = nombreUser;
        this.texto = texto;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getTexto() {
        return texto;
    }

    public Publicacion() {
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
