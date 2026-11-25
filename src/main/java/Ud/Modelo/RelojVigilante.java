package Ud.Modelo;

public class RelojVigilante implements Runnable {


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
