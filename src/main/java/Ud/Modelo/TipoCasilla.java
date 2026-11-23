package Ud.Modelo;

/**
 * Tipos de casilla que puede tener el tablero.
 *
 *  LIBRE   : se puede caminar por ella.
 *  PARED   : muro cafe que rodea la boveda 1; no se puede pasar.
 *  PUERTA  : la casilla que une la boveda 1 con la boveda 2; se puede pasar.
 *  TESORO  : la caja fuerte al final de la boveda 2.
 *
 * Cada tipo sabe si se puede caminar por el (transitable). Asi el tablero no
 * necesita condicionales repartidos: pregunta al tipo.
 */
public enum TipoCasilla {
    LIBRE(true),
    PARED(false),
    PUERTA(true),
    LLAVE(true),
    TESORO(true);

    private final boolean transitable;

    TipoCasilla(boolean transitable) {
        this.transitable = transitable;
    }

    public boolean esTransitable() {
        return transitable;
    }
}
