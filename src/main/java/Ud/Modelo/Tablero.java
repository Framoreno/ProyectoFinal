package Ud.Modelo;


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


    public boolean esTransitable(int fila, int columna) {
        return estaDentro(fila, columna)
                && casillas[fila][columna].esTransitable();
    }

    public boolean esValidaParaPerro(int fila, int columna) {
        if (!esTransitable(fila, columna)) {
            return false;
        }
        TipoCasilla tipo = casillas[fila][columna];
        return tipo != TipoCasilla.PUERTA && tipo != TipoCasilla.LLAVE;
    }
}
