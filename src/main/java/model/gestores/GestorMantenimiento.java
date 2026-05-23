package model.gestores;

import model.Parque;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import model.atraccion.Zona;
import model.estructuras.ColaPrioridad;
import model.estructuras.ListaEnlazada;

public class GestorMantenimiento {

    private Parque parque;
    private ColaPrioridad<String> alertas;

    public GestorMantenimiento(Parque parque) {
        this.parque = parque;
        this.alertas = new ColaPrioridad<>();
    }

    // Recorre todas las atracciones y detecta las que superaron 500 visitantes
    public ListaEnlazada<Atraccion> verificarAtracciones() {
        ListaEnlazada<Atraccion> atraccionesEnAlerta = new ListaEnlazada<>();

        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona zona = parque.getZonas().obtener(i);
            ListaEnlazada<Atraccion> atracciones = zona.getAtracciones();

            for (int j = 0; j < atracciones.tamaño(); j++) {
                Atraccion a = atracciones.obtener(j);
                if (a.getEstado() == EstadoAtraccion.MANTENIMIENTO) {
                    atraccionesEnAlerta.agregar(a);
                }
            }
        }
        return atraccionesEnAlerta;
    }

    // Verifica una atracción específica
    public boolean requiereMantenimiento(Atraccion atraccion) {
        return atraccion.getContadorVisitantes() >= 500
                || atraccion.getEstado() == EstadoAtraccion.MANTENIMIENTO;
    }

    // Retorna cuántas atracciones están actualmente en mantenimiento
    public int getTotalEnMantenimiento() {
        return verificarAtracciones().tamaño();
    }

    // Getter para la cola de alertas
    public ColaPrioridad<String> getAlertas() {
        return alertas;
    }

    // Verifica si hay alertas pendientes
    public boolean hayAlertas() {
        return !alertas.estaVacia();
    }

    // Genera alerta prioritaria cuando una atracción supera 500 visitantes
    public void generarAlerta(Atraccion atraccion) {
        int prioridad = calcularPrioridad(atraccion);
        String mensaje = "⚠ MANTENIMIENTO REQUERIDO | "
                + atraccion.getNombre()
                + " | Visitantes acumulados: " + atraccion.getContadorVisitantes()
                + " | Zona: " + getZonaDeAtraccion(atraccion)
                + " | Prioridad: " + (prioridad == 1 ? "ALTA" : "NORMAL");
        alertas.encolar(mensaje, prioridad);
    }

    // Genera alertas para todas las atracciones que lo requieran
    public void verificarYGenerarAlertas() {
        ListaEnlazada<Atraccion> enAlerta = verificarAtracciones();
        for (int i = 0; i < enAlerta.tamaño(); i++) {
            Atraccion a = enAlerta.obtener(i);
            // Evitar duplicar alertas ya encoladas
            if (!alertaYaExiste(a.getNombre())) {
                generarAlerta(a);
            }
        }
    }

    // Desencola la siguiente alerta más prioritaria
    public String atenderSiguienteAlerta() {
        if (alertas.estaVacia()) return null;
        return alertas.desencolar();
    }

    // Prioridad 1 = ALTA si supera 500, prioridad 2 = NORMAL si está en mantenimiento
    private int calcularPrioridad(Atraccion atraccion) {
        return atraccion.getContadorVisitantes() >= 500 ? 1 : 2;
    }

    // Busca en qué zona está la atracción
    private String getZonaDeAtraccion(Atraccion atraccion) {
        for (int i = 0; i < parque.getZonas().tamaño(); i++) {
            Zona zona = parque.getZonas().obtener(i);
            if (zona.getAtraccionPorId(atraccion.getId()) != null) {
                return zona.getNombre();
            }
        }
        return "Sin zona";
    }

    // Verifica si ya existe una alerta para esa atracción en la cola
    private boolean alertaYaExiste(String nombreAtraccion) {
        ColaPrioridad<String> copia = new ColaPrioridad<>();
        boolean existe = false;

        while (!alertas.estaVacia()) {
            String alerta = alertas.desencolar();
            if (alerta.contains(nombreAtraccion)) existe = true;
            copia.encolar(alerta, 1);
        }

        // Restaurar cola original
        while (!copia.estaVacia()) {
            alertas.encolar(copia.desencolar(), 1);
        }
        return existe;
    }
}