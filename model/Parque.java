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
}