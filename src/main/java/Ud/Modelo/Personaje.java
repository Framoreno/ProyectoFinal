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
    } /**
 * Cada subclase decide como se mueve: Lupin segun la tecla del jugador, el
 * perro al azar (ignora la direccion) y el vigilante persiguiendo con su
 * propio metodo perseguir(), por lo que aqui no hace nada.
 */
    public abstract void evaluarMovimiento(Tablero tablero, Direccion direccion); /**
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
