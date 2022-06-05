package com.dam.gametoday;

import android.app.Application;

import com.dam.gametoday.model.Tema;

public class Game2dayApplication extends Application {
    private String fondo;
    private Tema tema;
    private String color;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
