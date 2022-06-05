package com.dam.gametoday.model;

import android.graphics.drawable.Drawable;

public class Tema {

    private int colorTransOscuro;
    private int colorTransMenos;
    private int colorTransMenosMasMenosMasMasMenos;
    private int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos;
    private int colorrChilling;
    private int colorrChillingTrans;
    private int colorChilling;

    public Tema(int colorTransOscuro, int colorTransMenos, int colorTransMenosMasMenosMasMasMenos,
                int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos,
                int colorrChilling, int colorrChillingTrans, int colorChilling) {
        this.colorTransOscuro = colorTransOscuro;
        this.colorTransMenos = colorTransMenos;
        this.colorTransMenosMasMenosMasMasMenos = colorTransMenosMasMenosMasMasMenos;
        this.colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos;
        this.colorrChilling = colorrChilling;
        this.colorrChillingTrans = colorrChillingTrans;
        this.colorChilling = colorChilling;
    }

    public int getColorTransOscuro() {
        return colorTransOscuro;
    }

    public void setColorTransOscuro(int colorTransOscuro) {
        this.colorTransOscuro = colorTransOscuro;
    }

    public int getColorTransMenos() {
        return colorTransMenos;
    }

    public void setColorTransMenos(int colorTransMenos) {
        this.colorTransMenos = colorTransMenos;
    }

    public int getColorTransMenosMasMenosMasMasMenos() {
        return colorTransMenosMasMenosMasMasMenos;
    }

    public void setColorTransMenosMasMenosMasMasMenos(int colorTransMenosMasMenosMasMasMenos) {
        this.colorTransMenosMasMenosMasMasMenos = colorTransMenosMasMenosMasMasMenos;
    }

    public int getColorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos() {
        return colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos;
    }

    public void setColorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos(int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos) {
        this.colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos;
    }

    public int getColorrChilling() {
        return colorrChilling;
    }

    public void setColorrChilling(int colorrChilling) {
        this.colorrChilling = colorrChilling;
    }

    public int getColorrChillingTrans() {
        return colorrChillingTrans;
    }

    public void setColorrChillingTrans(int colorrChillingTrans) {
        this.colorrChillingTrans = colorrChillingTrans;
    }

    public int getColorChilling() {
        return colorChilling;
    }

    public void setColorChilling(int colorChilling) {
        this.colorChilling = colorChilling;
    }



}
