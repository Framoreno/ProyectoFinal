package Ud.Modelo;

/**
 * Clase Tablero (segun el modelo del docente): conoce su tamano en filas y
 * columnas, y ahora tambien de que tipo es cada casilla (libre, pared, puerta
 * o tesoro).
 *
 * Unica responsabilidad: representar la grilla del juego (principio de
 * responsabilidad unica, SOLID). No sabe dibujar ni leer el teclado; de eso se
 * encargan la Vista y el Controlador.
 *
 * Se preserva el comportamiento anterior: getFilas, getColumnas y estaDentro
 * siguen funcionando igual. Al crearse, todas las casillas son LIBRE; quien
 * arma el mapa decide las paredes, la puerta y el tesoro con setTipo.
 */
public class Tablero {

    private final int filas;
    private final int columnas;
    private final TipoCasilla[][] casillas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new TipoCasilla[filas][columnas];
        rellenarLibre();
    }

    private void rellenarLibre() {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                casillas[fila][columna] = TipoCasilla.LIBRE;
            }
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    /**
     * Indica si la casilla (fila, columna) cae dentro del tablero.
     */
    public boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < filas
                && columna >= 0 && columna < columnas;
    }

    public TipoCasilla getTipo(int fila, int columna) {
        return casillas[fila][columna];
    }

    public void setTipo(int fila, int columna, TipoCasilla tipo) {
        casillas[fila][columna] = tipo;
    }

    /**
     * Indica si un personaje puede pararse en esa casilla: debe estar dentro
     * del tablero y no ser una pared.
     */
    public boolean esTransitable(int fila, int columna) {
        return estaDentro(fila, columna)
                && casillas[fila][columna].esTransitable();
    }

    /**
     * Indica si un PERRO puede pararse en esa casilla. Ademas de ser
     * transitable, no puede ser la puerta ni una llave (regla del juego: los
     * perros no se paran sobre la puerta ni sobre las llaves).
     */
    public boolean esValidaParaPerro(int fila, int columna) {
        if (!esTransitable(fila, columna)) {
            return false;
        }
        TipoCasilla tipo = casillas[fila][columna];
        return tipo != TipoCasilla.PUERTA && tipo != TipoCasilla.LLAVE;
    }
}
