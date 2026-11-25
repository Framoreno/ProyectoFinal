package Ud.Modelo;


public class Llave implements Posicionable {

    public static final char SIMBOLO = 'K';

    private final int fila;
    private final int columna;

    public Llave(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public int getFila() {
        return fila;
    }

    @Override
    public int getColumna() {
        return columna;
    }

    @Override
    public char getSimbolo() {
        return SIMBOLO;
    }
}
