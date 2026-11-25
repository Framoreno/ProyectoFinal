package Ud.Vista;

import Ud.Controlador.JuegoFxmlController;
import Ud.Modelo.JuegoLupin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * arma las tres capas del patron MVC y las conecta usando FXML
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JuegoLupin juego = new JuegoLupin();
        VistaTablero vista = new VistaTablero(juego.getTablero());

        // cargar la escena desde el FXML y obtener controlador
        FXMLLoader cargador = new FXMLLoader(getClass().getResource(Paths.ESCENA_JUEGO));
        Parent raiz = cargador.load();
        JuegoFxmlController controlador = cargador.getController();

        // pasar modelo y vista al controlador del FXML
        controlador.configurar(juego, vista);

        Scene escena = new Scene(raiz);
        controlador.iniciar(escena);

        // al cerrar la ventana, detener los relojes de los perros
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
