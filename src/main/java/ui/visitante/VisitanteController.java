package ui.visitante;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Parque;
import model.Visitante;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import model.atraccion.Zona;
import model.estructuras.ListaEnlazada;
import model.gestores.ControlAcceso;
import model.gestores.GestorColas;
import techpark.Main;

import java.util.HashMap;
import java.util.Map;

public class VisitanteController {

    @FXML private StackPane panelCentral;

    private Parque parque;
    private Visitante visitante;
    private GestorColas gestorColas;
    private ControlAcceso controlAcceso;

    @FXML
    public void initialize() {
        this.parque       = Main.parque;
        this.visitante    = Main.visitanteActivo;
        this.gestorColas  = new GestorColas();
        this.controlAcceso = new ControlAcceso();

        // Registrar todas las atracciones en el gestor de colas
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona z = parque.getZonas().obtener(i);
            for (int j = 0; j < z.getAtracciones().tamaño(); j++) {
                gestorColas.registrarAtraccion(z.getAtracciones().obtener(j));
            }
        }

        mostrarPerfil();
    }

    // ── Perfil ────────────────────────────────────────────────────
    @FXML
    void mostrarPerfil() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(16);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("👤 Mi Perfil");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        // Tarjeta de perfil
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-border-color: #D0D7E3; -fx-border-radius: 12; "
                + "-fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        tarjeta.setMaxWidth(500);

        Label lblNombre   = info("👤 Nombre:",    visitante.getNombre());
        Label lblDoc      = info("🪪 Documento:", visitante.getDocumento());
        Label lblEdad     = info("🎂 Edad:",      visitante.getEdad() + " años");
        Label lblEstatura = info("📏 Estatura:",  visitante.getEstatura() + " m");
        Label lblSaldo    = new Label("💰 Saldo:  $"
                + String.format("%.0f", visitante.getSaldo()));
        lblSaldo.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #27AE60;");
        Label lblTicket   = info("🎫 Ticket:",
                visitante.getTicket() != null ?
                        visitante.getTicket().getTipo().toString() : "Sin ticket");

        tarjeta.getChildren().addAll(lblNombre, lblDoc, lblEdad,
                lblEstatura, lblSaldo, lblTicket);

        // Recargar saldo
        Label lblRecargar = new Label("💳 Recargar saldo");
        lblRecargar.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        TextField txtMonto = new TextField();
        txtMonto.setPromptText("Monto a recargar ($)");
        txtMonto.setMaxWidth(200);

        Button btnRecargar = new Button("➕ Recargar");
        btnRecargar.setStyle("-fx-background-color: #1F3864; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnRecargar.setOnAction(e -> {
            try {
                double monto = Double.parseDouble(txtMonto.getText().trim());
                visitante.recargarSaldo(monto);
                mostrarPerfil();
            } catch (NumberFormatException ex) {
                alerta("Error", "Ingresa un monto numérico válido.");
            }
        });

        HBox recarga = new HBox(10, txtMonto, btnRecargar);
        panel.getChildren().addAll(titulo, tarjeta, lblRecargar, recarga);
        panelCentral.getChildren().add(panel);
    }

