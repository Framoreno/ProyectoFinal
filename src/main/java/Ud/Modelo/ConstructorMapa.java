package Ud.Modelo;

/**
 * Arma el mapa del castillo con sus dos bovedas sobre un unico tablero
 * (una sola escena, como pide la guia).
 *
 * Unica responsabilidad: pintar los tipos de casilla del tablero
 * (paredes, puerta y tesoro). Separarlo de JuegoLupin mantiene cada clase
 * enfocada (responsabilidad unica, SOLID) y permite cambiar el mapa sin tocar
 * la logica del juego.
 *
 * Disposicion: las dos bovedas quedan UNA AL LADO DE LA OTRA (izquierda y
 * derecha), separadas por UNA SOLA columna de pared (pared simple) que tiene
 * la PUERTA. Las dos bovedas comparten las 10 filas de alto del tablero.
 *
 *   columnas 0..9    -> Boveda 1 (10 alto x 10 ancho), rodeada de paredes cafe.
 *   columna  10      -> pared simple que separa; una casilla es la PUERTA.
 *   columnas 11..20  -> Boveda 2 (5 alto x 10 ancho), con paredes cafe y el
 *                       TESORO al final (lado derecho). Como mide 5 de alto,
 *                       se centra verticalmente dentro de las 10 filas.
 *
 * Total del tablero: 10 filas x 21 columnas.
 */
public final class ConstructorMapa {

    public static final int ALTO = 10;                 // filas del tablero

    public static final int BOVEDA1_ANCHO = 10;        // columnas boveda 1
    public static final int BOVEDA2_ANCHO = 10;        // columnas boveda 2
    public static final int BOVEDA2_ALTO = 5;          // filas utiles boveda 2

    /** Columna de la pared simple que separa las dos bovedas. */
    public static final int COLUMNA_SEPARADORA = BOVEDA1_ANCHO; // 10
    public static final int BOVEDA2_COL_INICIAL = COLUMNA_SEPARADORA + 1; // 11

    public static final int TOTAL_COLUMNAS =
            BOVEDA1_ANCHO + 1 + BOVEDA2_ANCHO;         // 21

    /**
     * La boveda 2 (5 de alto) va centrada en las 10 filas: ocupa las filas
     * 2..6, dejando un margen arriba y abajo.
     */
    public static final int BOVEDA2_FILA_INICIAL = (ALTO - BOVEDA2_ALTO) / 2; // 2
    public static final int BOVEDA2_FILA_FINAL =
            BOVEDA2_FILA_INICIAL + BOVEDA2_ALTO - 1;   // 6

    /** Fila donde queda la puerta dentro de la pared separadora. */
    public static final int FILA_PUERTA = ALTO / 2;   // 5

    private ConstructorMapa() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Crea el tablero completo con las dos bovedas ya pintadas.
     */
    public static Tablero construir() {
        Tablero tablero = new Tablero(ALTO, TOTAL_COLUMNAS);
        pintarParedesBoveda1(tablero);
        pintarParedSeparadoraConPuerta(tablero);
        pintarBoveda2(tablero);
        abrirCasillasIndicadas(tablero);
        return tablero;
    }

    /**
     * Convierte en espacio jugable (LIBRE) las casillas que el usuario indico.
     *
     * El usuario cuenta filas y columnas EMPEZANDO EN 1, mientras que el
     * modelo usa indices que empiezan en 0. Por eso a cada numero del usuario
     * se le resta 1.
     *
     * Indicado por el usuario (base 1) -> indice en el modelo (base 0):
     *   - Columna 10, filas 2 a 9   -> columna 9, filas 1 a 8.
     *   - Columna 12, filas 4 y 5   -> columna 11, filas 3 y 4.
     */
    private static void abrirCasillasIndicadas(Tablero tablero) {
        // Columna 10 del usuario = indice 9; filas 2..9 = indices 1..8.
        for (int fila = 1; fila <= 8; fila++) {
            tablero.setTipo(fila, 9, TipoCasilla.LIBRE);
        }
        // Columna 12 del usuario = indice 11; filas 4 y 5 = indices 3 y 4.
        tablero.setTipo(3, 11, TipoCasilla.LIBRE);
        tablero.setTipo(4, 11, TipoCasilla.LIBRE);
    }

    /**
     * Rodea la boveda 1 (columnas 0..9) con paredes cafe. El interior queda
     * libre. El muro derecho deja una abertura en la fila de la puerta para
     * poder cruzar hacia la boveda 2.
     */
    private static void pintarParedesBoveda1(Tablero tablero) {
        int ultimaColumna = BOVEDA1_ANCHO - 1; // 9
        for (int fila = 0; fila < ALTO; fila++) {
            for (int columna = 0; columna <= ultimaColumna; columna++) {
                boolean borde = fila == 0 || fila == ALTO - 1
                        || columna == 0 || columna == ultimaColumna;
                if (borde) {
                    tablero.setTipo(fila, columna, TipoCasilla.PARED);
                }
            }
        }
        // Abertura en el muro derecho, alineada con la puerta.
        tablero.setTipo(FILA_PUERTA, ultimaColumna, TipoCasilla.LIBRE);
    }

    /**
     * La columna entre las dos bovedas es una pared SIMPLE (una sola columna),
     * menos la casilla puerta.
     */
    private static void pintarParedSeparadoraConPuerta(Tablero tablero) {
        for (int fila = 0; fila < ALTO; fila++) {
            tablero.setTipo(fila, COLUMNA_SEPARADORA, TipoCasilla.PARED);
        }
        tablero.setTipo(FILA_PUERTA, COLUMNA_SEPARADORA, TipoCasilla.PUERTA);
    }

    /**
     * Pinta la boveda 2 (columnas 11..20, filas 2..6): paredes cafe alrededor,
     * interior libre y el tesoro al final (esquina derecha). El muro izquierdo
     * deja una abertura en la fila de la puerta para entrar desde la puerta.
     */
    private static void pintarBoveda2(Tablero tablero) {
        int colInicial = BOVEDA2_COL_INICIAL;              // 11
        int colFinal = TOTAL_COLUMNAS - 1;                 // 20
        int filaInicial = BOVEDA2_FILA_INICIAL;            // 2
        int filaFinal = BOVEDA2_FILA_FINAL;                // 6

        for (int fila = filaInicial; fila <= filaFinal; fila++) {
            for (int columna = colInicial; columna <= colFinal; columna++) {
                boolean borde = fila == filaInicial || fila == filaFinal
                        || columna == colInicial || columna == colFinal;
                tablero.setTipo(fila, columna,
                        borde ? TipoCasilla.PARED : TipoCasilla.LIBRE);
            }
        }
        // Abertura en el muro izquierdo de la boveda 2, frente a la puerta.
        tablero.setTipo(FILA_PUERTA, colInicial, TipoCasilla.LIBRE);
        // Tesoro al final de la boveda 2, DENTRO del espacio transitable:
        // la ultima casilla libre antes del muro derecho, en la fila de la
        // puerta. Asi el ladron puede llegar hasta el y no queda en la pared.
        tablero.setTipo(FILA_PUERTA, colFinal - 1, TipoCasilla.TESORO);
    }
}
