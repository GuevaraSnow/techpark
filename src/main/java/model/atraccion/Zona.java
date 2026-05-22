package model.atraccion;

import model.Operador;
import model.estructuras.ListaEnlazada;

public class Zona {

    private String nombre;
    private ListaEnlazada<Atraccion> atracciones;
    private ListaEnlazada<Operador> operadores;

    public Zona(String nombre) {
        this.nombre = nombre;
        this.atracciones = new ListaEnlazada<>();
        this.operadores = new ListaEnlazada<>();
    }

    public void agregarAtraccion(Atraccion atraccion) {
        atracciones.agregar(atraccion);
    }

    public void agregarOperador(Operador operador) {
        operadores.agregar(operador);
    }

    public boolean tieneOperador() {
        return !operadores.estaVacia();
    }

    public Atraccion getAtraccionPorId(int id) {
        for (int i = 0; i < atracciones.tamaño(); i++) {
            Atraccion a = atracciones.obtener(i);
            if (a.getId() == id) return a;
        }
        return null;
    }

    // Getters
    public String getNombre() { return nombre; }
    public ListaEnlazada<Atraccion> getAtracciones() { return atracciones; }
    public ListaEnlazada<Operador> getOperadores() { return operadores; }

    @Override
    public String toString() {
        return "Zona: " + nombre + " | Atracciones: " + atracciones.tamaño()
                + " | Operadores: " + operadores.tamaño();
    }
}