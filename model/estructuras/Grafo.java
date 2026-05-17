package model.estructuras;

import model.atraccion.Atraccion;

public class Grafo {

    private class NodoGrafo {
        Atraccion atraccion;
        ListaEnlazada<Arista> aristas;

        NodoGrafo(Atraccion atraccion) {
            this.atraccion = atraccion;
            this.aristas = new ListaEnlazada<>();
        }
    }

    private class Arista {
        int idDestino;
        double peso;

        Arista(int idDestino, double peso) {
            this.idDestino = idDestino;
            this.peso = peso;
        }
    }

    private ListaEnlazada<NodoGrafo> nodos;

    public Grafo() {
        this.nodos = new ListaEnlazada<>();
    }

    // Agrega una atracción como nodo al grafo
    public void agregarNodo(Atraccion atraccion) {
        if (buscarNodo(atraccion.getId()) == null) {
            nodos.agregar(new NodoGrafo(atraccion));
        }
    }

    // Agrega arista bidireccional entre dos atracciones
    public void agregarArista(int idOrigen, int idDestino, double peso) {
        NodoGrafo origen  = buscarNodo(idOrigen);
        NodoGrafo destino = buscarNodo(idDestino);
        if (origen == null || destino == null) return;
        origen.aristas.agregar(new Arista(idDestino, peso));
        destino.aristas.agregar(new Arista(idOrigen, peso));
    }

    // Retorna la atracción de un nodo por ID
    public Atraccion getAtraccion(int id) {
        NodoGrafo nodo = buscarNodo(id);
        return nodo != null ? nodo.atraccion : null;
    }

    // Retorna todas las atracciones del grafo
    public ListaEnlazada<Atraccion> getAtracciones() {
        ListaEnlazada<Atraccion> resultado = new ListaEnlazada<>();
        for (int i = 0; i < nodos.tamaño(); i++) {
            resultado.agregar(nodos.obtener(i).atraccion);
        }
        return resultado;
    }

    // Retorna los IDs de los nodos vecinos de una atracción
    public ListaEnlazada<Integer> getVecinos(int idOrigen) {
        NodoGrafo nodo = buscarNodo(idOrigen);
        ListaEnlazada<Integer> vecinos = new ListaEnlazada<>();
        if (nodo == null) return vecinos;
        for (int i = 0; i < nodo.aristas.tamaño(); i++) {
            vecinos.agregar(nodo.aristas.obtener(i).idDestino);
        }
        return vecinos;
    }

    // Retorna el peso de la arista entre dos nodos
    public double getPeso(int idOrigen, int idDestino) {
        NodoGrafo nodo = buscarNodo(idOrigen);
        if (nodo == null) return -1;
        for (int i = 0; i < nodo.aristas.tamaño(); i++) {
            Arista a = nodo.aristas.obtener(i);
            if (a.idDestino == idDestino) return a.peso;
        }
        return -1;
    }

    public int tamaño() {
        return nodos.tamaño();
    }

    // Búsqueda interna de nodo por ID
    private NodoGrafo buscarNodo(int id) {
        for (int i = 0; i < nodos.tamaño(); i++) {
            NodoGrafo n = nodos.obtener(i);
            if (n.atraccion.getId() == id) return n;
        }
        return null;
    }
}