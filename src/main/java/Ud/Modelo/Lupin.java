package Ud.Modelo;


public class Lupin extends Personaje {

    public static final char SIMBOLO = 'L';
    private static final int VIDAS_INICIALES = 3;
    public static final int LLAVES_PARA_PUERTA = 3;

    private int vidas;
    private int llaves;

    public Lupin(int fila, int columna) {
        super(SIMBOLO, fila, columna);
        this.vidas = VIDAS_INICIALES;
        this.llaves = 0;
    }


    @Override
    public void evaluarMovimiento(Tablero tablero, Direccion direccion) {
        int nuevaFila = getFila() + direccion.getDeltaFila();
        int nuevaColumna = getColumna() + direccion.getDeltaColumna();

        if (!tablero.esTransitable(nuevaFila, nuevaColumna)) {
            return;
        }

        TipoCasilla destino = tablero.getTipo(nuevaFila, nuevaColumna);


        if (destino == TipoCasilla.PUERTA && !tieneTodasLasLlaves()) {
            return;
        }


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
