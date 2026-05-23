module techpark {
    requires javafx.controls;
    requires javafx.fxml;

    exports techpark;  // Exporta el paquete que contiene Main

    opens ui.admin to javafx.fxml;
}