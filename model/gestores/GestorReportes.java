package model.gestores;

import model.Parque;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import model.atraccion.Zona;
import model.estructuras.ListaEnlazada;

public class GestorReportes {

    private Parque parque;

    public GestorReportes(Parque parque) {
        this.parque = parque;
    }

    // Recorre todas las zonas y atracciones del parque
    private ListaEnlazada<Atraccion> getAllAtracciones() {
        ListaEnlazada<Atraccion> todas = new ListaEnlazada<>();
        ListaEnlazada<Zona> zonas = parque.getZonas();
        for (int i = 0; i < zonas.tamaño(); i++) {
            Zona zona = zonas.obtener(i);
            ListaEnlazada<Atraccion> atracciones = zona.getAtracciones();
            for (int j = 0; j < atracciones.tamaño(); j++) {
                todas.agregar(atracciones.obtener(j));
            }
        }
        return todas;
    }

    public ListaEnlazada<Atraccion> getTodasLasAtracciones() {
        return getAllAtracciones();
    }
}