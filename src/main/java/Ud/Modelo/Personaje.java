package Ud.Modelo;


public abstract class Personaje implements Posicionable {

    private final char simbolo;
    private int fila;
    private int columna;

    protected Personaje(char simbolo, int fila, int columna) {
        this.simbolo = simbolo;
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public char getSimbolo() {
        return simbolo;
    }

    @Override
    public int getFila() {
        return fila;
    }

    @Override
    public int getColumna() {
        return columna;
    }
    public abstract void evaluarMovimiento(Tablero tablero, Direccion direccion);
    protected void colocarEn(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public void reubicar(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
}
