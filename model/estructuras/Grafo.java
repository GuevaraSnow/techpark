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
    

    public ListaEnlazada<Atraccion> BFS(int idOrigen) {
        ListaEnlazada<Atraccion> resultado = new ListaEnlazada<>();
        ListaEnlazada<Integer> visitados = new ListaEnlazada<>();
        ListaEnlazada<Integer> cola = new ListaEnlazada<>();

        if (buscarNodo(idOrigen) == null) return resultado;

        cola.agregar(idOrigen);
        visitados.agregar(idOrigen);

        while (!cola.estaVacia()) {
            int idActual = cola.obtener(0);
            cola.eliminar(0);

            resultado.agregar(getAtraccion(idActual));

            ListaEnlazada<Integer> vecinos = getVecinos(idActual);
            for (int i = 0; i < vecinos.tamaño(); i++) {
                int idVecino = vecinos.obtener(i);
                if (!visitados.contiene(idVecino)) {
                    visitados.agregar(idVecino);
                    cola.agregar(idVecino);
                }
            }
        }
        return resultado;
    }
    public ListaEnlazada<Atraccion> dijkstra(int idOrigen, int idDestino) {
        ListaEnlazada<Atraccion> ruta = new ListaEnlazada<>();
        if (buscarNodo(idOrigen) == null || buscarNodo(idDestino) == null) return ruta;

        // Distancias y predecesores
        int[] ids = getIds();
        double[] distancias = new double[ids.length];
        int[] predecesores = new int[ids.length];
        boolean[] visitados = new boolean[ids.length];

        // Inicializar
        for (int i = 0; i < ids.length; i++) {
            distancias[i] = Double.MAX_VALUE;
            predecesores[i] = -1;
            visitados[i] = false;
        }
        distancias[indexOf(ids, idOrigen)] = 0;

        for (int iter = 0; iter < ids.length; iter++) {
            // Nodo no visitado con menor distancia
            int idActual = -1;
            double minDist = Double.MAX_VALUE;
            for (int i = 0; i < ids.length; i++) {
                if (!visitados[i] && distancias[i] < minDist) {
                    minDist = distancias[i];
                    idActual = ids[i];
                }
            }
            if (idActual == -1) break;
            visitados[indexOf(ids, idActual)] = true;

            // Relajar vecinos
            ListaEnlazada<Integer> vecinos = getVecinos(idActual);
            for (int i = 0; i < vecinos.tamaño(); i++) {
                int idVecino = vecinos.obtener(i);
                int idxVecino = indexOf(ids, idVecino);
                double nuevaDist = distancias[indexOf(ids, idActual)] + getPeso(idActual, idVecino);
                if (nuevaDist < distancias[idxVecino]) {
                    distancias[idxVecino] = nuevaDist;
                    predecesores[idxVecino] = idActual;
                }
            }
        }

        // Reconstruir ruta desde destino hacia origen
        ListaEnlazada<Atraccion> rutaInversa = new ListaEnlazada<>();
        int actual = idDestino;
        while (actual != -1) {
            rutaInversa.agregar(getAtraccion(actual));
            int idx = indexOf(ids, actual);
            actual = predecesores[idx];
        }

        // Invertir para obtener ruta origen → destino
        for (int i = rutaInversa.tamaño() - 1; i >= 0; i--) {
            ruta.agregar(rutaInversa.obtener(i));
        }

        return ruta;
    }

    // Retorna array con todos los IDs de nodos del grafo
    private int[] getIds() {
        int[] ids = new int[nodos.tamaño()];
        for (int i = 0; i < nodos.tamaño(); i++) {
            ids[i] = nodos.obtener(i).atraccion.getId();
        }
        return ids;
    }

    // Busca el índice de un ID en el array
    private int indexOf(int[] arr, int valor) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == valor) return i;
        }
        return -1;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MAPA DEL PARQUE ===\n");
        sb.append("Total de atracciones: ").append(nodos.tamaño()).append("\n\n");

        for (int i = 0; i < nodos.tamaño(); i++) {
            NodoGrafo nodo = nodos.obtener(i);
            sb.append("🎢 [").append(nodo.atraccion.getId()).append("] ")
                    .append(nodo.atraccion.getNombre())
                    .append(" (").append(nodo.atraccion.getEstado()).append(")")
                    .append("\n");

            if (nodo.aristas.estaVacia()) {
                sb.append("   └── Sin conexiones\n");
            } else {
                for (int j = 0; j < nodo.aristas.tamaño(); j++) {
                    Arista a = nodo.aristas.obtener(j);
                    Atraccion destino = getAtraccion(a.idDestino);
                    String conector = (j == nodo.aristas.tamaño() - 1) ? "└──" : "├──";
                    sb.append("   ").append(conector).append(" → ")
                            .append(destino.getNombre())
                            .append(" (").append(a.peso).append(" m)\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}