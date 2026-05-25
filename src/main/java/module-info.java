module techpark {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports techpark;  // Exporta el paquete que contiene Main

    opens ui.admin to javafx.fxml;
    opens ui.login to javafx.fxml;
    opens ui.visitante to javafx.fxml;
}