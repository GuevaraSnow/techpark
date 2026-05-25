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