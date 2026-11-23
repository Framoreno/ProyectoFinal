package Ud.Controlador;

import Ud.Modelo.Direccion;
import Ud.Modelo.JuegoLupin;
import Ud.Modelo.Perro;
import Ud.Modelo.RelojPerro;
import Ud.Modelo.RelojVigilante;
import Ud.Vista.VistaTablero;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador del juego (capa Controlador del patron MVC).
 *
 * Une el teclado y los relojes de los perros con el modelo y la vista:
 *  - lee la tecla que oprime el jugador y mueve a Lupin;
 *  - arranca un reloj independiente por cada perro (cada uno con su propia
 *    velocidad) y, cuando un perro se mueve, pide a la vista redibujar;
 *  - detecta cuando un perro atrapa a Lupin: detiene los relojes, avisa para
 *    mostrar la alerta, resta una vida, reinicia posiciones y vuelve a arrancar
 *    (o termina si ya no quedan vidas).
 *
 * No conoce colores ni sprites (eso es de la Vista) ni calcula movimientos
 * (eso es del Modelo): solo coordina.
 */
public class ControladorJuego {

    /**
     * Aviso hacia la capa de presentacion cuando Lupin es atrapado o el juego
     * termina. Lo implementa el controlador FXML (muestra la alerta y refresca
     * el texto de vidas). Mantiene a este controlador sin depender de JavaFX
     * para la ventana (SOLID).
     */
    public interface ObservadorJuego {
        /** Lupin fue atrapado y aun le quedan vidas. */
        void alSerAtrapado(int vidasRestantes);

        /** Lupin se quedo sin vidas: fin del juego. */
        void alTerminarJuego();

        /** Lupin llego al tesoro: gano el juego. */
        void alGanar();
    }

    // Cada perro se mueve a un tiempo distinto (milisegundos entre pasos).
    private static final long[] PERIODOS_MS = {500, 800, 1200};
    // Velocidad del vigilante (milisegundos entre pasos). Mas bajo = mas rapido.
    private static final long PERIODO_VIGILANTE_MS = 450;

    private final JuegoLupin juego;
    private final VistaTablero vista;
    private final List<RelojPerro> relojes = new ArrayList<>();
    private RelojVigilante relojVigilante;

    // Candado compartido: los pasos de los tres perros no se pisan entre si.
    private final Object candado = new Object();

    private ObservadorJuego observador;
    // Evita procesar varias atrapadas a la vez (los 3 hilos podrian coincidir).
    private boolean procesandoAtrapada = false;

    public ControladorJuego(JuegoLupin juego, VistaTablero vista) {
        this.juego = juego;
        this.vista = vista;
    }

    public void setObservador(ObservadorJuego observador) {
        this.observador = observador;
    }

    /**
     * Dibuja a Lupin y a los perros, empieza a escuchar el teclado y arranca
     * el reloj independiente de cada perro.
     */
    public void iniciar(Scene escena) {
        vista.dibujarLupin(juego.getLupin());
        vista.prepararPerros(juego.getPerros());
        vista.dibujarPerros(juego.getPerros());
        vista.prepararVigilante();
        vista.dibujarVigilante(juego.getVigilante());

        escena.addEventHandler(KeyEvent.KEY_PRESSED, this::manejarTecla);
        arrancarRelojesPerros();
        arrancarRelojVigilante();
    }

    /** Detiene todos los relojes (p. ej. al cerrar la ventana o al atrapar). */
    public void detener() {
        for (RelojPerro reloj : relojes) {
            reloj.detener();
        }
        relojes.clear();
        if (relojVigilante != null) {
            relojVigilante.detener();
            relojVigilante = null;
        }
    }

    private void arrancarRelojesPerros() {
        List<Perro> perros = juego.getPerros();
        for (int i = 0; i < perros.size(); i++) {
            long periodo = PERIODOS_MS[i % PERIODOS_MS.length];
            RelojPerro reloj = new RelojPerro(
                    perros.get(i),
                    perros,
                    juego.getTablero(),
                    periodo,
                    candado,
                    // Tras cada paso: redibujar y revisar atrapada, en el hilo
                    // de JavaFX.
                    () -> Platform.runLater(() -> {
                        vista.dibujarPerros(perros);
                        verificarAtrapada();
                    }));
            relojes.add(reloj);
            reloj.iniciar();
        }
    }

    private void arrancarRelojVigilante() {
        relojVigilante = new RelojVigilante(
                juego.getVigilante(),
                juego.getLupin(),
                juego.getTablero(),
                PERIODO_VIGILANTE_MS,
                candado,
                () -> Platform.runLater(() -> {
                    vista.dibujarVigilante(juego.getVigilante());
                    verificarAtrapada();
                }));
        relojVigilante.iniciar();
    }

    private void manejarTecla(KeyEvent evento) {
        Direccion direccion = traducir(evento.getCode());
        if (direccion != null) {
            synchronized (candado) {
                juego.moverLupin(direccion);
                juego.activarVigilanteSiCorresponde();
            }
            vista.dibujarLupin(juego.getLupin());
            vista.actualizarLlaves();
            if (!verificarVictoria()) {
                verificarAtrapada();
            }
            evento.consume();
        }
    }

    /**
     * Si Lupin llego al tesoro, gana: detiene los relojes y avisa al
     * observador. Debe llamarse en el hilo de JavaFX. Devuelve true si gano.
     */
    private boolean verificarVictoria() {
        if (!juego.lupinGano()) {
            return false;
        }
        detener();
        if (observador != null) {
            observador.alGanar();
        }
        return true;
    }

    /**
     * Comprueba si un perro atrapo a Lupin. Debe llamarse en el hilo de JavaFX.
     * Si hay atrapada: detiene relojes, resta una vida y avisa al observador.
     * El observador (la ventana) decide mostrar la alerta y luego, si quedan
     * vidas, se reinicia y se vuelve a arrancar.
     */
    private void verificarAtrapada() {
        if (procesandoAtrapada
                || !(juego.perroAtrapoALupin() || juego.vigilanteAtrapoALupin())) {
            return;
        }
        procesandoAtrapada = true;
        detener();                 // 1. detener los relojes
        juego.aplicarAtrapada();   // 2. restar vida y reiniciar posiciones

        if (juego.juegoTerminado()) {
            if (observador != null) {
                observador.alTerminarJuego();
            }
            return; // no se vuelve a arrancar
        }

        // 3. redibujar YA en la posicion inicial (antes de la alerta, que
        //    bloquea), para que al cerrar el dialogo se vea el reinicio.
        vista.dibujarLupin(juego.getLupin());
        vista.dibujarPerros(juego.getPerros());
        vista.dibujarVigilante(juego.getVigilante());

        // 4. avisar: muestra la alerta con las vidas restantes (bloqueante).
        if (observador != null) {
            observador.alSerAtrapado(juego.getVidas());
        }

        // 5. volver a arrancar los relojes.
        procesandoAtrapada = false;
        arrancarRelojesPerros();
        arrancarRelojVigilante();
    }

    /**
     * Convierte una tecla en una direccion. Devuelve null si la tecla no
     * corresponde a un movimiento.
     */
    private Direccion traducir(KeyCode codigo) {
        return switch (codigo) {
            case UP, W -> Direccion.ARRIBA;
            case DOWN, S -> Direccion.ABAJO;
            case LEFT, A -> Direccion.IZQUIERDA;
            case RIGHT, D -> Direccion.DERECHA;
            default -> null;
        };
    }
}
