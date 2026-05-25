package ui.operador;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import java.io.File;

public class OperadorController {

    @FXML private StackPane panelCentral;
    @FXML private Label lblNombreOperador;
    @FXML private Label lblZonaOperador;
    @FXML private Label lblInicial;
    @FXML private javafx.scene.shape.Circle avatarCirculo;
    @FXML private javafx.scene.image.ImageView avatarImagen;

    private Parque parque;
    private Operador operador;
    private Zona zonaAsignada;
    private GestorColas gestorColas;
    private ControlAcceso controlAcceso;

    @FXML
    public void initialize() {
        this.parque        = Main.parque;
        this.operador      = Main.operadorActivo;
        this.gestorColas   = Main.gestorColas;
        this.controlAcceso = new ControlAcceso();

        // Buscar zona asignada
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

        actualizarAvatarSidebar();
        mostrarAtraccion();
    }

    // ── Actualizar avatar del sidebar ─────────────────────────────
    private void actualizarAvatarSidebar() {
        if (operador == null) return;
        lblNombreOperador.setText(operador.getNombre());
        lblInicial.setText(
                String.valueOf(operador.getNombre().charAt(0)).toUpperCase());
        lblZonaOperador.setText(
                zonaAsignada != null ? zonaAsignada.getNombre() : "Sin zona");

        if (operador.getFotoRuta() != null
                && !operador.getFotoRuta().isEmpty()) {
            try {
                javafx.scene.image.Image img =
                        new javafx.scene.image.Image(
                                "file:" + operador.getFotoRuta());
                avatarImagen.setImage(img);
                javafx.scene.shape.Circle clip =
                        new javafx.scene.shape.Circle(20, 20, 20);
                avatarImagen.setClip(clip);
                avatarCirculo.setFill(javafx.scene.paint.Color.TRANSPARENT);
                lblInicial.setVisible(false);
            } catch (Exception ignored) {}
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
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

        // Avatar grande
        StackPane avatarGrande = new StackPane();
        javafx.scene.shape.Circle circulo = new javafx.scene.shape.Circle(45);
        circulo.setFill(javafx.scene.paint.Color.web("#7D3C98"));
        circulo.setStroke(javafx.scene.paint.Color.web("#D0D7E3"));
        circulo.setStrokeWidth(2);

        Label inicialGrande = new Label(
                String.valueOf(operador.getNombre().charAt(0)).toUpperCase());
        inicialGrande.setStyle(
                "-fx-text-fill: white; -fx-font-size: 32; -fx-font-weight: bold;");

        javafx.scene.image.ImageView imgView =
                new javafx.scene.image.ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setPreserveRatio(false);

        if (operador.getFotoRuta() != null
                && !operador.getFotoRuta().isEmpty()) {
            try {
                javafx.scene.image.Image img =
                        new javafx.scene.image.Image(
                                "file:" + operador.getFotoRuta());
                imgView.setImage(img);
                javafx.scene.shape.Circle clip =
                        new javafx.scene.shape.Circle(45, 45, 45);
                imgView.setClip(clip);
                circulo.setFill(javafx.scene.paint.Color.TRANSPARENT);
                inicialGrande.setVisible(false);
            } catch (Exception ignored) {}
        }

        avatarGrande.getChildren().addAll(circulo, inicialGrande, imgView);

        Button btnFoto = new Button("📷 Cambiar foto");
        btnFoto.setStyle("-fx-background-color: #E8EEF5; -fx-text-fill: #4A235A; "
                + "-fx-font-weight: bold; -fx-padding: 7 14; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnFoto.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleccionar foto de perfil");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Imágenes", "*.png", "*.jpg", "*.jpeg"));
            File archivo = fc.showOpenDialog(
                    panelCentral.getScene().getWindow());
            if (archivo != null) {
                operador.setFotoRuta(archivo.getAbsolutePath());
                try {
                    javafx.scene.image.Image img =
                            new javafx.scene.image.Image(
                                    "file:" + operador.getFotoRuta());
                    imgView.setImage(img);
                    javafx.scene.shape.Circle clip =
                            new javafx.scene.shape.Circle(45, 45, 45);
                    imgView.setClip(clip);
                    circulo.setFill(javafx.scene.paint.Color.TRANSPARENT);
                    inicialGrande.setVisible(false);
                    actualizarAvatarSidebar();
                } catch (Exception ignored) {}
            }
        });

