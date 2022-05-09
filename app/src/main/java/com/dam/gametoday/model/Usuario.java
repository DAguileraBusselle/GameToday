package com.dam.gametoday.model;

public class Usuario {
    private String displayName;
    private String correo;
    private String userId;

    //no se si es buena idea tener la contrasenia aqui pero weno

    public Usuario(String displayName, String correo, String userId) {
        this.displayName = displayName;
        this.correo = correo;
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Usuario() {
    }
}
