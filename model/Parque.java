package model;

import model.atraccion.Atraccion;
import model.atraccion.Zona;
import model.estructuras.ArbolBST;
import model.estructuras.Grafo;
import model.estructuras.ListaEnlazada;

public class Parque {

    private String nombre;
    private int capacidadMaxima;
    private int visitantesActuales;
    private ListaEnlazada<Zona> zonas;
    private ListaEnlazada<Visitante> visitantes;
    private ListaEnlazada<Empleado> empleados;
    private ArbolBST catalogoAtracciones;
    private Grafo mapa;

    public Parque(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.visitantesActuales = 0;
        this.zonas = new ListaEnlazada<>();
        this.visitantes = new ListaEnlazada<>();
        this.empleados = new ListaEnlazada<>();
        this.catalogoAtracciones = new ArbolBST();
        this.mapa = new Grafo();
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public int getVisitantesActuales() { return visitantesActuales; }
    public ListaEnlazada<Zona> getZonas() { return zonas; }
    public ListaEnlazada<Visitante> getVisitantes() { return visitantes; }
    public ListaEnlazada<Empleado> getEmpleados() { return empleados; }
    public ArbolBST getCatalogoAtracciones() { return catalogoAtracciones; }
    public Grafo getMapa() { return mapa; }

    @Override
    public String toString() {
        return "Parque: " + nombre
                + " | Aforo: " + visitantesActuales + "/" + capacidadMaxima
                + " | Zonas: " + zonas.tamaño()
                + " | Atracciones: " + mapa.tamaño();
    }

    java// Agregar a Parque.java

// ── Zonas ─────────────────────────────────────────────────────────

    public void agregarZona(Zona zona) {
        if (buscarZona(zona.getNombre()) == null) {
            zonas.agregar(zona);
        }
    }

    public Zona buscarZona(String nombre) {
        for (int i = 0; i < zonas.tamaño(); i++) {
            Zona z = zonas.obtener(i);
            if (z.getNombre().equalsIgnoreCase(nombre)) return z;
        }
        return null;
    }

    public Zona buscarZonaPorId(int idAtraccion) {
        for (int i = 0; i < zonas.tamaño(); i++) {
            Zona z = zonas.obtener(i);
            if (z.getAtraccionPorId(idAtraccion) != null) return z;
        }
        return null;
    }

// ── Atracciones ───────────────────────────────────────────────────

    public boolean agregarAtraccionAZona(String nombreZona, Atraccion atraccion) {
        Zona zona = buscarZona(nombreZona);
        if (zona == null) return false;

        // Agregar a la zona
        zona.agregarAtraccion(atraccion);

        // Agregar al catálogo ArbolBST
        catalogoAtracciones.insertar(atraccion);

        // Agregar al grafo como nodo
        mapa.agregarNodo(atraccion);

        return true;
    }

    public Atraccion buscarAtraccion(String nombre) {
        return catalogoAtracciones.buscar(nombre);
    }

    public boolean eliminarAtraccion(String nombre) {
        Atraccion atraccion = catalogoAtracciones.buscar(nombre);
        if (atraccion == null) return false;

        // Eliminar de su zona
        Zona zona = buscarZonaPorId(atraccion.getId());
        if (zona != null) {
            zona.getAtracciones().eliminarDato(atraccion);
        }

        // Eliminar del catálogo
        catalogoAtracciones.eliminar(nombre);

        return true;
    }

// ── Empleados ─────────────────────────────────────────────────────

    public void agregarEmpleado(Empleado empleado) {
        empleados.agregar(empleado);
    }

// ── Visitantes ────────────────────────────────────────────────────

    public void agregarVisitante(Visitante visitante) {
        visitantes.agregar(visitante);
    }

    public Visitante buscarVisitante(String documento) {
        for (int i = 0; i < visitantes.tamaño(); i++) {
            Visitante v = visitantes.obtener(i);
            if (v.getDocumento().equals(documento)) return v;
        }
        return null;
    }
}