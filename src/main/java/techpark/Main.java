package techpark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Parque;
import model.persistencia.DataLoader;
import java.nio.file.Path;

public class Main extends Application {

    public static Parque parque;

    @Override
    public void start(Stage stage) throws Exception {
        parque = new Parque("Tech-Park UQ", 500);
        DataLoader loader = new DataLoader(parque);

        String rutaJson = Path.of(
                getClass().getResource("/data/escenario_prueba.json").toURI()
        ).toString();

        loader.cargarEscenario(rutaJson);

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/ui/admin/AdminView.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TechPark");
        stage.setScene(scene);

        // Guardar al cerrar la ventana
        stage.setOnCloseRequest(e -> loader.guardarEstado(rutaJson));

        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}