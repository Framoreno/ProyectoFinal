package Ud.Modelo;

/**
 * Las cuatro direcciones en que un personaje puede intentar moverse.
 * Cada direccion sabe cuanto cambia la fila y la columna (delta).
 */
public enum Direccion {
    ARRIBA(-1, 0),
    ABAJO(1, 0),
    IZQUIERDA(0, -1),
    DERECHA(0, 1);

    private final int deltaFila;
    private final int deltaColumna;

    Direccion(int deltaFila, int deltaColumna) {
        this.deltaFila = deltaFila;
        this.deltaColumna = deltaColumna;
    }

    public int getDeltaFila() {
        return deltaFila;
    }

    public int getDeltaColumna() {
        return deltaColumna;
    }
}
