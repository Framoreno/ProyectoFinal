package Ud.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class EscenaPrincipalController {

    @FXML
    private Button Iniciar;

    @FXML
    private Text Titulo;

    @FXML
    void click(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Hola Tonotos");
        alerta.setHeaderText("Hola Mundo");
        alerta.setContentText("Este es un mensaje de prueba");
        alerta.showAndWait();
    }

}