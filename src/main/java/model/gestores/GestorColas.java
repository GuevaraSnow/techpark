package model.gestores;

import model.Visitante;
import model.atraccion.Atraccion;
import model.estructuras.ColaPrioridad;
import model.estructuras.ListaEnlazada;

public class GestorColas {

    private class EntradaCola {
        int idAtraccion;
        ColaPrioridad<Visitante> cola;

        EntradaCola(int idAtraccion) {
            this.idAtraccion = idAtraccion;
            this.cola = new ColaPrioridad<>();
        }
    }

    private ListaEnlazada<EntradaCola> colas;
    private ListaEnlazada<String> notificaciones; // ✅ declarado aquí

    public GestorColas() {
        this.colas = new ListaEnlazada<>();
        this.notificaciones = new ListaEnlazada<>(); // ✅ inicializado en constructor
    }

    public void registrarAtraccion(Atraccion atraccion) {
        if (buscarEntrada(atraccion.getId()) == null) {
            colas.agregar(new EntradaCola(atraccion.getId()));
        }
    }

    public boolean encolar(Visitante visitante, Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null) return false;
        int prioridad = visitante.getTicket().getPrioridad();
        entrada.cola.encolar(visitante, prioridad);
        return true;
    }

    public boolean estaEnCola(Visitante visitante, Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null) return false;
        ColaPrioridad<Visitante> copiaCola = new ColaPrioridad<>();
        boolean encontrado = false;
        while (!entrada.cola.estaVacia()) {
            Visitante v = entrada.cola.desencolar();
            if (v.getDocumento().equals(visitante.getDocumento())) {
                encontrado = true;
            }
            copiaCola.encolar(v, v.getTicket().getPrioridad());
        }
        while (!copiaCola.estaVacia()) {
            Visitante v = copiaCola.desencolar();
            entrada.cola.encolar(v, v.getTicket().getPrioridad());
        }
        return encontrado;
    }

    public boolean encolarSeguro(Visitante visitante, Atraccion atraccion) {
        if (estaEnCola(visitante, atraccion)) return false;
        return encolar(visitante, atraccion);
    }

    public Visitante desencolar(Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null || entrada.cola.estaVacia()) return null;
        return entrada.cola.desencolar();
    }

    public Visitante desencolarConNotificacion(Atraccion atraccion) {
        Visitante siguiente = desencolar(atraccion);
        if (siguiente != null) {
            String notif = "✅ " + siguiente.getNombre()
                    + " — es tu turno en: " + atraccion.getNombre();
            notificaciones.agregar(notif); // ✅ ya no es null
        }
        return siguiente;
    }

    public Visitante verSiguiente(Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null || entrada.cola.estaVacia()) return null;
        return entrada.cola.verFrente();
    }

    public int getPosicion(Visitante visitante, Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null || entrada.cola.estaVacia()) return -1;
        ColaPrioridad<Visitante> copiaCola = new ColaPrioridad<>();
        int posicion = -1;
        int contador = 1;
        while (!entrada.cola.estaVacia()) {
            Visitante v = entrada.cola.desencolar();
            if (v.getDocumento().equals(visitante.getDocumento())) {
                posicion = contador;
            }
            copiaCola.encolar(v, v.getTicket().getPrioridad());
            contador++;
        }
        while (!copiaCola.estaVacia()) {
            Visitante v = copiaCola.desencolar();
            entrada.cola.encolar(v, v.getTicket().getPrioridad());
        }
        return posicion;
    }

    public int tamañoCola(Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null) return 0;
        return entrada.cola.tamaño();
    }

    public boolean colaVacia(Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null) return true;
        return entrada.cola.estaVacia();
    }

    public ListaEnlazada<String> getNotificaciones() {
        return notificaciones;
    }

    public void limpiarNotificaciones() {
        notificaciones = new ListaEnlazada<>();
    }

    private EntradaCola buscarEntrada(int idAtraccion) {
        for (int i = 0; i < colas.tamaño(); i++) {
            EntradaCola e = colas.obtener(i);
            if (e.idAtraccion == idAtraccion) return e;
        }
        return null;
    }
}