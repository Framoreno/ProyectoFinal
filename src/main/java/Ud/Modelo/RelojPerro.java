package Ud.Modelo;

import java.util.List;


public class RelojPerro implements Runnable {

  
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


    public RelojPerro(Perro perro, List<Perro> todosLosPerros, Tablero tablero,
                      long periodoMs, Object candado, AlMoverse alMoverse) {
        this.perro = perro;
        this.todosLosPerros = todosLosPerros;
        this.tablero = tablero;
        this.periodoMs = periodoMs;
        this.candado = candado;
        this.alMoverse = alMoverse;
    }

    public void iniciar() {
        hilo = new Thread(this, "reloj-perro");
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
                perro.moverAlAzar(tablero, todosLosPerros);
            }
            if (alMoverse != null) {
                alMoverse.ejecutar();
            }
        }
    }
}
