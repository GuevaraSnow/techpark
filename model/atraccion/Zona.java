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
}