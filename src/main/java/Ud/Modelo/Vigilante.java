package Ud.Modelo;

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


    @Override
    public void evaluarMovimiento(Tablero tablero, Direccion direccion) {

    }

    public void perseguir(Tablero tablero, Lupin lupin) {
        if (!activo) {
            return;
        }
        int difFila = Integer.compare(lupin.getFila(), getFila());
        int difCol = Integer.compare(lupin.getColumna(), getColumna());

        int distFila = Math.abs(lupin.getFila() - getFila());
        int distCol = Math.abs(lupin.getColumna() - getColumna());

        boolean filaPrimero = distFila >= distCol;

        if (filaPrimero) {
            if (intentarMover(tablero, difFila, 0)) return;
            intentarMover(tablero, 0, difCol);
        } else {
            if (intentarMover(tablero, 0, difCol)) return;
            intentarMover(tablero, difFila, 0);
        }
    }

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
