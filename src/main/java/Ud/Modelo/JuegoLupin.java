package Ud.Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoLupin {

    private static final int CANTIDAD_LLAVES = 3;

    private final Tablero tablero;
    private final Lupin lupin;
    private final List<Perro> perros;
    private final List<Llave> llaves;
    private final List<Vigilante> vigilantes;


    private final int lupinFilaInicial;
    private final int lupinColumnaInicial;
    private final int[][] perrosInicial;
    private final int[][] vigilantesInicial;

    public JuegoLupin() {
        this.tablero = ConstructorMapa.construir();

        this.lupinFilaInicial = ConstructorMapa.ALTO - 2;
        this.lupinColumnaInicial = 1;
        this.lupin = new Lupin(lupinFilaInicial, lupinColumnaInicial);

        int colDerecha = ConstructorMapa.COLUMNA_SEPARADORA - 1;
        int filaPuerta = ConstructorMapa.FILA_PUERTA;
        this.perrosInicial = new int[][]{
                {filaPuerta - 1, colDerecha},
                {filaPuerta,     colDerecha},
                {filaPuerta + 1, colDerecha}
        };
        this.perros = List.of(
                new Perro(perrosInicial[0][0], perrosInicial[0][1]),
                new Perro(perrosInicial[1][0], perrosInicial[1][1]),
                new Perro(perrosInicial[2][0], perrosInicial[2][1])
        );

        this.llaves = colocarLlavesAlAzar();

        int filaVig = ConstructorMapa.BOVEDA2_FILA_INICIAL + 1;
        int colVigIzq = ConstructorMapa.BOVEDA2_COL_INICIAL + 1;
        int colVigDer = ConstructorMapa.TOTAL_COLUMNAS - 2;
        this.vigilantesInicial = new int[][]{
                {filaVig, colVigIzq},
                {filaVig, colVigDer}
        };
        this.vigilantes = List.of(
                new Vigilante(vigilantesInicial[0][0], vigilantesInicial[0][1]),
                new Vigilante(vigilantesInicial[1][0], vigilantesInicial[1][1])
        );
    }

    public Tablero getTablero() {
        return tablero;
    }

    public Lupin getLupin() {
        return lupin;
    }

    public List<Perro> getPerros() {
        return perros;
    }

    public List<Llave> getLlaves() {
        return llaves;
    }

    public List<Vigilante> getVigilantes() {
        return vigilantes;
    }


    public boolean lupinEnBoveda2() {
        return lupin.getColumna() > ConstructorMapa.COLUMNA_SEPARADORA;
    }

    public void activarVigilante() {
        if (lupinEnBoveda2()) {
            for (Vigilante v : vigilantes) {
                if (!v.estaActivo()) {
                    v.activar();
                }
            }
        }
    }

    public boolean vigilanteAtrapoALupin() {
        for (Vigilante v : vigilantes) {
            if (v.getFila() == lupin.getFila()
                    && v.getColumna() == lupin.getColumna()) {
                return true;
            }
        }
        return false;
    }

    public boolean lupinGano() {
        return tablero.getTipo(lupin.getFila(), lupin.getColumna())
                == TipoCasilla.TESORO;
    }


    public void moverLupin(Direccion direccion) {
        lupin.evaluarMovimiento(tablero, direccion);
    }


    public boolean perroAtrapoALupin() {
        for (Perro perro : perros) {
            if (perro.getFila() == lupin.getFila()
                    && perro.getColumna() == lupin.getColumna()) {
                return true;
            }
        }
        return false;
    }


    public void aplicarAtrapada() {
        lupin.perderVida();
        reiniciarPosiciones();
    }


    public int getVidas() {
        return lupin.getVidas();
    }


    public boolean juegoTerminado() {
        return !lupin.estaVivo();
    }


    public void reiniciarPosiciones() {
        lupin.reubicar(lupinFilaInicial, lupinColumnaInicial);
        for (int i = 0; i < perros.size(); i++) {
            perros.get(i).reubicar(perrosInicial[i][0], perrosInicial[i][1]);
        }

        for (int i = 0; i < vigilantes.size(); i++) {
            vigilantes.get(i).reubicar(vigilantesInicial[i][0], vigilantesInicial[i][1]);
            vigilantes.get(i).desactivar();
        }
    }


    private List<Llave> colocarLlavesAlAzar() {
        List<int[]> libres = casillasLibresBoveda1();
        Collections.shuffle(libres);

        List<Llave> elegidas = new ArrayList<>();
        for (int[] celda : libres) {
            if (elegidas.size() == CANTIDAD_LLAVES) {
                break;
            }
            int fila = celda[0];
            int columna = celda[1];
            tablero.setTipo(fila, columna, TipoCasilla.LLAVE);
            elegidas.add(new Llave(fila, columna));
        }
        return elegidas;
    }


    private List<int[]> casillasLibresBoveda1() {
        List<int[]> libres = new ArrayList<>();
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < ConstructorMapa.COLUMNA_SEPARADORA; columna++) {
                if (tablero.getTipo(fila, columna) == TipoCasilla.LIBRE
                        && !hayPersonajeEn(fila, columna)) {
                    libres.add(new int[]{fila, columna});
                }
            }
        }
        return libres;
    }


    private boolean hayPersonajeEn(int fila, int columna) {
        if (lupin.getFila() == fila && lupin.getColumna() == columna) {
            return true;
        }
        for (Perro perro : perros) {
            if (perro.getFila() == fila && perro.getColumna() == columna) {
                return true;
            }
        }
        return false;
    }
}
