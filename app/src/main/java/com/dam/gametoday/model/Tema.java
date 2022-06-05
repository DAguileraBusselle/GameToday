package com.dam.gametoday.model;

public class Tema {

    private int colorTransOscuro = 0;
    private int colorTransMenos = 0;
    private int colorTransMenosMasMenosMasMasMenos = 0;
    private int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos = 0;
    private int colorrChilling = 0;
    private int colorrChillingTrans = 0;
    private int colorChilling = 0;

    public Tema(int colorTransOscuro,
                int colorTransMenos,
                int colorTransMenosMasMenosMasMasMenos,
                int colorTransMenosMasMenosMasMasMenosMenosMasMenosMasMasMenos,
                int colorrChilling,
                int colorrChillingTrans,
                int colorChilling) {
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
