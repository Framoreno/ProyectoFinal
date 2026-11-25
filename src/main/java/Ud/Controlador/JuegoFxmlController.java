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
     * el juego arranca cuando ya existe la escena
     */
    public void iniciar(Scene escena) {
        controladorJuego.iniciar(escena);
    }

    public void detener() {
        if (controladorJuego != null) {
            controladorJuego.detener();
        }
    }


    @Override
    public void alSerAtrapado(int vidasRestantes) {
        mostrarVidas();
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Atrapado");
        alerta.setHeaderText(null);
        alerta.setContentText("Lupin ha sido atrapado, quedan " + vidasRestantes + " vidas");
        alerta.showAndWait();
    }

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

    private void mostrarVidas() {
        etiquetaVidas.setText("Vidas: " + juego.getVidas());
    }
}
