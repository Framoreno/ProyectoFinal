package Ud.Modelo;


public final class ConstructorMapa {

    public static final int ALTO = 10;

    public static final int BOVEDA1_ANCHO = 10;
    public static final int BOVEDA2_ANCHO = 10;
    public static final int BOVEDA2_ALTO = 5;


    public static final int COLUMNA_SEPARADORA = BOVEDA1_ANCHO;
    public static final int BOVEDA2_COL_INICIAL = COLUMNA_SEPARADORA + 1;

    public static final int TOTAL_COLUMNAS =
            BOVEDA1_ANCHO + 1 + BOVEDA2_ANCHO;


    public static final int BOVEDA2_FILA_INICIAL = (ALTO - BOVEDA2_ALTO) / 2; // 2
    public static final int BOVEDA2_FILA_FINAL =
            BOVEDA2_FILA_INICIAL + BOVEDA2_ALTO - 1;   // 6

    public static final int FILA_PUERTA = ALTO / 2;   // 5

    private ConstructorMapa() {

    }

    public static Tablero construir() {
        Tablero tablero = new Tablero(ALTO, TOTAL_COLUMNAS);
        pintarParedesBoveda1(tablero);
        pintarPuerta(tablero);
        pintarBoveda2(tablero);
        abrirCasillasIndicadas(tablero);
        return tablero;
    }


    private static void abrirCasillasIndicadas(Tablero tablero) {

        for (int fila = 1; fila <= 8; fila++) {
            tablero.setTipo(fila, 9, TipoCasilla.LIBRE);
        }

        tablero.setTipo(3, 11, TipoCasilla.LIBRE);
        tablero.setTipo(4, 11, TipoCasilla.LIBRE);
    }

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

        tablero.setTipo(FILA_PUERTA, ultimaColumna, TipoCasilla.LIBRE);
    }


    private static void pintarPuerta(Tablero tablero) {
        for (int fila = 0; fila < ALTO; fila++) {
            tablero.setTipo(fila, COLUMNA_SEPARADORA, TipoCasilla.PARED);
        }
        tablero.setTipo(FILA_PUERTA, COLUMNA_SEPARADORA, TipoCasilla.PUERTA);
    }

    private static void pintarBoveda2(Tablero tablero) {
        int colInicial = BOVEDA2_COL_INICIAL;
        int colFinal = TOTAL_COLUMNAS - 1;
        int filaInicial = BOVEDA2_FILA_INICIAL;
        int filaFinal = BOVEDA2_FILA_FINAL;

        for (int fila = filaInicial; fila <= filaFinal; fila++) {
            for (int columna = colInicial; columna <= colFinal; columna++) {
                boolean borde = fila == filaInicial || fila == filaFinal
                        || columna == colInicial || columna == colFinal;
                tablero.setTipo(fila, columna,
                        borde ? TipoCasilla.PARED : TipoCasilla.LIBRE);
            }
        }

        tablero.setTipo(FILA_PUERTA, colInicial, TipoCasilla.LIBRE);

        tablero.setTipo(FILA_PUERTA, colFinal - 1, TipoCasilla.TESORO);
    }
}
