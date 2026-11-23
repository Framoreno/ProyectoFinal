package Ud.Modelo;

/**
 * Vigilante de la boveda 2. Hereda de Personaje.
 *
 * Empieza quieto y solo se ACTIVA cuando Lupin entra a la boveda 2. Una vez
 * activo, PERSIGUE a Lupin: en cada paso se acerca una casilla, reduciendo
 * primero la diferencia mas grande (en fila o en columna). Solo se mueve por
 * casillas transitables; si la casilla elegida no es valida, intenta el otro
 * eje.
 */
public class Vigilante extends Personaje {

    public static final char SIMBOLO = 'V';

    private boolean activo = false;

    public Vigilante(int fila, int columna) {
        super(SIMBOLO, fila, columna);
    }

    public boolean estaActivo() {
        return activo;
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    /**
     * El vigilante persigue a Lupin. La direccion recibida se ignora: calcula
     * su propia direccion hacia Lupin. Solo se mueve si esta activo.
     *
     * Se incluye para cumplir la firma de Personaje, pero el metodo comodo es
     * perseguir(tablero, lupin).
     */
    @Override
    public void evaluarMovimiento(Tablero tablero, Direccion direccion) {
        // El vigilante necesita saber donde esta Lupin; sin esa info no se
        // mueve aqui. Se usa perseguir(...) desde su reloj.
    }

    /**
     * Da un paso acercandose a Lupin. Reduce primero la diferencia mayor
     * (fila o columna). Si esa casilla no es transitable, intenta por el otro
     * eje. Si ninguna sirve, se queda quieto.
     */
    public void perseguir(Tablero tablero, Lupin lupin) {
        if (!activo) {
            return;
        }
        int difFila = Integer.compare(lupin.getFila(), getFila());      // -1,0,1
        int difCol = Integer.compare(lupin.getColumna(), getColumna()); // -1,0,1

        int distFila = Math.abs(lupin.getFila() - getFila());
        int distCol = Math.abs(lupin.getColumna() - getColumna());

        // Intentar primero el eje con mayor distancia.
        boolean filaPrimero = distFila >= distCol;

        if (filaPrimero) {
            if (intentarMover(tablero, difFila, 0)) return;
            intentarMover(tablero, 0, difCol);
        } else {
            if (intentarMover(tablero, 0, difCol)) return;
            intentarMover(tablero, difFila, 0);
        }
    }

    /**
     * Mueve un paso (deltaFila, deltaColumna) si la casilla destino es
     * transitable. Devuelve true si se movio.
     */
    private boolean intentarMover(Tablero tablero, int deltaFila, int deltaColumna) {
        if (deltaFila == 0 && deltaColumna == 0) {
            return false;
        }
        int nuevaFila = getFila() + deltaFila;
        int nuevaColumna = getColumna() + deltaColumna;
        if (tablero.esTransitable(nuevaFila, nuevaColumna)) {
            colocarEn(nuevaFila, nuevaColumna);
            return true;
        }
        return false;
    }
}
