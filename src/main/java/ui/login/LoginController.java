package ui.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import techpark.Main;

public class LoginController {

    @FXML private Button btnAdmin;
    @FXML private Button btnVisitante;
    @FXML private Button btnOperador;

    @FXML
    void ingresarComoAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/admin/AdminView.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) btnAdmin.getScene().getWindow();
            stage.setTitle("TechPark — Administrador");
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ingresarComoVisitante() {
        try {
            // Seleccionar visitante del parque
            if (Main.parque.getVisitantes().estaVacia()) {
                javafx.scene.control.Alert alert =
                        new javafx.scene.control.Alert(
                                javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Sin visitantes");
                alert.setHeaderText(null);
                alert.setContentText("No hay visitantes registrados en el sistema.");
                alert.showAndWait();
                return;
            }

            // Usar el primer visitante del parque como sesión activa
            Main.visitanteActivo = Main.parque.getVisitantes().obtener(0);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/visitante/VisitanteView.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) btnVisitante.getScene().getWindow();
            stage.setTitle("TechPark — Visitante");
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ingresarComoOperador() {
        try {
            if (Main.parque.getEmpleados().estaVacia()) {
                javafx.scene.control.Alert alert =
                        new javafx.scene.control.Alert(
                                javafx.scene.control.Alert.AlertType.WARNING);
                alert.setTitle("Sin operadores");
                alert.setHeaderText(null);
                alert.setContentText("No hay operadores registrados en el sistema.");
                alert.showAndWait();
                return;
            }

            // Usar el primer operador como sesión activa
            Main.operadorActivo = (model.Operador)
                    Main.parque.getEmpleados().obtener(0);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ui/operador/OperadorView.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) btnOperador.getScene().getWindow();
            stage.setTitle("TechPark — Operador");
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}