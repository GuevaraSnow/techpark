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
}