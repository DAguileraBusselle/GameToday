package com.dam.gametoday.notif;

import com.dam.gametoday.model.DatosNoti;

public class NotificationSender {
    public DatosNoti data;
    public String to;

    public NotificationSender(DatosNoti data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationSender() {
    }
}
