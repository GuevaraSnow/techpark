package ui.admin;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Parque;
import model.atraccion.Atraccion;
import model.atraccion.Zona;
import model.estructuras.ListaEnlazada;
import techpark.Main;

public class AdminController {

    @FXML private StackPane panelCentral;

    private Parque parque;

    @FXML
    public void initialize() {
        this.parque = Main.parque;
    }

    // ── Zonas ─────────────────────────────────────────────────────
    @FXML
    void mostrarZonas() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🗺 Gestión de Zonas");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        ListView<String> listaZonas = new ListView<>();
        actualizarListaZonas(listaZonas);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la nueva zona");

        Button btnCrear = new Button("➕ Crear Zona");
        btnCrear.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                parque.agregarZona(new Zona(nombre));
                actualizarListaZonas(listaZonas);
                txtNombre.clear();
            }
        });

        HBox formulario = new HBox(10, txtNombre, btnCrear);
        panel.getChildren().addAll(titulo, listaZonas, formulario);
        panelCentral.getChildren().add(panel);
    }

    private void actualizarListaZonas(ListView<String> lista) {
        lista.getItems().clear();
        ListaEnlazada<Zona> zonas = parque.getZonas();
        for (int i = 0; i < zonas.tamaño(); i++) {
            Zona z = zonas.obtener(i);
            lista.getItems().add("📍 " + z.getNombre()
                    + "  |  Atracciones: " + z.getAtracciones().tamaño()
                    + "  |  Operadores: "  + z.getOperadores().tamaño());
        }
    }

    // ── Atracciones ───────────────────────────────────────────────
    @FXML
    void mostrarAtracciones() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🎢 Atracciones del Parque");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        TableView<String[]> tabla = new TableView<>();
        TableColumn<String[], String> colNombre   = new TableColumn<>("Nombre");
        TableColumn<String[], String> colTipo     = new TableColumn<>("Tipo");
        TableColumn<String[], String> colZona     = new TableColumn<>("Zona");
        TableColumn<String[], String> colEstado   = new TableColumn<>("Estado");
        TableColumn<String[], String> colVisitantes = new TableColumn<>("Visitantes");

        colNombre.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[0]));
        colTipo.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[1]));
        colZona.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[2]));
        colEstado.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[3]));
        colVisitantes.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[4]));

        // Colorear columna estado
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

        tabla.getColumns().addAll(colNombre, colTipo, colZona, colEstado, colVisitantes);

        // Poblar tabla desde zonas del parque
        ListaEnlazada<Zona> zonas = parque.getZonas();
        for (int i = 0; i < zonas.tamaño(); i++) {
            Zona z = zonas.obtener(i);
            ListaEnlazada<Atraccion> atracciones = z.getAtracciones();
            for (int j = 0; j < atracciones.tamaño(); j++) {
                Atraccion a = atracciones.obtener(j);
                tabla.getItems().add(new String[]{
                        a.getNombre(),
                        a.getTipo(),
                        z.getNombre(),
                        a.getEstado().toString(),
                        String.valueOf(a.getContadorVisitantes())
                });
            }
        }

        panel.getChildren().addAll(titulo, tabla);
        panelCentral.getChildren().add(panel);
    }

    // ── Empleados ─────────────────────────────────────────────────
    @FXML
    void mostrarEmpleados() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("👥 Gestión de Empleados");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        TableView<String[]> tabla = new TableView<>();
        TableColumn<String[], String> colNombre = new TableColumn<>("Nombre");
        TableColumn<String[], String> colDoc    = new TableColumn<>("Documento");
        TableColumn<String[], String> colCargo  = new TableColumn<>("Cargo");

        colNombre.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[0]));
        colDoc.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[1]));
        colCargo.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[2]));

        tabla.getColumns().addAll(colNombre, colDoc, colCargo);

        for (int i = 0; i < parque.getEmpleados().tamaño(); i++) {
            model.Empleado emp = parque.getEmpleados().obtener(i);
            tabla.getItems().add(new String[]{
                    emp.getNombre(),
                    emp.getDocumento(),
                    emp.getCargo()
            });
        }

        panel.getChildren().addAll(titulo, tabla);
        panelCentral.getChildren().add(panel);
    }

    // ── Alertas ───────────────────────────────────────────────────
    @FXML
    void mostrarAlertas() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🔔 Alertas de Mantenimiento");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        model.gestores.GestorMantenimiento gestor =
                new model.gestores.GestorMantenimiento(parque);
        gestor.verificarYGenerarAlertas();

        ListView<String> listaAlertas = new ListView<>();
        if (gestor.hayAlertas()) {
            while (gestor.hayAlertas()) {
                listaAlertas.getItems().add(gestor.atenderSiguienteAlerta());
            }
        } else {
            listaAlertas.getItems().add("✅ No hay alertas de mantenimiento pendientes.");
        }

        panel.getChildren().addAll(titulo, listaAlertas);
        panelCentral.getChildren().add(panel);
    }

    // ── Estadísticas ──────────────────────────────────────────────
    @FXML
    void mostrarEstadisticas() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("📊 Estadísticas del Parque");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        model.gestores.GestorReportes reportes =
                new model.gestores.GestorReportes(parque);

        Label lblAforo = new Label("👥 Visitantes actuales: "
                + parque.getVisitantesActuales() + " / " + parque.getCapacidadMaxima());
        Label lblZonas = new Label("🗺 Total zonas: "
                + parque.getZonas().tamaño());
        Label lblAtracciones = new Label("🎢 Total atracciones: "
                + parque.getMapa().tamaño());
        Label lblIngresos = new Label("💰 Ingresos del día: $"
                + String.format("%.0f", reportes.calcularIngresosDiarios()));
        Label lblMantenimiento = new Label("⚠ Atracciones en mantenimiento: "
                + reportes.getAtraccionesEnMantenimiento().tamaño());

        for (Label lbl : new Label[]{lblAforo, lblZonas, lblAtracciones, lblIngresos, lblMantenimiento}) {
            lbl.setStyle("-fx-font-size: 13; -fx-padding: 5;");
        }

        panel.getChildren().addAll(titulo, lblAforo, lblZonas,
                lblAtracciones, lblIngresos, lblMantenimiento);
        panelCentral.getChildren().add(panel);
    }

    // ── Mapa ──────────────────────────────────────────────────────
    @FXML
    void mostrarMapa() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🗾 Mapa del Parque");
        titulo.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        Label info = new Label(parque.getMapa().toString());
        info.setStyle("-fx-font-family: monospace; -fx-font-size: 11;");

        panel.getChildren().addAll(titulo, info);
        panelCentral.getChildren().add(panel);
    }
}