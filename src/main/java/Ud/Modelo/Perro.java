package Ud.Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Perro enemigo. Hereda de Personaje y se mueve SOLO (automatico), al azar,
 * una casilla por vez (arriba, abajo, izquierda o derecha).
 *
 * Solo puede pararse en casillas validas para perro: area jugable, nunca sobre
 * la puerta ni sobre una llave. Esa regla la decide el Tablero
 * (esValidaParaPerro); el perro solo elige entre las direcciones posibles.
 */
public class Perro extends Personaje {

    public static final char SIMBOLO = 'P';

    private final Random azar = new Random();
    private Direccion ultimaDireccion = null;

    public Perro(int fila, int columna) {
        super(SIMBOLO, fila, columna);
    }

    /**
     * Mueve el perro un paso en una direccion al azar, entre las que llevan a
     * una casilla valida, SIN repetir el movimiento anterior y SIN caer en una
     * casilla ocupada por otro perro. Si al quitar la direccion anterior no
     * queda ninguna opcion, se permite cualquiera valida (para que el perro no
     * quede atascado). Si no hay casilla valida, se queda quieto.
     *
     * @param tablero tablero del juego.
     * @param otros   los demas perros (para no pisarse entre ellos). Puede
     *                incluir a este mismo perro; se ignora su casilla actual.
     */
    public void moverAlAzar(Tablero tablero, List<Perro> otros) {
        List<Direccion> posibles = direccionesValidas(tablero, otros);
        if (posibles.isEmpty()) {
            return;
        }

        // Quitar la direccion del movimiento anterior (no repetirla).
        List<Direccion> sinRepetir = new ArrayList<>(posibles);
        sinRepetir.remove(ultimaDireccion);
        List<Direccion> opciones = sinRepetir.isEmpty() ? posibles : sinRepetir;

        Direccion elegida = opciones.get(azar.nextInt(opciones.size()));
        colocarEn(getFila() + elegida.getDeltaFila(),
                getColumna() + elegida.getDeltaColumna());
        ultimaDireccion = elegida;
    }

    /**
     * Version sin otros perros (compatibilidad): se mueve sin tener en cuenta
     * a los demas.
     */
    @Override
    public void evaluarMovimiento(Tablero tablero, Direccion direccion) {
        moverAlAzar(tablero, Collections.emptyList());
    }

    /**
     * Devuelve las direcciones hacia las que el perro SI puede moverse ahora:
     * la casilla destino debe ser valida para perro (area jugable, ni puerta
     * ni llave) y no estar ocupada por otro perro.
     */
    private List<Direccion> direccionesValidas(Tablero tablero, List<Perro> otros) {
        List<Direccion> validas = new ArrayList<>();
        for (Direccion d : Direccion.values()) {
            int nuevaFila = getFila() + d.getDeltaFila();
            int nuevaColumna = getColumna() + d.getDeltaColumna();
            if (tablero.esValidaParaPerro(nuevaFila, nuevaColumna)
                    && !hayOtroPerroEn(nuevaFila, nuevaColumna, otros)) {
                validas.add(d);
            }
        }
        return validas;
    }

    /**
     * Indica si algun perro distinto de este esta en la casilla dada.
     */
    private boolean hayOtroPerroEn(int fila, int columna, List<Perro> otros) {
        for (Perro otro : otros) {
            if (otro != this
                    && otro.getFila() == fila && otro.getColumna() == columna) {
                return true;
            }
        }
        return false;
    }
}
