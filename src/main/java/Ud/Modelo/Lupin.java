package Ud.Modelo;

/**
 * El ladron que controla el jugador. Hereda de Personaje.
 *
 * Correccion aplicada: se usan VIDAS en lugar de puntos. Lupin empieza con
 * 3 vidas (cada vez que un perro lo atrapa perdera una; esa logica llega
 * en una entrega posterior). Tambien lleva la cuenta de llaves recogidas,
 * como pide la guia.
 */
public class Lupin extends Personaje {

    public static final char SIMBOLO = 'L';
    private static final int VIDAS_INICIALES = 3;
    /** Llaves necesarias para poder cruzar la puerta. */
    public static final int LLAVES_PARA_PUERTA = 3;

    private int vidas;
    private int llaves;

    public Lupin(int fila, int columna) {
        super(SIMBOLO, fila, columna);
        this.vidas = VIDAS_INICIALES;
        this.llaves = 0;
    }

    /**
     * Mueve a Lupin segun la tecla del jugador, aplicando las reglas de la
     * puerta y de las llaves:
     *
     *  - Si la casilla destino no es transitable (pared) o se sale del
     *    tablero, no se mueve.
     *  - Si la casilla destino es la PUERTA y Lupin aun no tiene las 3 llaves,
     *    no puede pasar: se queda donde esta.
     *  - Si la casilla destino tiene una LLAVE, la recoge: suma al contador y
     *    esa casilla se vuelve espacio jugable (LIBRE).
     *  - En los demas casos, avanza.
     */
    @Override
    public void evaluarMovimiento(Tablero tablero, Direccion direccion) {
        int nuevaFila = getFila() + direccion.getDeltaFila();
        int nuevaColumna = getColumna() + direccion.getDeltaColumna();

        if (!tablero.esTransitable(nuevaFila, nuevaColumna)) {
            return;
        }

        TipoCasilla destino = tablero.getTipo(nuevaFila, nuevaColumna);

        // Regla 1: la puerta solo se cruza con las 3 llaves.
        if (destino == TipoCasilla.PUERTA && !tieneTodasLasLlaves()) {
            return;
        }

        // Regla 2: al pisar una llave, se recoge y la casilla queda libre.
        if (destino == TipoCasilla.LLAVE) {
            recogerLlave();
            tablero.setTipo(nuevaFila, nuevaColumna, TipoCasilla.LIBRE);
        }

        colocarEn(nuevaFila, nuevaColumna);
    }

    public boolean tieneTodasLasLlaves() {
        return llaves >= LLAVES_PARA_PUERTA;
    }

    public int getVidas() {
        return vidas;
    }

    public boolean estaVivo() {
        return vidas > 0;
    }

    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public int getLlaves() {
        return llaves;
    }

    public void recogerLlave() {
        llaves++;
    }
}
