package Ud.Modelo;

/**
 * Una llave que el ladron debe recoger. Ocupa una casilla del tablero.
 *
 * Es un elemento del mapa, no un Personaje (no se mueve), pero implementa
 * Posicionable para que la vista la pueda ubicar y dibujar igual que a los
 * demas (SOLID: la vista depende de una interfaz pequena, no de la clase).
 */
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