        VBox avatarBox = new VBox(8, avatarGrande, btnFoto);
        avatarBox.setAlignment(javafx.geometry.Pos.CENTER);

        Label lblNombreL = new Label("Nombre completo");
        lblNombreL.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");
        TextField txtNombre = new TextField(operador.getNombre());
        txtNombre.setStyle("-fx-padding: 8 12; -fx-background-radius: 6; "
                + "-fx-border-color: #D0D7E3; -fx-border-radius: 6;");

        Label lblDocL = new Label("Documento (no editable)");
        lblDocL.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");
        TextField txtDoc = new TextField(operador.getDocumento());
        txtDoc.setEditable(false);
        txtDoc.setStyle("-fx-padding: 8 12; -fx-background-radius: 6; "
                + "-fx-border-color: #D0D7E3; -fx-border-radius: 6; "
                + "-fx-background-color: #F0F4F8;");

        Label lblZonaL = new Label("Zona asignada (no editable)");
        lblZonaL.setStyle("-fx-font-size: 12; -fx-text-fill: #555;");
        TextField txtZona = new TextField(
                zonaAsignada != null ? zonaAsignada.getNombre() : "Sin zona");
        txtZona.setEditable(false);
        txtZona.setStyle("-fx-padding: 8 12; -fx-background-radius: 6; "
                + "-fx-border-color: #D0D7E3; -fx-border-radius: 6; "
                + "-fx-background-color: #F0F4F8;");

