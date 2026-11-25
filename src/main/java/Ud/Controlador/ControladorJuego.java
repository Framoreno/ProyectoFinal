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
public class ControladorJuego {

    public interface ObservadorJuego {
        void alSerAtrapado(int vidasRestantes);

        void alTerminarJuego();

        void alGanar();
    }

    private static final long[] PERIODOS_MS = {500, 800, 1200};
    private static final long PERIODO_VIGILANTE_MS = 300;

    private final JuegoLupin juego;
    private final VistaTablero vista;
    private final List<RelojPerro> relojes = new ArrayList<>();
    private final List<RelojVigilante> relojesVigilantes = new ArrayList<>();

    private final Object candado = new Object();

    private ObservadorJuego observador;

    private boolean procesandoAtrapada = false;

    public ControladorJuego(JuegoLupin juego, VistaTablero vista) {
        this.juego = juego;
        this.vista = vista;
    }

    public void setObservador(ObservadorJuego observador) {
        this.observador = observador;
    }


    public void iniciar(Scene escena) {
        vista.dibujarLupin(juego.getLupin());
        vista.prepararPerros(juego.getPerros());
        vista.dibujarPerros(juego.getPerros());
        vista.prepararVigilantes(juego.getVigilantes());
        vista.dibujarVigilantes(juego.getVigilantes());

        escena.addEventHandler(KeyEvent.KEY_PRESSED, this::manejarTecla);
        arrancarRelojesPerros();
        arrancarRelojesVigilantes();
    }

    public void detener() {
        for (RelojPerro reloj : relojes) {
            reloj.detener();
        }
        relojes.clear();
        for (RelojVigilante rv : relojesVigilantes) {
            rv.detener();
        }
        relojesVigilantes.clear();
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
                    // hace que la vista espere al modelo
                    () -> Platform.runLater(() -> {
                        vista.dibujarPerros(perros);
                        verificarAtrapada();
                    }));
            relojes.add(reloj);
            reloj.iniciar();
        }
    }

    private void arrancarRelojesVigilantes() {
        for (var vigilante : juego.getVigilantes()) {
            RelojVigilante rv = new RelojVigilante(
                    vigilante,
                    juego.getLupin(),
                    juego.getTablero(),
                    PERIODO_VIGILANTE_MS,
                    candado,
                    () -> Platform.runLater(() -> {
                        vista.dibujarVigilantes(juego.getVigilantes());
                        verificarAtrapada();
                    }));
            relojesVigilantes.add(rv);
            rv.iniciar();
        }
    }

    private void manejarTecla(KeyEvent evento) {
        Direccion direccion = traducir(evento.getCode());
        if (direccion != null) {
            synchronized (candado) {
                juego.moverLupin(direccion);
                juego.activarVigilante();
            }
            vista.dibujarLupin(juego.getLupin());
            vista.actualizarLlaves();
            if (!verificarVictoria()) {
                verificarAtrapada();
            }
            evento.consume();
        }
    }

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

    private void verificarAtrapada() {
        if (procesandoAtrapada
                || !(juego.perroAtrapoALupin() || juego.vigilanteAtrapoALupin())) {
            return;
        }
        procesandoAtrapada = true;
        detener();
        juego.aplicarAtrapada();   // restar vida y reiniciar posiciones

        if (juego.juegoTerminado()) {
            if (observador != null) {
                observador.alTerminarJuego();
            }
            return;
        }

        vista.dibujarLupin(juego.getLupin());
        vista.dibujarPerros(juego.getPerros());
        vista.dibujarVigilantes(juego.getVigilantes());

        if (observador != null) {
            observador.alSerAtrapado(juego.getVidas());
        }

        procesandoAtrapada = false;
        arrancarRelojesPerros();
        arrancarRelojesVigilantes();
    }

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
