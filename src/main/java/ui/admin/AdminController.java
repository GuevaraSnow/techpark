package ui.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Parque;
import model.atraccion.Atraccion;
import model.atraccion.Zona;
import model.estructuras.ListaEnlazada;
import techpark.Main;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AdminController {

    @FXML private StackPane panelCentral;
    @FXML private Label lblNombreAdmin;
    @FXML private Label lblInicial;
    @FXML private javafx.scene.shape.Circle avatarCirculo;
    @FXML private javafx.scene.image.ImageView avatarImagen;

    private Parque parque;
    private String fotoRuta = null;

    @FXML
    public void initialize() {
        this.parque = Main.parque;

        // Actualizar nombre e inicial en el sidebar
        if (Main.administradorActivo != null) {
            String nombre = Main.administradorActivo.getNombre();
            lblNombreAdmin.setText(nombre);
            lblInicial.setText(String.valueOf(nombre.charAt(0)).toUpperCase());
        }
    }

    // ── Editar perfil ─────────────────────────────────────────────
    @FXML
    void editarPerfil() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(16);
        panel.setPadding(new Insets(30));
        panel.setMaxWidth(500);

        Label titulo = new Label("✏ Editar Perfil");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        // Avatar grande
        StackPane avatarGrande = new StackPane();
        javafx.scene.shape.Circle circulo = new javafx.scene.shape.Circle(45);
        circulo.setFill(javafx.scene.paint.Color.web("#2E75B6"));
        circulo.setStroke(javafx.scene.paint.Color.web("#D0D7E3"));
        circulo.setStrokeWidth(2);

        Label inicialGrande = new Label(
                Main.administradorActivo != null ?
                        String.valueOf(Main.administradorActivo.getNombre().charAt(0)).toUpperCase() : "A");
        inicialGrande.setStyle("-fx-text-fill: white; -fx-font-size: 32; -fx-font-weight: bold;");

        // Si hay foto cargarla
        javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setPreserveRatio(false);
        imgView.setStyle("-fx-clip: circle;");

        if (fotoRuta != null) {
            try {
                javafx.scene.image.Image img =
                        new javafx.scene.image.Image("file:" + fotoRuta);
                imgView.setImage(img);
                circulo.setFill(javafx.scene.paint.Color.TRANSPARENT);
                inicialGrande.setVisible(false);
            } catch (Exception ignored) {}
        }

        avatarGrande.getChildren().addAll(circulo, inicialGrande, imgView);

        Button btnFoto = new Button("📷 Cambiar foto");
        btnFoto.setStyle("-fx-background-color: #E8EEF5; -fx-text-fill: #1F3864; "
                + "-fx-font-weight: bold; -fx-padding: 7 14; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnFoto.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleccionar foto de perfil");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
            File archivo = fc.showOpenDialog(panelCentral.getScene().getWindow());
            if (archivo != null) {
                fotoRuta = archivo.getAbsolutePath();
                try {
                    javafx.scene.image.Image img =
                            new javafx.scene.image.Image("file:" + fotoRuta);
                    imgView.setImage(img);
                    circulo.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    inicialGrande.setVisible(false);
                    // Actualizar avatar del sidebar
                    avatarCirculo.setFill(javafx.scene.paint.Color.TRANSPARENT);
                } catch (Exception ignored) {}
            }
        });

        VBox avatarBox = new VBox(8, avatarGrande, btnFoto);
        avatarBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Campos editables
        Label lblNombre = new Label("Nombre completo");
        lblNombre.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");
        TextField txtNombre = new TextField(
                Main.administradorActivo != null ?
                        Main.administradorActivo.getNombre() : "");
        txtNombre.setStyle("-fx-padding: 8 12; -fx-background-radius: 6; "
                + "-fx-border-color: #D0D7E3; -fx-border-radius: 6;");

        Label lblDoc = new Label("Documento");
        lblDoc.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");
        TextField txtDoc = new TextField(
                Main.administradorActivo != null ?
                        Main.administradorActivo.getDocumento() : "");
        txtDoc.setEditable(false);
        txtDoc.setStyle("-fx-padding: 8 12; -fx-background-radius: 6; "
                + "-fx-border-color: #D0D7E3; -fx-border-radius: 6; "
                + "-fx-background-color: #F0F4F8;");

        Button btnGuardar = new Button("💾 Guardar Cambios");
        btnGuardar.setStyle("-fx-background-color: #1F3864; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 10 24; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnGuardar.setOnAction(e -> {
            String nuevoNombre = txtNombre.getText().trim();
            if (nuevoNombre.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido",
                        "El nombre no puede estar vacío.");
                return;
            }
            // Actualizar nombre en el objeto y en el sidebar
            if (Main.administradorActivo != null) {
                // Nota: agregar setNombre() en Persona si no existe
                lblNombreAdmin.setText(nuevoNombre);
                lblInicial.setText(
                        String.valueOf(nuevoNombre.charAt(0)).toUpperCase());
                inicialGrande.setText(
                        String.valueOf(nuevoNombre.charAt(0)).toUpperCase());
            }
            parque.registrarNotificacion(
                    "✏ Perfil de administrador actualizado.");
            mostrarAlerta(Alert.AlertType.INFORMATION, "Perfil actualizado",
                    "✅ Cambios guardados correctamente.");
        });

        panel.getChildren().addAll(
                titulo, avatarBox,
                lblNombre, txtNombre,
                lblDoc, txtDoc,
                btnGuardar
        );
        panelCentral.getChildren().add(panel);
    }

    // ── Cerrar sesión ─────────────────────────────────────────────
    @FXML
    void cerrarSesion() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cerrar sesión");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Estás seguro que deseas cerrar sesión?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Main.administradorActivo = null;
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/ui/login/LoginView.fxml"));
                    Scene scene = new Scene(loader.load(), 1280, 720);
                    Stage stage = (Stage) panelCentral.getScene().getWindow();
                    stage.setTitle("TechPark UQ");
                    stage.setScene(scene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ── Zonas ─────────────────────────────────────────────────────
    @FXML
    void mostrarZonas() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🗺 Gestión de Zonas");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        ListView<String> listaZonas = new ListView<>();
        actualizarListaZonas(listaZonas);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre de la nueva zona");

        Button btnCrear = new Button("➕ Crear Zona");
        btnCrear.setStyle("-fx-background-color: #1F3864; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
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
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona z = parque.getZonas().obtener(i);
            lista.getItems().add("📍 " + z.getNombre()
                    + "  |  Atracciones: " + z.getAtracciones().tamaño()
                    + "  |  Operadores: " + z.getOperadores().tamaño());
        }
    }

    // ── Atracciones ───────────────────────────────────────────────
    @FXML
    void mostrarAtracciones() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🎢 Atracciones del Parque");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        TableView<String[]> tabla = new TableView<>();
        TableColumn<String[], String> colNombre     = new TableColumn<>("Nombre");
        TableColumn<String[], String> colTipo       = new TableColumn<>("Tipo");
        TableColumn<String[], String> colZona       = new TableColumn<>("Zona");
        TableColumn<String[], String> colEstado     = new TableColumn<>("Estado");
        TableColumn<String[], String> colVisitantes = new TableColumn<>("Visitantes");
        TableColumn<String[], String> colCapacidad  = new TableColumn<>("Capacidad");

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
        colCapacidad.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue()[5]));

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

        tabla.getColumns().addAll(colNombre, colTipo, colZona,
                colEstado, colVisitantes, colCapacidad);
        tabla.setPrefHeight(280);
        actualizarTablaAtracciones(tabla);

        Label lblFormulario = new Label("➕ Nueva Atracción");
        lblFormulario.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        TextField txtId        = new TextField(); txtId.setPromptText("ID (número)");
        TextField txtNombre    = new TextField(); txtNombre.setPromptText("Nombre");
        TextField txtAlturaMin = new TextField(); txtAlturaMin.setPromptText("Altura mín. (m)");
        TextField txtEdadMin   = new TextField(); txtEdadMin.setPromptText("Edad mín.");
        TextField txtCapacidad = new TextField(); txtCapacidad.setPromptText("Capacidad");
        TextField txtCosto     = new TextField(); txtCosto.setPromptText("Costo extra ($)");

        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("mecánica de altura", "acuática", "otra");
        cbTipo.setPromptText("Tipo");

        ComboBox<String> cbZona = new ComboBox<>();
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            cbZona.getItems().add(parque.getZonas().obtener(i).getNombre());
        }
        cbZona.setPromptText("Zona");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.addRow(0, txtId, txtNombre, cbTipo, cbZona);
        grid.addRow(1, txtCapacidad, txtAlturaMin, txtEdadMin, txtCosto);

        Button btnCrear = new Button("➕ Crear Atracción");
        btnCrear.setStyle("-fx-background-color: #1F3864; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnCrear.setOnAction(e -> {
            try {
                int id        = Integer.parseInt(txtId.getText().trim());
                String nombre = txtNombre.getText().trim();
                String tipo   = cbTipo.getValue();
                String zona   = cbZona.getValue();
                int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
                double altMin = Double.parseDouble(txtAlturaMin.getText().trim());
                int edadMin   = Integer.parseInt(txtEdadMin.getText().trim());
                double costo  = Double.parseDouble(txtCosto.getText().trim());

                if (nombre.isEmpty() || tipo == null || zona == null) {
                    mostrarAlerta(Alert.AlertType.WARNING,
                            "Campos incompletos", "Completa todos los campos.");
                    return;
                }

                Atraccion nueva = new Atraccion(id, nombre, tipo,
                        capacidad, altMin, edadMin, costo);
                parque.agregarAtraccionAZona(zona, nueva);
                actualizarTablaAtracciones(tabla);

                txtId.clear(); txtNombre.clear(); txtAlturaMin.clear();
                txtEdadMin.clear(); txtCapacidad.clear(); txtCosto.clear();
                cbTipo.setValue(null); cbZona.setValue(null);

                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Atracción creada", "✅ " + nombre + " agregada correctamente.");

            } catch (NumberFormatException ex) {
                mostrarAlerta(Alert.AlertType.ERROR,
                        "Error", "Verifica que ID, capacidad, edad y costo sean números válidos.");
            }
        });

        Button btnClima = new Button("🌧 Activar Alerta Climática");
        btnClima.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnClima.setOnAction(e -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(
                    "Tormenta eléctrica", "Tormenta eléctrica", "Lluvia fuerte");
            dialog.setTitle("Alerta Climática");
            dialog.setHeaderText("Seleccionar tipo de alerta");
            dialog.setContentText("Tipo:");
            dialog.showAndWait().ifPresent(tipo -> {
                int cerradas = parque.activarAlertaClimatica(tipo);
                actualizarTablaAtracciones(tabla);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Alerta activada",
                        "⚠ Se cerraron " + cerradas + " atracciones por: " + tipo);
            });
        });

        Button btnDesactivarClima = new Button("☀ Desactivar Alerta");
        btnDesactivarClima.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnDesactivarClima.setOnAction(e -> {
            parque.desactivarAlertaClimatica();
            actualizarTablaAtracciones(tabla);
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Alerta desactivada",
                    "☀ Alerta climática desactivada. Atracciones afectadas volvieron a ACTIVA.");
        });

        HBox botones = new HBox(10, btnCrear, btnClima, btnDesactivarClima);
        panel.getChildren().addAll(titulo, tabla, lblFormulario, grid, botones);
        panelCentral.getChildren().add(panel);
    }

    private void actualizarTablaAtracciones(TableView<String[]> tabla) {
        tabla.getItems().clear();
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona z = parque.getZonas().obtener(i);
            for (int j = 0; j < z.getAtracciones().tamaño(); j++) {
                Atraccion a = z.getAtracciones().obtener(j);
                tabla.getItems().add(new String[]{
                        a.getNombre(), a.getTipo(), z.getNombre(),
                        a.getEstado().toString(),
                        String.valueOf(a.getContadorVisitantes()),
                        String.valueOf(a.getCapacidad())
                });
            }
        }
    }

    // ── Empleados ─────────────────────────────────────────────────
    @FXML
    void mostrarEmpleados() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("👥 Gestión de Empleados");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

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
                    emp.getNombre(), emp.getDocumento(), emp.getCargo()
            });
        }

        panel.getChildren().addAll(titulo, tabla);
        panelCentral.getChildren().add(panel);
    }

    // ── Alertas ───────────────────────────────────────────────────
    @FXML
    void mostrarAlertas() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🔔 Alertas del Sistema");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        Label lblActivas = new Label("⚡ Alertas activas");
        lblActivas.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #C0392B;");

        ListView<String> listaActivas = new ListView<>();
        listaActivas.setPrefHeight(120);

        model.gestores.GestorMantenimiento gestor =
                new model.gestores.GestorMantenimiento(parque);
        gestor.verificarYGenerarAlertas();
        while (gestor.hayAlertas()) {
            listaActivas.getItems().add(gestor.atenderSiguienteAlerta());
        }

        model.gestores.GestorReportes reportes =
                new model.gestores.GestorReportes(parque);
        ListaEnlazada<Atraccion> cerradasClima = reportes.getCierresPorClima();
        for (int i = 0; i < cerradasClima.tamaño(); i++) {
            Atraccion a = cerradasClima.obtener(i);
            listaActivas.getItems().add("🌧 ACTIVA | "
                    + a.getNombre() + " | " + a.getMotivoCierre());
        }

        if (listaActivas.getItems().isEmpty()) {
            listaActivas.getItems().add("✅ No hay alertas activas.");
        }

        Button btnDesactivar = new Button("☀ Desactivar Alerta Climática");
        btnDesactivar.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnDesactivar.setOnAction(e -> {
            parque.desactivarAlertaClimatica();
            mostrarAlertas();
        });

        Label lblHistorial = new Label("📋 Historial de la sesión");
        lblHistorial.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        ListView<String> listaHistorial = new ListView<>();
        listaHistorial.setPrefHeight(250);

        ListaEnlazada<String> historial = parque.getHistorialNotificaciones();
        if (historial.estaVacia()) {
            listaHistorial.getItems().add("Sin notificaciones en esta sesión.");
        } else {
            for (int i = historial.tamaño() - 1; i >= 0; i--) {
                listaHistorial.getItems().add(historial.obtener(i));
            }
        }

        panel.getChildren().addAll(
                titulo,
                lblActivas, listaActivas,
                btnDesactivar,
                lblHistorial, listaHistorial
        );
        panelCentral.getChildren().add(panel);
    }

    // ── Estadísticas ──────────────────────────────────────────────
    @FXML
    void mostrarEstadisticas() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("📊 Estadísticas del Parque");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

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
        Label lblCierresClima = new Label("🌧 Cierres por clima: "
                + reportes.getCierresPorClima().tamaño());

        for (Label lbl : new Label[]{lblAforo, lblZonas, lblAtracciones,
                lblIngresos, lblMantenimiento, lblCierresClima}) {
            lbl.setStyle("-fx-font-size: 13; -fx-padding: 5;");
        }

        panel.getChildren().addAll(titulo, lblAforo, lblZonas,
                lblAtracciones, lblIngresos, lblMantenimiento, lblCierresClima);
        panelCentral.getChildren().add(panel);
    }

    // ── Mapa ──────────────────────────────────────────────────────
    @FXML
    void mostrarMapa() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🗾 Mapa del Parque");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        ComboBox<String> cbOrigen  = new ComboBox<>();
        ComboBox<String> cbDestino = new ComboBox<>();
        cbOrigen.setPromptText("Origen");
        cbDestino.setPromptText("Destino");

        ListaEnlazada<Atraccion> atracciones = parque.getMapa().getAtracciones();
        for (int i = 0; i < atracciones.tamaño(); i++) {
            String nombre = atracciones.obtener(i).getNombre();
            cbOrigen.getItems().add(nombre);
            cbDestino.getItems().add(nombre);
        }

        Label lblDistancia = new Label("");
        lblDistancia.setStyle("-fx-font-weight: bold; -fx-text-fill: #2E75B6;");

        Pane panelGrafo = new Pane();
        panelGrafo.setPrefSize(1000, 520);
        panelGrafo.setStyle("-fx-background-color: #F8FAFF; -fx-border-color: #D0D7E3; "
                + "-fx-border-radius: 10; -fx-background-radius: 10;");

        Map<Integer, double[]> coords = new HashMap<>();
        int total = atracciones.tamaño();
        double cx = 500, cy = 260, radio = 210;
        for (int i = 0; i < total; i++) {
            double angulo = 2 * Math.PI * i / total - Math.PI / 2;
            coords.put(atracciones.obtener(i).getId(),
                    new double[]{cx + radio * Math.cos(angulo),
                            cy + radio * Math.sin(angulo)});
        }

        dibujarAristas(panelGrafo, coords, atracciones);
        dibujarNodos(panelGrafo, coords, atracciones);

        Button btnRuta = new Button("📍 Calcular Ruta");
        btnRuta.setStyle("-fx-background-color: #2E75B6; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnRuta.setOnAction(e -> {
            String nombreOrigen  = cbOrigen.getValue();
            String nombreDestino = cbDestino.getValue();
            if (nombreOrigen == null || nombreDestino == null) return;

            Atraccion origen  = parque.buscarAtraccion(nombreOrigen);
            Atraccion destino = parque.buscarAtraccion(nombreDestino);
            if (origen == null || destino == null) return;

            panelGrafo.getChildren().clear();
            dibujarAristas(panelGrafo, coords, atracciones);
            dibujarNodos(panelGrafo, coords, atracciones);

            ListaEnlazada<Atraccion> ruta =
                    parque.getMapa().dijkstra(origen.getId(), destino.getId());

            for (int i = 0; i < ruta.tamaño() - 1; i++) {
                double[] posA = coords.get(ruta.obtener(i).getId());
                double[] posB = coords.get(ruta.obtener(i + 1).getId());
                if (posA == null || posB == null) continue;
                Line rutaLinea = new Line(posA[0], posA[1], posB[0], posB[1]);
                rutaLinea.setStroke(javafx.scene.paint.Color.web("#2196F3"));
                rutaLinea.setStrokeWidth(5);
                rutaLinea.setEffect(new javafx.scene.effect.DropShadow(6,
                        javafx.scene.paint.Color.web("#1565C0")));
                panelGrafo.getChildren().add(rutaLinea);
            }

            double distancia = 0;
            for (int i = 0; i < ruta.tamaño() - 1; i++) {
                distancia += parque.getMapa().getPeso(
                        ruta.obtener(i).getId(), ruta.obtener(i + 1).getId());
            }
            lblDistancia.setText("📍 Ruta: " + ruta.tamaño()
                    + " atracciones | Distancia: " + (int) distancia + " m");
        });

        HBox leyenda = new HBox(20);
        leyenda.setPadding(new Insets(8, 0, 0, 0));
        leyenda.getChildren().addAll(
                crearItemLeyenda("#4CAF50", "Activa"),
                crearItemLeyenda("#FFC107", "Mantenimiento"),
                crearItemLeyenda("#F44336", "Cerrada")
        );

        HBox controles = new HBox(10, cbOrigen, cbDestino, btnRuta, lblDistancia);
        controles.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        panel.getChildren().addAll(titulo, controles, leyenda, panelGrafo);
        panelCentral.getChildren().add(panel);
    }

    // ── Métodos privados ──────────────────────────────────────────
    private void dibujarAristas(Pane panel, Map<Integer, double[]> coords,
                                ListaEnlazada<Atraccion> atracciones) {
        int total = atracciones.tamaño();
        for (int i = 0; i < total; i++) {
            Atraccion origen = atracciones.obtener(i);
            double[] posO = coords.get(origen.getId());
            ListaEnlazada<Integer> vecinos =
                    parque.getMapa().getVecinos(origen.getId());
            for (int j = 0; j < vecinos.tamaño(); j++) {
                int idDest = vecinos.obtener(j);
                if (idDest <= origen.getId()) continue;
                double[] posD = coords.get(idDest);
                if (posD == null) continue;
                Line linea = new Line(posO[0], posO[1], posD[0], posD[1]);
                linea.setStroke(javafx.scene.paint.Color.web("#B0BEC5"));
                linea.setStrokeWidth(2);
                double midX = (posO[0] + posD[0]) / 2;
                double midY = (posO[1] + posD[1]) / 2;
                double peso = parque.getMapa().getPeso(origen.getId(), idDest);
                javafx.scene.text.Text lblPeso = new javafx.scene.text.Text(
                        midX - 10, midY - 5, (int) peso + "m");
                lblPeso.setStyle("-fx-font-size: 9; -fx-font-weight: bold;");
                lblPeso.setFill(javafx.scene.paint.Color.web("#455A64"));
                javafx.scene.shape.Rectangle fondo =
                        new javafx.scene.shape.Rectangle();
                fondo.setX(midX - 14);
                fondo.setY(midY - 16);
                fondo.setWidth(lblPeso.getText().length() * 6 + 4);
                fondo.setHeight(14);
                fondo.setFill(javafx.scene.paint.Color.WHITE);
                fondo.setArcWidth(4);
                fondo.setArcHeight(4);
                fondo.setOpacity(0.85);
                panel.getChildren().addAll(linea, fondo, lblPeso);
            }
        }
    }

    private void dibujarNodos(Pane panel, Map<Integer, double[]> coords,
                              ListaEnlazada<Atraccion> atracciones) {
        int total = atracciones.tamaño();
        for (int i = 0; i < total; i++) {
            Atraccion a = atracciones.obtener(i);
            double[] pos = coords.get(a.getId());
            javafx.scene.shape.Circle circulo =
                    new javafx.scene.shape.Circle(pos[0], pos[1], 24);
            circulo.setFill(getColorEstado(a));
            circulo.setStroke(javafx.scene.paint.Color.WHITE);
            circulo.setStrokeWidth(3);
            circulo.setEffect(new javafx.scene.effect.DropShadow(8,
                    javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));
            circulo.setOnMouseEntered(e -> {
                circulo.setRadius(28);
                circulo.setEffect(new javafx.scene.effect.DropShadow(14,
                        javafx.scene.paint.Color.web("#2E75B6")));
            });
            circulo.setOnMouseExited(e -> {
                circulo.setRadius(24);
                circulo.setEffect(new javafx.scene.effect.DropShadow(8,
                        javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));
            });
            javafx.scene.control.Tooltip tip = new javafx.scene.control.Tooltip(
                    a.getNombre() + "\nEstado: " + a.getEstado()
                            + "\nEspera: ~" + (int) a.getTiempoEspera() + " min"
                            + "\nVisitantes: " + a.getContadorVisitantes());
            javafx.scene.control.Tooltip.install(circulo, tip);
            javafx.scene.text.Text etiqueta = new javafx.scene.text.Text(
                    pos[0] - 30, pos[1] + 38, a.getNombre());
            etiqueta.setStyle(
                    "-fx-font-size: 9; -fx-font-weight: bold; -fx-fill: #1F3864;");
            etiqueta.setWrappingWidth(70);
            etiqueta.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            panel.getChildren().addAll(circulo, etiqueta);
        }
    }

    private javafx.scene.paint.Color getColorEstado(Atraccion a) {
        return switch (a.getEstado()) {
            case ACTIVA        -> javafx.scene.paint.Color.web("#4CAF50");
            case MANTENIMIENTO -> javafx.scene.paint.Color.web("#FFC107");
            case CERRADA       -> javafx.scene.paint.Color.web("#F44336");
        };
    }

    private HBox crearItemLeyenda(String color, String texto) {
        javafx.scene.shape.Circle circulo = new javafx.scene.shape.Circle(8);
        circulo.setFill(javafx.scene.paint.Color.web(color));
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-size: 11; -fx-text-fill: #555;");
        HBox item = new HBox(6, circulo, lbl);
        item.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return item;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}