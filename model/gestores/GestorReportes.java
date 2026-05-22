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

    public ListaEnlazada<Atraccion> getAtraccionesMasVisitadas(int top) {
        ListaEnlazada<Atraccion> todas = getAllAtracciones();
        // Ordenar por contadorVisitantes descendente (burbuja)
        for (int i = 0; i < todas.tamaño() - 1; i++) {
            for (int j = 0; j < todas.tamaño() - i - 1; j++) {
                Atraccion a = todas.obtener(j);
                Atraccion b = todas.obtener(j + 1);
                if (a.getContadorVisitantes() < b.getContadorVisitantes()) {
                    // Intercambiar
                    todas.eliminar(j);
                    todas.eliminar(j); // b sube a posición j
                    ListaEnlazada<Atraccion> temp = new ListaEnlazada<>();
                    for (int k = 0; k < j; k++) temp.agregar(todas.obtener(k));
                    temp.agregar(b);
                    temp.agregar(a);
                    for (int k = j; k < todas.tamaño(); k++) temp.agregar(todas.obtener(k));
                    todas = temp;
                }
            }
        }
        // Retornar solo el top solicitado
        ListaEnlazada<Atraccion> resultado = new ListaEnlazada<>();
        int limite = Math.min(top, todas.tamaño());
        for (int i = 0; i < limite; i++) {
            resultado.agregar(todas.obtener(i));
        }
        return resultado;
    }

    public double getTiempoPromedioEspera() {
        ListaEnlazada<Atraccion> activas = new ListaEnlazada<>();
        ListaEnlazada<Atraccion> todas = getAllAtracciones();
        double suma = 0;
        int contador = 0;
        for (int i = 0; i < todas.tamaño(); i++) {
            Atraccion a = todas.obtener(i);
            if (a.getEstado() == EstadoAtraccion.ACTIVA) {
                suma += a.getTiempoEspera();
                contador++;
            }
        }
        return contador > 0 ? suma / contador : 0;
    }

    public String getResumenTiemposEspera() {
        ListaEnlazada<Atraccion> todas = getAllAtracciones();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < todas.tamaño(); i++) {
            Atraccion a = todas.obtener(i);
            sb.append(a.getNombre())
                    .append(": ~")
                    .append((int) a.getTiempoEspera())
                    .append(" min\n");
        }
        return sb.toString();
    }

    public ListaEnlazada<Atraccion> getCierresPorClima() {
        ListaEnlazada<Atraccion> resultado = new ListaEnlazada<>();
        ListaEnlazada<Atraccion> todas = getAllAtracciones();
        for (int i = 0; i < todas.tamaño(); i++) {
            Atraccion a = todas.obtener(i);
            if (a.getEstado() == EstadoAtraccion.CERRADA
                    && a.getMotivoCierre() != null
                    && (a.getMotivoCierre().toLowerCase().contains("clima")
                    || a.getMotivoCierre().toLowerCase().contains("tormenta")
                    || a.getMotivoCierre().toLowerCase().contains("lluvia"))) {
                resultado.agregar(a);
            }
        }
        return resultado;
    }

    public int getTotalCierresPorClima() {
        return getCierresPorClima().tamaño();
    }
}