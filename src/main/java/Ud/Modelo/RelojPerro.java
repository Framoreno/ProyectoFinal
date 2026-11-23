package Ud.Modelo;

import java.util.List;

/**
 * Reloj INDEPENDIENTE de un perro: es un hilo propio (concurrencia, tema del
 * proyecto). Cada perro tiene su RelojPerro con su propio periodo, por eso los
 * tres perros se mueven a tiempos distintos y de forma independiente entre si.
 *
 * El reloj solo sabe: cada cierto tiempo, mover su perro y avisar que algo
 * cambio. NO sabe dibujar (eso es de la Vista); recibe una accion
 * "alMoverse" que ejecuta tras cada paso. Asi cumple responsabilidad unica y
 * no depende de JavaFX (SOLID).
 */
public class RelojPerro implements Runnable {

    /** Accion a ejecutar despues de cada movimiento (p. ej. redibujar). */
    public interface AlMoverse {
        void ejecutar();
    }

    private final Perro perro;
    private final List<Perro> todosLosPerros;
    private final Tablero tablero;
    private final long periodoMs;
    private final AlMoverse alMoverse;
    private final Object candado;

    private volatile boolean activo = true;
    private Thread hilo;

    /**
     * @param perro          perro que mueve este reloj.
     * @param todosLosPerros todos los perros (para que no se pisen entre si).
     * @param tablero        tablero sobre el que se mueve.
     * @param periodoMs      cada cuantos milisegundos da un paso (su velocidad).
     * @param candado        objeto compartido para sincronizar el paso de todos
     *                       los perros y evitar choques entre hilos.
     * @param alMoverse      accion tras cada paso (normalmente, redibujar).
     */
    public RelojPerro(Perro perro, List<Perro> todosLosPerros, Tablero tablero,
                      long periodoMs, Object candado, AlMoverse alMoverse) {
        this.perro = perro;
        this.todosLosPerros = todosLosPerros;
        this.tablero = tablero;
        this.periodoMs = periodoMs;
        this.candado = candado;
        this.alMoverse = alMoverse;
    }

    /** Arranca el hilo de este reloj. */
    public void iniciar() {
        hilo = new Thread(this, "reloj-perro");
        hilo.setDaemon(true); // no impide cerrar la aplicacion
        hilo.start();
    }

    /** Detiene el reloj de forma ordenada. */
    public void detener() {
        activo = false;
        if (hilo != null) {
            hilo.interrupt();
        }
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(periodoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            // Un paso por vez entre todos los perros: evita carreras al leer y
            // escribir posiciones desde hilos distintos.
            synchronized (candado) {
                perro.moverAlAzar(tablero, todosLosPerros);
            }
            if (alMoverse != null) {
                alMoverse.ejecutar();
            }
        }
    }
}
