package Ud.Vista;

import Ud.Controlador.JuegoFxmlController;
import Ud.Modelo.JuegoLupin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX.
 *
 * Arma las tres capas del patron MVC y las conecta usando FXML:
 *   Modelo      -> JuegoLupin (tablero + Lupin + perros)
 *   Vista       -> Juego.fxml (texto de vidas + contenedor) + VistaTablero
 *   Controlador -> JuegoFxmlController (une FXML con modelo y vista)
 *
 * La ventana se carga desde Juego.fxml; la grilla del tablero (que cambia en
 * vivo) se inyecta dentro del contenedor del FXML por codigo.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JuegoLupin juego = new JuegoLupin();
        VistaTablero vista = new VistaTablero(juego.getTablero());

        // Cargar la escena desde el FXML y obtener su controlador.
        FXMLLoader cargador = new FXMLLoader(getClass().getResource(Paths.ESCENA_JUEGO));
        Parent raiz = cargador.load();
        JuegoFxmlController controlador = cargador.getController();

        // Pasar modelo y vista al controlador del FXML.
        controlador.configurar(juego, vista);

        Scene escena = new Scene(raiz);
        controlador.iniciar(escena);

        // Al cerrar la ventana, detener los relojes de los perros.
        primaryStage.setOnCloseRequest(e -> controlador.detener());

        primaryStage.setTitle("Proyecto Lupin - Tablero");
        primaryStage.setScene(escena);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
