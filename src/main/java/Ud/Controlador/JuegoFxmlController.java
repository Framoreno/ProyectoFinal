package Ud.Controlador;

import Ud.Modelo.JuegoLupin;
import Ud.Modelo.RepositorioResultados;
import Ud.Vista.VistaTablero;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Optional;

/**
 * Controlador de la escena FXML (Juego.fxml), capa Controlador del patron MVC.
 *
 * Conecta lo que declara el FXML (el texto de vidas y el contenedor del
 * tablero) con el modelo y la vista:
 *   - mete la grilla del tablero (VistaTablero) dentro del contenedor;
 *   - muestra el texto de las vidas y lo actualiza cuando cambian;
 *   - delega el teclado y los relojes de los perros en ControladorJuego;
 *   - como ObservadorJuego, muestra la alerta cuando atrapan a Lupin y el
 *     mensaje de fin de juego.
 */
public class JuegoFxmlController implements ControladorJuego.ObservadorJuego {

    @FXML
    private Label etiquetaVidas;

    @FXML
    private StackPane contenedorTablero;

    private JuegoLupin juego;
    private VistaTablero vista;
    private ControladorJuego controladorJuego;

    private final RepositorioResultados repositorio = new RepositorioResultados();
    private String nombreJugador = "Anonimo";

    /**
     * Recibe el modelo y la vista ya creados y arma la escena: pide el nombre
     * del jugador, coloca la grilla en el contenedor, dibuja las llaves y
     * muestra las vidas.
     */
    public void configurar(JuegoLupin juego, VistaTablero vista) {
        this.juego = juego;
        this.vista = vista;
        this.controladorJuego = new ControladorJuego(juego, vista);
        this.controladorJuego.setObservador(this);

        pedirNombre();

        vista.ajustarTamano();
        contenedorTablero.getChildren().setAll(vista);
        vista.dibujarLlaves(juego.getLlaves());
        mostrarVidas();
    }

    /** Pide el nombre del jugador en un dialogo al empezar. */
    private void pedirNombre() {
        TextInputDialog dialogo = new TextInputDialog("Jugador");
        dialogo.setTitle("Nombre del jugador");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Escribe tu nombre:");
        Optional<String> respuesta = dialogo.showAndWait();
        if (respuesta.isPresent() && !respuesta.get().isBlank()) {
            nombreJugador = respuesta.get().trim();
        }
    }

    /**
     * Arranca la parte de juego (teclado + perros) una vez la escena existe.
     */
    public void iniciar(Scene escena) {
        controladorJuego.iniciar(escena);
    }

    /** Detiene los relojes de los perros (al cerrar la ventana). */
    public void detener() {
        if (controladorJuego != null) {
            controladorJuego.detener();
        }
    }

    // --- ObservadorJuego ---

    /**
     * Lupin fue atrapado: muestra la alerta con las vidas que quedan y refresca
     * el texto. La alerta es bloqueante; el juego ya reinicio posiciones antes.
     */
    @Override
    public void alSerAtrapado(int vidasRestantes) {
        mostrarVidas();
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Atrapado");
        alerta.setHeaderText(null);
        alerta.setContentText("Lupin ha sido atrapado, quedan " + vidasRestantes + " vidas");
        alerta.showAndWait();
    }

    /** Lupin se quedo sin vidas: guarda el resultado, muestra el fin y cierra. */
    @Override
    public void alTerminarJuego() {
        mostrarVidas();
        guardarResultado("ATRAPADO", juego.getVidas());
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Fin del juego");
        alerta.setHeaderText(null);
        alerta.setContentText("Lupin ha sido atrapado, quedan 0 vidas. Fin del juego.");
        alerta.showAndWait();
        Platform.exit();
    }

    /** Lupin llego al tesoro: guarda el resultado, muestra que gano y cierra. */
    @Override
    public void alGanar() {
        guardarResultado("GANO", juego.getVidas());
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Victoria");
        alerta.setHeaderText(null);
        alerta.setContentText("Lupin ha ganado! Se robo el tesoro.");
        alerta.showAndWait();
        Platform.exit();
    }

    /**
     * Guarda el resultado de la partida en el archivo de texto. Si falla la
     * escritura, avisa con una alerta pero no interrumpe el cierre.
     */
    private void guardarResultado(String resultado, int vidas) {
        try {
            repositorio.guardar(nombreJugador, resultado, vidas);
        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.WARNING);
            error.setTitle("Aviso");
            error.setHeaderText(null);
            error.setContentText("No se pudo guardar el resultado: " + e.getMessage());
            error.showAndWait();
        }
    }

    /** Muestra las vidas actuales de Lupin en el texto de arriba. */
    private void mostrarVidas() {
        etiquetaVidas.setText("Vidas: " + juego.getVidas());
    }
}
