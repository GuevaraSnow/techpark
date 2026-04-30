package model.estructuras;

import model.atraccion.Atraccion;

public class ArbolBST {

    private class Nodo {
        Atraccion dato;
        Nodo izquierdo;
        Nodo derecho;

        Nodo(Atraccion dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    private Nodo raiz;

    public ArbolBST() {
        raiz = null;
    }

    // Inserta una atracción ordenada por nombre
    public void insertar(Atraccion atraccion) {
        raiz = insertarRec(raiz, atraccion);
    }

    private Nodo insertarRec(Nodo actual, Atraccion atraccion) {
        if (actual == null) return new Nodo(atraccion);
        int cmp = atraccion.getNombre().compareToIgnoreCase(actual.dato.getNombre());
        if (cmp < 0) {
            actual.izquierdo = insertarRec(actual.izquierdo, atraccion);
        } else if (cmp > 0) {
            actual.derecho = insertarRec(actual.derecho, atraccion);
        }
        // Si cmp == 0 (mismo nombre) no se inserta duplicado
        return actual;
    }

    // Busca por nombre, retorna la atracción o null si no existe
    public Atraccion buscar(String nombre) {
        return buscarRec(raiz, nombre);
    }

    private Atraccion buscarRec(Nodo actual, String nombre) {
        if (actual == null) return null;
        int cmp = nombre.compareToIgnoreCase(actual.dato.getNombre());
        if (cmp == 0) return actual.dato;
        if (cmp < 0) return buscarRec(actual.izquierdo, nombre);
        return buscarRec(actual.derecho, nombre);
    }

    // Elimina por nombre (3 casos: hoja, un hijo, dos hijos)
    public void eliminar(String nombre) {
        raiz = eliminarRec(raiz, nombre);
    }

    private Nodo eliminarRec(Nodo actual, String nombre) {
        if (actual == null) return null;
        int cmp = nombre.compareToIgnoreCase(actual.dato.getNombre());
        if (cmp < 0) {
            actual.izquierdo = eliminarRec(actual.izquierdo, nombre);
        } else if (cmp > 0) {
            actual.derecho = eliminarRec(actual.derecho, nombre);
        } else {
            // Caso 1: hoja
            if (actual.izquierdo == null && actual.derecho == null) return null;
            // Caso 2: un solo hijo
            if (actual.izquierdo == null) return actual.derecho;
            if (actual.derecho == null) return actual.izquierdo;
            // Caso 3: dos hijos → sucesor inorden (mínimo del subárbol derecho)
            Atraccion sucesor = minimoNodo(actual.derecho);
            actual.dato = sucesor;
            actual.derecho = eliminarRec(actual.derecho, sucesor.getNombre());
        }
        return actual;
    }

    private Atraccion minimoNodo(Nodo nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo.dato;
    }

    // Recorrido inorden → devuelve atracciones ordenadas alfabéticamente
    public ListaEnlazada<Atraccion> inorden() {
        ListaEnlazada<Atraccion> resultado = new ListaEnlazada<>();
        inordenRec(raiz, resultado);
        return resultado;
    }

    private void inordenRec(Nodo actual, ListaEnlazada<Atraccion> lista) {
        if (actual == null) return;
        inordenRec(actual.izquierdo, lista);
        lista.agregar(actual.dato);
        inordenRec(actual.derecho, lista);
    }

    public boolean estaVacio() {
        return raiz == null;
    }
}