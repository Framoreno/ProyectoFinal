package Ud.Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Perro extends Personaje {

    public static final char SIMBOLO = 'P';

    private final Random azar = new Random();
    private Direccion ultimaDireccion = null;

    public Perro(int fila, int columna) {
        super(SIMBOLO, fila, columna);
    }


    public void moverAlAzar(Tablero tablero, List<Perro> otros) {
        List<Direccion> posibles = direccionesValidas(tablero, otros);
        if (posibles.isEmpty()) {
            return;
        }


        List<Direccion> sinRepetir = new ArrayList<>(posibles);
        sinRepetir.remove(ultimaDireccion);
        List<Direccion> opciones = sinRepetir.isEmpty() ? posibles : sinRepetir;

        Direccion elegida = opciones.get(azar.nextInt(opciones.size()));
        colocarEn(getFila() + elegida.getDeltaFila(),
                getColumna() + elegida.getDeltaColumna());
        ultimaDireccion = elegida;
    }


    @Override
    public void evaluarMovimiento(Tablero tablero, Direccion direccion) {
        moverAlAzar(tablero, Collections.emptyList());
    }


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
