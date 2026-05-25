package techpark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Operador;
import model.Parque;
import model.Visitante;
import model.persistencia.DataLoader;
import java.nio.file.Path;

public class Main extends Application {

    public static Parque parque;
    public static Visitante visitanteActivo;
    public static Operador operadorActivo;

    @Override
    public void start(Stage stage) throws Exception {
        parque = new Parque("Tech-Park UQ", 500);
        DataLoader loader = new DataLoader(parque);

        String rutaJson = Path.of(
                getClass().getResource("/data/escenario_prueba.json").toURI()
        ).toString();

        loader.cargarEscenario(rutaJson);

        // Arrancar en pantalla de Login
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