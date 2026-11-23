package Ud.Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modelo principal del juego: reune el tablero (con sus dos bovedas), a Lupin,
 * a los tres perros y a las tres llaves. Ofrece al controlador operaciones de
 * alto nivel.
 *
 * El mapa de las dos bovedas lo arma ConstructorMapa, asi este modelo no se
 * llena de detalles del mapa (responsabilidad unica, SOLID). El vigilante se
 * agregara despues sin cambiar esta interfaz (abierto/cerrado).
 */
public class JuegoLupin {

    private static final int CANTIDAD_LLAVES = 3;

    private final Tablero tablero;
    private final Lupin lupin;
    private final List<Perro> perros;
    private final List<Llave> llaves;
    private final Vigilante vigilante;

    // Posiciones iniciales, para devolver a todos tras una atrapada.
    private final int lupinFilaInicial;
    private final int lupinColumnaInicial;
    private final int[][] perrosInicial;
    private final int vigilanteFilaInicial;
    private final int vigilanteColumnaInicial;

    public JuegoLupin() {
        this.tablero = ConstructorMapa.construir();
        // Lupin empieza dentro de la boveda 1, por dentro de las paredes
        // (esquina inferior izquierda del area libre).
        this.lupinFilaInicial = ConstructorMapa.ALTO - 2;
        this.lupinColumnaInicial = 1;
        this.lupin = new Lupin(lupinFilaInicial, lupinColumnaInicial);
        // Tres perros arrancan a la derecha de la boveda 1, pegados a la
        // puerta: en la columna libre mas a la derecha (col 9), junto a la
        // fila de la puerta.
        int colDerecha = ConstructorMapa.COLUMNA_SEPARADORA - 1; // 9
        int filaPuerta = ConstructorMapa.FILA_PUERTA;            // 5
        this.perrosInicial = new int[][]{
                {filaPuerta - 1, colDerecha}, // (4,9)
                {filaPuerta,     colDerecha}, // (5,9)
                {filaPuerta + 1, colDerecha}  // (6,9)
        };
        this.perros = List.of(
                new Perro(perrosInicial[0][0], perrosInicial[0][1]),
                new Perro(perrosInicial[1][0], perrosInicial[1][1]),
                new Perro(perrosInicial[2][0], perrosInicial[2][1])
        );
        // Tres llaves en posiciones aleatorias de la boveda 1 (cambian en cada
        // arranque).
        this.llaves = colocarLlavesAlAzar();

        // Vigilante dentro de la boveda 2, cerca de la entrada. Empieza quieto.
        this.vigilanteFilaInicial = ConstructorMapa.BOVEDA2_FILA_INICIAL + 1; // 3
        this.vigilanteColumnaInicial = ConstructorMapa.BOVEDA2_COL_INICIAL + 2; // 13
        this.vigilante = new Vigilante(vigilanteFilaInicial, vigilanteColumnaInicial);
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

    public Vigilante getVigilante() {
        return vigilante;
    }

    /**
     * Indica si Lupin esta en la boveda 2 (columnas a la derecha de la
     * columna separadora).
     */
    public boolean lupinEnBoveda2() {
        return lupin.getColumna() > ConstructorMapa.COLUMNA_SEPARADORA;
    }

    /**
     * Activa el vigilante si Lupin ya entro a la boveda 2 (una sola vez).
     */
    public void activarVigilanteSiCorresponde() {
        if (!vigilante.estaActivo() && lupinEnBoveda2()) {
            vigilante.activar();
        }
    }

    /** True si el vigilante esta en la misma casilla que Lupin (lo atrapo). */
    public boolean vigilanteAtrapoALupin() {
        return vigilante.getFila() == lupin.getFila()
                && vigilante.getColumna() == lupin.getColumna();
    }

    /** True si Lupin llego al tesoro (gano el juego). */
    public boolean lupinGano() {
        return tablero.getTipo(lupin.getFila(), lupin.getColumna())
                == TipoCasilla.TESORO;
    }

    /**
     * Pide a Lupin que se mueva en una direccion. El tablero valida los limites
     * y las paredes.
     */
    public void moverLupin(Direccion direccion) {
        lupin.evaluarMovimiento(tablero, direccion);
    }

    /**
     * Indica si algun perro esta en la misma casilla que Lupin (lo atrapo).
     */
    public boolean perroAtrapoALupin() {
        for (Perro perro : perros) {
            if (perro.getFila() == lupin.getFila()
                    && perro.getColumna() == lupin.getColumna()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Aplica una atrapada: Lupin pierde una vida y devuelve a Lupin y a los
     * perros a su posicion inicial. Las llaves ya recogidas se conservan.
     */
    public void aplicarAtrapada() {
        lupin.perderVida();
        reiniciarPosiciones();
    }

    /** Vidas que le quedan a Lupin. */
    public int getVidas() {
        return lupin.getVidas();
    }

    /** True si Lupin se quedo sin vidas (fin del juego). */
    public boolean juegoTerminado() {
        return !lupin.estaVivo();
    }

    /**
     * Devuelve a Lupin y a los tres perros a su posicion inicial (tras una
     * atrapada). No toca las llaves ni las vidas.
     */
    public void reiniciarPosiciones() {
        lupin.reubicar(lupinFilaInicial, lupinColumnaInicial);
        for (int i = 0; i < perros.size(); i++) {
            perros.get(i).reubicar(perrosInicial[i][0], perrosInicial[i][1]);
        }
        // El vigilante vuelve a su sitio y se desactiva: Lupin regresa a la
        // boveda 1, asi que el vigilante vuelve a quedar quieto.
        vigilante.reubicar(vigilanteFilaInicial, vigilanteColumnaInicial);
        vigilante.desactivar();
    }

    /**
     * Coloca las 3 llaves en casillas LIBRE al azar dentro de la boveda 1, sin
     * repetir casilla y sin caer sobre Lupin ni sobre un perro. Marca cada
     * casilla elegida como LLAVE en el tablero (asi los perros no se paran
     * sobre ellas). Como el orden es aleatorio, las posiciones cambian en cada
     * arranque del juego.
     */
    private List<Llave> colocarLlavesAlAzar() {
        List<int[]> libres = casillasLibresBoveda1();
        Collections.shuffle(libres); // orden aleatorio: distinto cada arranque

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

    /**
     * Busca todas las casillas LIBRE de la boveda 1 (lado izquierdo, antes de
     * la columna separadora) que no esten ocupadas por Lupin ni por un perro.
     */
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

    /** Indica si Lupin o algun perro esta en esa casilla. */
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
