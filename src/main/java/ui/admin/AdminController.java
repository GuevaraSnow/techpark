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
import javafx.scene.shape.Line;
import javafx.scene.control.ComboBox;
import java.util.HashMap;
import java.util.Map;

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

    @FXML
    void mostrarMapa() {
        panelCentral.getChildren().clear();

        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));

        Label titulo = new Label("🗾 Mapa del Parque");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1F3864;");

        // ComboBox para Dijkstra
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

        // Pane del grafo
        Pane panelGrafo = new Pane();
        panelGrafo.setPrefSize(1000, 550);
        panelGrafo.setStyle("-fx-background-color: #F8FAFF; -fx-border-color: #D0D7E3; "
                + "-fx-border-radius: 10; -fx-background-radius: 10;");

        // Calcular coordenadas en círculo
        Map<Integer, double[]> coords = new HashMap<>();
        int total = atracciones.tamaño();
        double cx = 500, cy = 270, radio = 210;
        for (int i = 0; i < total; i++) {
            double angulo = 2 * Math.PI * i / total - Math.PI / 2;
            coords.put(atracciones.obtener(i).getId(),
                    new double[]{cx + radio * Math.cos(angulo), cy + radio * Math.sin(angulo)});
        }

        // Dibujar aristas
        for (int i = 0; i < total; i++) {
            Atraccion origen = atracciones.obtener(i);
            double[] posO = coords.get(origen.getId());
            ListaEnlazada<Integer> vecinos = parque.getMapa().getVecinos(origen.getId());
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
                lblPeso.setStyle("-fx-font-size: 9; -fx-fill: #78909C;");

                panelGrafo.getChildren().addAll(linea, lblPeso);
            }
        }

        // Dibujar nodos
        for (int i = 0; i < total; i++) {
            Atraccion a = atracciones.obtener(i);
            double[] pos = coords.get(a.getId());

            javafx.scene.shape.Circle circulo = new javafx.scene.shape.Circle(pos[0], pos[1], 24);
            circulo.setFill(getColorEstado(a));
            circulo.setStroke(javafx.scene.paint.Color.WHITE);
            circulo.setStrokeWidth(3);
            circulo.setEffect(new javafx.scene.effect.DropShadow(8,
                    javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));

            // Hover
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

            // Tooltip
            javafx.scene.control.Tooltip tip = new javafx.scene.control.Tooltip(
                    a.getNombre() + "\nEstado: " + a.getEstado()
                            + "\nEspera: ~" + (int) a.getTiempoEspera() + " min"
                            + "\nVisitantes: " + a.getContadorVisitantes());
            javafx.scene.control.Tooltip.install(circulo, tip);

            // Etiqueta
            javafx.scene.text.Text etiqueta = new javafx.scene.text.Text(
                    pos[0] - 30, pos[1] + 38, a.getNombre());
            etiqueta.setStyle("-fx-font-size: 9; -fx-font-weight: bold; -fx-fill: #1F3864;");
            etiqueta.setWrappingWidth(70);
            etiqueta.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            panelGrafo.getChildren().addAll(circulo, etiqueta);
        }

        // Botón calcular ruta
        Button btnRuta = new Button("📍 Calcular Ruta");
        btnRuta.setStyle("-fx-background-color: #2E75B6; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 18; -fx-background-radius: 6; "
                + "-fx-cursor: hand;");
        btnRuta.setOnAction(e -> {
            String nombreOrigen  = cbOrigen.getValue();
            String nombreDestino = cbDestino.getValue();
            if (nombreOrigen == null || nombreDestino == null) return;

            Atraccion origen  = parque.buscarAtraccion(nombreOrigen);
            Atraccion destino = parque.buscarAtraccion(nombreDestino);
            if (origen == null || destino == null) return;

            ListaEnlazada<Atraccion> ruta =
                    parque.getMapa().dijkstra(origen.getId(), destino.getId());

            // Resaltar ruta
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
                    + " atracciones | Distancia total: " + (int) distancia + " m");
        });

        // Leyenda
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
}