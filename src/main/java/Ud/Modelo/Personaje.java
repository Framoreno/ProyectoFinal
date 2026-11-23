package Ud.Modelo;

/**
 * Clase abstracta Personaje (segun el modelo en el tablero del docente).
 * Guarda simbolo, fila y columna. Define el comportamiento comun de moverse
 * sobre un tablero y deja el "como decide moverse" a cada subclase mediante
 * evaluarMovimiento() (patron Template Method).
 *
 * Al depender del tablero por su posicion, cualquier personaje (Lupin, Perro,
 * Vigilante) se puede sustituir sin romper el tablero (principio de sustitucion
 * de Liskov, SOLID).
 */
public abstract class Personaje implements Posicionable {

    private char simbolo;
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

    protected void setFila(int fila) {
        this.fila = fila;
    }

    protected void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Cada subclase decide como se mueve (el jugador con el teclado, los perros
     * al azar, el vigilante persiguiendo). Por ahora solo lo usa Lupin.
     */
    public abstract void evaluarMovimiento(Tablero tablero, Direccion direccion);

    /**
     * Mueve el personaje en una direccion solo si la casilla destino es
     * transitable: dentro del tablero y que no sea una pared. Si no, se queda
     * quieto.
     */
    protected void moverDentroDe(Tablero tablero, Direccion direccion) {
        int nuevaFila = fila + direccion.getDeltaFila();
        int nuevaColumna = columna + direccion.getDeltaColumna();
        if (tablero.esTransitable(nuevaFila, nuevaColumna)) {
            this.fila = nuevaFila;
            this.columna = nuevaColumna;
        }
    }

    /**
     * Coloca el personaje en una casilla concreta. Lo usan las subclases que
     * validan el destino con su propia regla (por ejemplo el perro, que no
     * puede pisar la puerta ni las llaves).
     */
    protected void colocarEn(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Reubica el personaje en una casilla concreta, sin validar (lo usa el
     * juego para devolver a todos a su posicion inicial tras una atrapada).
     */
    public void reubicar(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }
}