        Button btnGuardar = new Button("💾 Guardar Cambios");
        btnGuardar.setStyle("-fx-background-color: #4A235A; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 10 24; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnGuardar.setOnAction(e -> {
            String nuevoNombre = txtNombre.getText().trim();
            if (nuevoNombre.isEmpty()) {
                alerta("Campo requerido", "El nombre no puede estar vacío.");
                return;
            }
            operador.setNombre(nuevoNombre);
            actualizarAvatarSidebar();
            parque.registrarNotificacion(
                    "✏ Perfil de operador actualizado: " + nuevoNombre);
            alerta("Perfil actualizado", "✅ Cambios guardados correctamente.");
        });

        panel.getChildren().addAll(
                titulo, avatarBox,
                lblNombreL, txtNombre,
                lblDocL, txtDoc,
                lblZonaL, txtZona,
                btnGuardar
        );
        panelCentral.getChildren().add(panel);
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
        lblZona.setStyle(
                "-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

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

        tabla.getColumns().addAll(
                colNombre, colEstado, colVisitantes, colCapacidad, colEspera);

        for (int i = 0; i < zonaAsignada.getAtracciones().tamaño(); i++) {
            Atraccion a = zonaAsignada.getAtracciones().obtener(i);
            tabla.getItems().add(new String[]{
                    a.getNombre(), a.getEstado().toString(),
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
                Visitante sig = gestorColas.verSiguiente(a);
                if (sig != null) {
                    listaCola.getItems().add("👑 Siguiente: "
                            + sig.getNombre()
                            + " (" + sig.getTicket().getTipo() + ")");
                }
            }
            lblInfo.setText("Capacidad del ciclo: "
                    + a.getContadorActual() + " / " + a.getCapacidad());
        });

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
                gestorColas.desencolar(a);
                alerta("❌ Ingreso denegado", resultado.mensaje);
            }

            cbAtraccion.getOnAction().handle(null);
        });

        HBox filtro = new HBox(10, new Label("Atracción:"), cbAtraccion);
        filtro.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        panel.getChildren().addAll(titulo, filtro, listaCola, lblInfo, btnProcesar);
        panelCentral.getChildren().add(panel);
    }

    // ── Estado y revisión ─────────────────────────────────────────
    @FXML
    void mostrarEstado() {
        panelCentral.getChildren().clear();
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(30));

        Label titulo = new Label("⚙ Estado y Revisión Técnica");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

        if (zonaAsignada == null) {
            panel.getChildren().addAll(titulo,
                    new Label("No tienes zona asignada."));
            panelCentral.getChildren().add(panel);
            return;
        }

        ComboBox<String> cbAtraccion = new ComboBox<>();
        for (int i = 0; i < zonaAsignada.getAtracciones().tamaño(); i++) {
            cbAtraccion.getItems().add(
                    zonaAsignada.getAtracciones().obtener(i).getNombre());
        }
        cbAtraccion.setPromptText("Seleccionar atracción");

        Label lblEstadoActual = new Label("Estado: —");
        lblEstadoActual.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        Label lblMotivo = new Label("");
        lblMotivo.setStyle("-fx-font-size: 12; -fx-text-fill: #C0392B;");

        cbAtraccion.setOnAction(e -> {
            String nombre = cbAtraccion.getValue();
            if (nombre == null) return;
            Atraccion a = parque.buscarAtraccion(nombre);
            if (a == null) return;
            lblEstadoActual.setText("Estado: " + a.getEstado()
                    + "  |  Visitantes acumulados: " + a.getContadorVisitantes());
            lblMotivo.setText(a.getMotivoCierre().isEmpty() ? "" :
                    "Motivo: " + a.getMotivoCierre());
        });

        ComboBox<String> cbNuevoEstado = new ComboBox<>();
        cbNuevoEstado.getItems().addAll("ACTIVA", "CERRADA");
        cbNuevoEstado.setPromptText("Nuevo estado");

        TextField txtMotivo = new TextField();
        txtMotivo.setPromptText("Motivo (requerido si es CERRADA)");

        Button btnCambiar = new Button("✏ Cambiar Estado");
        btnCambiar.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnCambiar.setOnAction(e -> {
            String nombre = cbAtraccion.getValue();
            String nuevoEstado = cbNuevoEstado.getValue();
            if (nombre == null || nuevoEstado == null) return;
            Atraccion a = parque.buscarAtraccion(nombre);
            if (a == null) return;

            if (nuevoEstado.equals("CERRADA")) {
                String motivo = txtMotivo.getText().trim();
                if (motivo.isEmpty()) {
                    alerta("Campo requerido", "Ingresa el motivo del cierre.");
                    return;
                }
                a.setEstado(EstadoAtraccion.CERRADA);
                a.setMotivoCierre(motivo);
            } else {
                a.setEstado(EstadoAtraccion.ACTIVA);
                a.setMotivoCierre("");
            }
            cbAtraccion.getOnAction().handle(null);
            alerta("Estado actualizado",
                    "✅ Estado de " + nombre + " cambiado a " + nuevoEstado);
        });

        Button btnRevision = new Button("🔧 Registrar Revisión Técnica");
        btnRevision.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; "
                + "-fx-background-radius: 6; -fx-cursor: hand;");
        btnRevision.setOnAction(e -> {
            String nombre = cbAtraccion.getValue();
            if (nombre == null) return;
            Atraccion a = parque.buscarAtraccion(nombre);
            if (a == null) return;

            if (a.getEstado() != EstadoAtraccion.MANTENIMIENTO) {
                alerta("No aplica",
                        "Solo puedes registrar revisión en atracciones en MANTENIMIENTO.");
                return;
            }

            a.registrarRevisionTecnica();
            parque.registrarNotificacion("🔧 Revisión técnica registrada en "
                    + nombre + " por operador " + operador.getNombre());
            cbAtraccion.getOnAction().handle(null);
            alerta("Revisión registrada",
                    "✅ " + nombre + " volvió a estado ACTIVA.\n"
                            + "Contador de visitantes reiniciado.");
        });

        Label lblCambioEstado = new Label("✏ Cambiar estado de atracción");
        lblCambioEstado.setStyle(
                "-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #4A235A;");
        Label lblRevision = new Label("🔧 Revisión técnica");
        lblRevision.setStyle(
                "-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #4A235A;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.addRow(0, cbNuevoEstado, txtMotivo, btnCambiar);

        panel.getChildren().addAll(
                titulo,
                new Label("Seleccionar atracción:"), cbAtraccion,
                lblEstadoActual, lblMotivo,
                new Separator(),
                lblCambioEstado, grid,
                new Separator(),
                lblRevision, btnRevision
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
                Main.operadorActivo = null;
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

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}