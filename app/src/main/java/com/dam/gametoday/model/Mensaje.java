package com.dam.gametoday.model;

public class Mensaje {
    private String participante;
    private Boolean msjEntrante;
    private String texto;
    private String leido;
    private long fechaHoraMsj;


    public Mensaje(String participante, Boolean msjEntrante, String texto, String leido, long fechaHoraMsj) {
        this.participante = participante;
        this.msjEntrante = msjEntrante;
        this.texto = texto;
        this.leido = leido;
        this.fechaHoraMsj = fechaHoraMsj;
    }

    public String getParticipante() {
        return participante;
    }

    public String getLeido() {
        return leido;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }

    public void setParticipante(String participante) {
        this.participante = participante;
    }

    public Boolean getMsjEntrante() {
        return msjEntrante;
    }

    public void setMsjEntrante(Boolean msjEntrante) {
        this.msjEntrante = msjEntrante;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getFechaHoraMsj() {
        return fechaHoraMsj;
    }

    public void setFechaHoraMsj(long fechaHoraMsj) {
        this.fechaHoraMsj = fechaHoraMsj;
    }
}
