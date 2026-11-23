package Ud.Modelo;

/**
 * Reloj INDEPENDIENTE del vigilante: su propio hilo (concurrencia). En cada
 * tic, el vigilante persigue a Lupin un paso. Igual que los relojes de los
 * perros, no sabe dibujar: recibe una accion "alMoverse" que ejecuta tras
 * cada paso (normalmente, redibujar en la UI).
 *
 * El vigilante solo se mueve si esta activo (Lupin ya entro a la boveda 2);
 * mientras no lo este, el reloj sigue corriendo pero el vigilante no se mueve.
 */
public class RelojVigilante implements Runnable {

    /** Accion a ejecutar despues de cada paso. */
    public interface AlMoverse {
        void ejecutar();
    }

    private final Vigilante vigilante;
    private final Lupin lupin;
    private final Tablero tablero;
    private final long periodoMs;
    private final Object candado;
    private final AlMoverse alMoverse;

    private volatile boolean activo = true;
    private Thread hilo;

    public RelojVigilante(Vigilante vigilante, Lupin lupin, Tablero tablero,
                          long periodoMs, Object candado, AlMoverse alMoverse) {
        this.vigilante = vigilante;
        this.lupin = lupin;
        this.tablero = tablero;
        this.periodoMs = periodoMs;
        this.candado = candado;
        this.alMoverse = alMoverse;
    }

    public void iniciar() {
        hilo = new Thread(this, "reloj-vigilante");
        hilo.setDaemon(true);
        hilo.start();
    }

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
            synchronized (candado) {
                vigilante.perseguir(tablero, lupin);
            }
            if (alMoverse != null) {
                alMoverse.ejecutar();
            }
        }
    }
}
