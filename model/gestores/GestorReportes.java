package model.gestores;

import model.Parque;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import model.atraccion.Zona;
import model.estructuras.ListaEnlazada;
import model.Visitante;
import model.ticket.TipoTicket;

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

    public double calcularIngresosDiarios() {
        double total = 0;
        ListaEnlazada<Visitante> visitantes = parque.getVisitantes();
        for (int i = 0; i < visitantes.tamaño(); i++) {
            Visitante v = visitantes.obtener(i);
            if (v.getTicket() != null && v.getTicket().isActivo()) {
                total += v.getTicket().getPrecio();
            }
        }
        return total;
    }

    public String getDesglosePorTipoTicket() {
        double general = 0, familiar = 0, fastpass = 0;
        int cGeneral = 0, cFamiliar = 0, cFastpass = 0;
        ListaEnlazada<Visitante> visitantes = parque.getVisitantes();
        for (int i = 0; i < visitantes.tamaño(); i++) {
            Visitante v = visitantes.obtener(i);
            if (v.getTicket() == null) continue;
            switch (v.getTicket().getTipo()) {
                case GENERAL  -> { general  += v.getTicket().getPrecio(); cGeneral++; }
                case FAMILIAR -> { familiar += v.getTicket().getPrecio(); cFamiliar++; }
                case FASTPASS -> { fastpass += v.getTicket().getPrecio(); cFastpass++; }
            }
        }
        return "General: " + cGeneral + " tickets ($" + general + ")\n"
                + "Familiar: " + cFamiliar + " tickets ($" + familiar + ")\n"
                + "FastPass: " + cFastpass + " tickets ($" + fastpass + ")\n"
                + "TOTAL: $" + (general + familiar + fastpass);
    }
}