package com.dam.gametoday.model;

public class DatosNoti {
    private String Title;
    private String Message;

    public DatosNoti(String title, String message) {
        Title = title;
        Message = message;
    }

    public DatosNoti() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}