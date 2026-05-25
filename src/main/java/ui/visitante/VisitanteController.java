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

    // ── Atracciones ───────────────────────────────────────────────
    @FXML
    void mostrarAtracciones() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("🎢 Atracciones Disponibles");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        // Filtro por zona
        ComboBox<String> cbZona = new ComboBox<>();
        cbZona.getItems().add("Todas las zonas");
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            cbZona.getItems().add(parque.getZonas().obtener(i).getNombre());
        }
        cbZona.setValue("Todas las zonas");

        TableView<String[]> tabla = new TableView<>();
        TableColumn<String[], String> colNombre   = new TableColumn<>("Atracción");
        TableColumn<String[], String> colZona     = new TableColumn<>("Zona");
        TableColumn<String[], String> colEstado   = new TableColumn<>("Estado");
        TableColumn<String[], String> colEspera   = new TableColumn<>("Espera");
        TableColumn<String[], String> colAltMin   = new TableColumn<>("Alt. mín.");
        TableColumn<String[], String> colEdadMin  = new TableColumn<>("Edad mín.");
        TableColumn<String[], String> colCosto    = new TableColumn<>("Costo extra");

        colNombre.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[0]));
        colZona.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[1]));
        colEstado.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[2]));
        colEspera.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[3]));
        colAltMin.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[4]));
        colEdadMin.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[5]));
        colCosto.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[6]));

        // Colorear estado
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                switch (item) {
                    case "ACTIVA"        -> setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold;");
                    case "MANTENIMIENTO" -> setStyle("-fx-text-fill: #F39C12; -fx-font-weight: bold;");
                    case "CERRADA"       -> setStyle("-fx-text-fill: #C0392B; -fx-font-weight: bold;");
                }
            }
        });

        tabla.getColumns().addAll(colNombre, colZona, colEstado,
                colEspera, colAltMin, colEdadMin, colCosto);
        tabla.setPrefHeight(320);

        // Poblar tabla
        poblarTablaAtracciones(tabla, "Todas las zonas");

        // Filtrar al cambiar zona
        cbZona.setOnAction(e ->
                poblarTablaAtracciones(tabla, cbZona.getValue()));

        // Botones de acción
        Button btnFavorito = new Button("❤ Agregar a Favoritos");
        btnFavorito.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnFavorito.setOnAction(e -> {
            String[] fila = tabla.getSelectionModel().getSelectedItem();
            if (fila == null) { alerta("Aviso", "Selecciona una atracción."); return; }
            Atraccion a = parque.buscarAtraccion(fila[0]);
            if (a != null) {
                boolean agregado = visitante.agregarFavorito(a);
                alerta("Favoritos", agregado ?
                        "❤ " + a.getNombre() + " agregada a favoritos." :
                        "Ya está en tus favoritos.");
            }
        });

        Button btnCola = new Button("🎫 Unirme a la Cola");
        btnCola.setStyle("-fx-background-color: #1F3864; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnCola.setOnAction(e -> {
            String[] fila = tabla.getSelectionModel().getSelectedItem();
            if (fila == null) { alerta("Aviso", "Selecciona una atracción."); return; }
            Atraccion a = parque.buscarAtraccion(fila[0]);
            if (a == null) return;

            ControlAcceso.ResultadoValidacion resultado =
                    controlAcceso.validarRestricciones(visitante, a);
            if (!resultado.aprobado) {
                alerta("Acceso denegado", resultado.mensaje);
                return;
            }

            if (a.getEstado() != EstadoAtraccion.ACTIVA) {
                alerta("No disponible", "La atracción no está activa.");
                return;
            }

            boolean encolado = gestorColas.encolarSeguro(visitante, a);
            if (encolado) {
                int pos = gestorColas.getPosicion(visitante, a);
                alerta("Cola virtual",
                        "✅ Te uniste a la cola de " + a.getNombre()
                                + "\n📍 Tu posición: #" + pos);
            } else {
                alerta("Cola virtual", "Ya estás en la cola de esta atracción.");
            }
        });

        HBox acciones = new HBox(10, btnFavorito, btnCola);
        HBox filtro = new HBox(10,
                new Label("Filtrar por zona:"), cbZona);
        filtro.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        panel.getChildren().addAll(titulo, filtro, tabla, acciones);
        panelCentral.getChildren().add(panel);
    }

    private void poblarTablaAtracciones(TableView<String[]> tabla, String filtroZona) {
        tabla.getItems().clear();
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona z = parque.getZonas().obtener(i);
            if (!filtroZona.equals("Todas las zonas")
                    && !z.getNombre().equals(filtroZona)) continue;
            for (int j = 0; j < z.getAtracciones().tamaño(); j++) {
                Atraccion a = z.getAtracciones().obtener(j);
                tabla.getItems().add(new String[]{
                        a.getNombre(),
                        z.getNombre(),
                        a.getEstado().toString(),
                        "~" + (int) a.getTiempoEspera() + " min",
                        a.getAlturaMin() + " m",
                        a.getEdadMin() + " años",
                        a.getCostoExtra() > 0 ? "$" + (int) a.getCostoExtra() : "Gratis"
                });
            }
        }
    }

    // ── Favoritos ─────────────────────────────────────────────────
    @FXML
    void mostrarFavoritos() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("❤ Mis Favoritos");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        ListView<String> lista = new ListView<>();

        ListaEnlazada<Atraccion> favs = visitante.getFavoritos().listar();
        if (favs.estaVacia()) {
            lista.getItems().add("No tienes atracciones favoritas aún.");
        } else {
            for (int i = 0; i < favs.tamaño(); i++) {
                Atraccion a = favs.obtener(i);
                lista.getItems().add("❤ " + a.getNombre()
                        + "  |  " + a.getEstado()
                        + "  |  " + a.getTipo());
            }
        }

        Button btnEliminar = new Button("🗑 Eliminar de Favoritos");
        btnEliminar.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnEliminar.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx < 0 || favs.estaVacia()) return;
            visitante.eliminarFavorito(favs.obtener(idx));
            mostrarFavoritos();
        });

        panel.getChildren().addAll(titulo, lista, btnEliminar);
        panelCentral.getChildren().add(panel);
    }

    // ── Historial ─────────────────────────────────────────────────
    @FXML
    void mostrarHistorial() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("📋 Historial de Visitas");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        TableView<String[]> tabla = new TableView<>();
        TableColumn<String[], String> colNum    = new TableColumn<>("#");
        TableColumn<String[], String> colNombre = new TableColumn<>("Atracción");
        TableColumn<String[], String> colTipo   = new TableColumn<>("Tipo");
        TableColumn<String[], String> colEstado = new TableColumn<>("Estado actual");

        colNum.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[0]));
        colNombre.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[1]));
        colTipo.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[2]));
        colEstado.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[3]));

        tabla.getColumns().addAll(colNum, colNombre, colTipo, colEstado);

        ListaEnlazada<Atraccion> historial = visitante.getHistorial();
        if (historial.estaVacia()) {
            tabla.setPlaceholder(new Label("No has visitado ninguna atracción aún."));
        } else {
            for (int i = 0; i < historial.tamaño(); i++) {
                Atraccion a = historial.obtener(i);
                tabla.getItems().add(new String[]{
                        String.valueOf(i + 1),
                        a.getNombre(),
                        a.getTipo(),
                        a.getEstado().toString()
                });
            }
        }

        Label lblTotal = new Label("Total de visitas: " + historial.tamaño());
        lblTotal.setStyle("-fx-font-size: 12; -fx-text-fill: #555; -fx-padding: 4 0 0 0;");

        panel.getChildren().addAll(titulo, tabla, lblTotal);
        panelCentral.getChildren().add(panel);
    }


