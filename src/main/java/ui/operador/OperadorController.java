package ui.operador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Operador;
import model.Parque;
import model.Visitante;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import model.atraccion.Zona;
import model.estructuras.ListaEnlazada;
import model.gestores.ControlAcceso;
import model.gestores.GestorColas;
import techpark.Main;

public class OperadorController {

    @FXML private StackPane panelCentral;

    private Parque parque;
    private Operador operador;
    private Zona zonaAsignada;
    private GestorColas gestorColas;
    private ControlAcceso controlAcceso;

    @FXML
    public void initialize() {
        this.parque       = Main.parque;
        this.operador     = Main.operadorActivo;
        this.gestorColas  = new GestorColas();
        this.controlAcceso = new ControlAcceso();

        // Buscar zona asignada al operador
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona z = parque.getZonas().obtener(i);
            for (int j = 0; j < z.getOperadores().tamaño(); j++) {
                if (z.getOperadores().obtener(j)
                        .getDocumento().equals(operador.getDocumento())) {
                    zonaAsignada = z;
                    break;
                }
            }
        }

        // Registrar atracciones de la zona en el gestor de colas
        if (zonaAsignada != null) {
            for (int i = 0; i < zonaAsignada.getAtracciones().tamaño(); i++) {
                gestorColas.registrarAtraccion(
                        zonaAsignada.getAtracciones().obtener(i));
            }
        }

        mostrarAtraccion();
    }

    // ── Mi Atracción ──────────────────────────────────────────────
    @FXML
    void mostrarAtraccion() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("🎢 Mi Zona Asignada");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

        if (zonaAsignada == null) {
            panel.getChildren().addAll(titulo,
                    new Label("⚠ No tienes una zona asignada."));
            panelCentral.getChildren().add(panel);
            return;
        }

        Label lblZona = new Label("📍 Zona: " + zonaAsignada.getNombre());
        lblZona.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

        TableView<String[]> tabla = new TableView<>();
        TableColumn<String[], String> colNombre     = new TableColumn<>("Atracción");
        TableColumn<String[], String> colEstado     = new TableColumn<>("Estado");
        TableColumn<String[], String> colVisitantes = new TableColumn<>("Visitantes");
        TableColumn<String[], String> colCapacidad  = new TableColumn<>("Capacidad");
        TableColumn<String[], String> colEspera     = new TableColumn<>("Espera");

        colNombre.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[0]));
        colEstado.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[1]));
        colVisitantes.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[2]));
        colCapacidad.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[3]));
        colEspera.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[4]));

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

        tabla.getColumns().addAll(colNombre, colEstado,
                colVisitantes, colCapacidad, colEspera);

        for (int i = 0; i < zonaAsignada.getAtracciones().tamaño(); i++) {
            Atraccion a = zonaAsignada.getAtracciones().obtener(i);
            tabla.getItems().add(new String[]{
                    a.getNombre(),
                    a.getEstado().toString(),
                    String.valueOf(a.getContadorVisitantes()),
                    String.valueOf(a.getCapacidad()),
                    "~" + (int) a.getTiempoEspera() + " min"
            });
        }

        panel.getChildren().addAll(titulo, lblZona, tabla);
        panelCentral.getChildren().add(panel);
    }

    // ── Cola de visitantes ────────────────────────────────────────
    @FXML
    void mostrarCola() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("👥 Cola de Visitantes");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

        if (zonaAsignada == null || zonaAsignada.getAtracciones().estaVacia()) {
            panel.getChildren().addAll(titulo,
                    new Label("No hay atracciones en tu zona."));
            panelCentral.getChildren().add(panel);
            return;
        }

        // Seleccionar atracción
        ComboBox<String> cbAtraccion = new ComboBox<>();
        for (int i = 0; i < zonaAsignada.getAtracciones().tamaño(); i++) {
            cbAtraccion.getItems().add(
                    zonaAsignada.getAtracciones().obtener(i).getNombre());
        }
        cbAtraccion.setPromptText("Seleccionar atracción");

        ListView<String> listaCola = new ListView<>();
        listaCola.setPrefHeight(280);

        Label lblInfo = new Label("");
        lblInfo.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");

        cbAtraccion.setOnAction(e -> {
            String nombre = cbAtraccion.getValue();
            if (nombre == null) return;
            Atraccion a = parque.buscarAtraccion(nombre);
            if (a == null) return;
            listaCola.getItems().clear();
            int tam = gestorColas.tamañoCola(a);
            if (tam == 0) {
                listaCola.getItems().add("✅ Cola vacía");
            } else {
                listaCola.getItems().add("Total en cola: " + tam + " visitantes");
                listaCola.getItems().add("👑 Siguiente: "
                        + gestorColas.verSiguiente(a).getNombre()
                        + " ("  + gestorColas.verSiguiente(a)
                        .getTicket().getTipo() + ")");
            }
            lblInfo.setText("Capacidad del ciclo: "
                    + a.getContadorActual() + " / " + a.getCapacidad());
        });

        // Botón procesar siguiente
        Button btnProcesar = new Button("▶ Procesar Siguiente");
        btnProcesar.setStyle("-fx-background-color: #4A235A; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnProcesar.setOnAction(e -> {
            String nombre = cbAtraccion.getValue();
            if (nombre == null) return;
            Atraccion a = parque.buscarAtraccion(nombre);
            if (a == null || gestorColas.colaVacia(a)) {
                alerta("Cola vacía", "No hay visitantes en la cola.");
                return;
            }

            Visitante siguiente = gestorColas.verSiguiente(a);
            ControlAcceso.ResultadoValidacion resultado =
                    controlAcceso.autorizarIngreso(siguiente, a);

            if (resultado.aprobado) {
                gestorColas.desencolarConNotificacion(a);
                alerta("✅ Ingreso autorizado", resultado.mensaje);
            } else {
                gestorColas.desencolar(a); // sacar de cola aunque no pase
                alerta("❌ Ingreso denegado", resultado.mensaje);
            }

            // Refrescar
            cbAtraccion.getOnAction().handle(null);
        });

        HBox filtro = new HBox(10,
                new Label("Atracción:"), cbAtraccion);
        filtro.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        panel.getChildren().addAll(titulo, filtro, listaCola, lblInfo, btnProcesar);
        panelCentral.getChildren().add(panel);
    }