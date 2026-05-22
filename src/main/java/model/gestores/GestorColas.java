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

    public GestorColas() {
        this.colas = new ListaEnlazada<>();
    }

    // Registra una atracción en el gestor al iniciar el sistema
    public void registrarAtraccion(Atraccion atraccion) {
        if (buscarEntrada(atraccion.getId()) == null) {
            colas.agregar(new EntradaCola(atraccion.getId()));
        }
    }

    // Encola un visitante según la prioridad de su ticket
    public boolean encolar(Visitante visitante, Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null) return false;
        int prioridad = visitante.getTicket().getPrioridad();
        entrada.cola.encolar(visitante, prioridad);
        return true;
    }

    // Desencola el siguiente visitante (respeta prioridad FastPass > General)
    public Visitante desencolar(Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null || entrada.cola.estaVacia()) return null;
        return entrada.cola.desencolar();
    }

    // Ver quién es el siguiente sin sacarlo de la cola
    public Visitante verSiguiente(Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null || entrada.cola.estaVacia()) return null;
        return entrada.cola.verFrente();
    }

    // Tamaño de la cola de una atracción
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

    // Busca la entrada de cola por ID de atracción
    private EntradaCola buscarEntrada(int idAtraccion) {
        for (int i = 0; i < colas.tamaño(); i++) {
            EntradaCola e = colas.obtener(i);
            if (e.idAtraccion == idAtraccion) return e;
        }
        return null;
    }

    // Verifica si un visitante ya está en la cola de una atracción
    public boolean estaEnCola(Visitante visitante, Atraccion atraccion) {
        EntradaCola entrada = buscarEntrada(atraccion.getId());
        if (entrada == null) return false;
        // Recorrer la cola buscando al visitante por documento
        ColaPrioridad<Visitante> copiaCola = new ColaPrioridad<>();
        boolean encontrado = false;
        while (!entrada.cola.estaVacia()) {
            Visitante v = entrada.cola.desencolar();
            if (v.getDocumento().equals(visitante.getDocumento())) {
                encontrado = true;
            }
            copiaCola.encolar(v, v.getTicket().getPrioridad());
        }
        // Restaurar la cola original
        while (!copiaCola.estaVacia()) {
            Visitante v = copiaCola.desencolar();
            entrada.cola.encolar(v, v.getTicket().getPrioridad());
        }
        return encontrado;
    }

    // Encolar con validación: no permitir duplicados en cola
    public boolean encolarSeguro(Visitante visitante, Atraccion atraccion) {
        if (estaEnCola(visitante, atraccion)) return false;
        return encolar(visitante, atraccion);
    }

    private ListaEnlazada<String> notificaciones;

// Inicializar en constructor:

    // Desencolar con registro de notificación
    public Visitante desencolarConNotificacion(Atraccion atraccion) {
        Visitante siguiente = desencolar(atraccion);
        if (siguiente != null) {
            String notif = "✅ " + siguiente.getNombre()
                    + " — es tu turno en: " + atraccion.getNombre();
            notificaciones.agregar(notif);
        }
        return siguiente;
    }

    public ListaEnlazada<String> getNotificaciones() {
        return notificaciones;
    }

    public void limpiarNotificaciones() {
        notificaciones = new ListaEnlazada<>();
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

        // Restaurar cola
        while (!copiaCola.estaVacia()) {
            Visitante v = copiaCola.desencolar();
            entrada.cola.encolar(v, v.getTicket().getPrioridad());
        }

        return posicion; // -1 si no está en cola
    }
}