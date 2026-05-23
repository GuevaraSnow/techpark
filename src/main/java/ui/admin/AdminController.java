package ui.admin;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class AdminController {

    @FXML
    private StackPane panelCentral;

    @FXML
    void mostrarZonas() {
        panelCentral.getChildren().clear();
    }

    @FXML
    void mostrarAtracciones() {
        panelCentral.getChildren().clear();
    }

    @FXML
    void mostrarEmpleados() {
        panelCentral.getChildren().clear();
    }

    @FXML
    void mostrarAlertas() {
        panelCentral.getChildren().clear();
    }

    @FXML
    void mostrarEstadisticas() {
        panelCentral.getChildren().clear();
    }

    @FXML
    void mostrarMapa() {
        panelCentral.getChildren().clear();
    }
}