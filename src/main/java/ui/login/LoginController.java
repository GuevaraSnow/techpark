package ui.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Administrador;
import model.Empleado;
import model.Operador;
import model.Visitante;
import techpark.Main;

public class LoginController {

    @FXML private TextField     txtDocumento;
    @FXML private PasswordField txtPassword;
    @FXML private Label         lblError;

    @FXML
    void ingresar() {
        String documento = txtDocumento.getText().trim();
        String password  = txtPassword.getText().trim();

        if (documento.isEmpty() || password.isEmpty()) {
            lblError.setText("⚠ Completa todos los campos.");
            return;
        }

        // ── 1. Verificar Admin ────────────────────────────────────
        if (documento.equals("admin") && password.equals("admin")) {
            // Buscar o crear administrador activo
            for (int i = 0; i < Main.parque.getEmpleados().tamaño(); i++) {
                Empleado emp = Main.parque.getEmpleados().obtener(i);
                if (emp instanceof Administrador
                        && emp.getDocumento().equals("admin")) {
                    Main.administradorActivo = (Administrador) emp;
                    break;
                }
            }
            if (Main.administradorActivo == null) {
                Main.administradorActivo = new Administrador(
                        "Administrador Principal", "admin");
            }
            navegar("/ui/admin/AdminView.fxml", "TechPark — Administrador");
            return;
        }

        // ── 2. Verificar Visitante ────────────────────────────────
        // Contraseña = documento
        Visitante visitante = Main.parque.buscarVisitante(documento);
        if (visitante != null && password.equals(documento)) {
            Main.visitanteActivo = visitante;
            navegar("/ui/visitante/VisitanteView.fxml", "TechPark — Visitante");
            return;
        }

        // ── 3. Verificar Operador ─────────────────────────────────
        // Contraseña = documento
        for (int i = 0; i < Main.parque.getEmpleados().tamaño(); i++) {
            Empleado emp = Main.parque.getEmpleados().obtener(i);
            if (emp instanceof Operador
                    && emp.getDocumento().equals(documento)
                    && password.equals(documento)) {
                Main.operadorActivo = (Operador) emp;
                navegar("/ui/operador/OperadorView.fxml", "TechPark — Operador");
                return;
            }
        }

        // ── 4. Credenciales incorrectas ───────────────────────────
        lblError.setText("❌ Documento o contraseña incorrectos.");
        txtPassword.clear();
    }

    private void navegar(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) txtDocumento.getScene().getWindow();
            stage.setTitle(titulo);
            stage.setScene(scene);
        } catch (Exception e) {
            lblError.setText("❌ Error al cargar la pantalla: " + e.getMessage());
            e.printStackTrace();
        }
    }
}