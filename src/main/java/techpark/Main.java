package techpark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Administrador;
import model.Operador;
import model.Parque;
import model.Visitante;
import model.atraccion.Zona;
import model.gestores.GestorColas;
import model.persistencia.DataLoader;
import java.nio.file.Path;

public class Main extends Application {

    public static Parque parque;
    public static GestorColas gestorColas;
    public static Visitante visitanteActivo;
    public static Operador operadorActivo;
    public static Administrador administradorActivo;

    @Override
    public void start(Stage stage) throws Exception {
        // Inicializar parque
        parque = new Parque("Tech-Park UQ", 500);
        DataLoader loader = new DataLoader(parque);

        String rutaJson = Path.of(
                getClass().getResource("/data/escenario_prueba.json").toURI()
        ).toString();

        loader.cargarEscenario(rutaJson);

        // Inicializar gestor de colas global con todas las atracciones
        gestorColas = new GestorColas();
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona z = parque.getZonas().obtener(i);
            for (int j = 0; j < z.getAtracciones().tamaño(); j++) {
                gestorColas.registrarAtraccion(z.getAtracciones().obtener(j));
            }
        }

        // Arrancar en Login
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ui/login/LoginView.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("TechPark UQ");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> loader.guardarEstado(rutaJson));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}