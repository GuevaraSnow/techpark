package model.estructuras;

public class ListaEnlazada<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cabeza;
    private int tamaño;

    public ListaEnlazada() {
        cabeza = null;
        tamaño = 0;
    }

    // Agrega al final
    public void agregar(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    // Obtiene por índice (0-based)
    public T obtener(int indice) {
        validarIndice(indice);
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    // Elimina por índice
    public void eliminar(int indice) {
        validarIndice(indice);
        if (indice == 0) {
            cabeza = cabeza.siguiente;
        } else {
            Nodo actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.siguiente;
            }
            actual.siguiente = actual.siguiente.siguiente;
        }
        tamaño--;
    }

    // Elimina por referencia al objeto
    public boolean eliminarDato(T dato) {
        if (cabeza == null) return false;
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return true;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean contiene(T dato) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(dato)) return true;
            actual = actual.siguiente;
        }
        return false;
    }

    public int tamaño() {
        return tamaño;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    // Útil para recorrer en for-each o pasar a GUI
    public Object[] toArray() {
        Object[] arr = new Object[tamaño];
        Nodo actual = cabeza;
        for (int i = 0; i < tamaño; i++) {
            arr[i] = actual.dato;
            actual = actual.siguiente;
        }
        return arr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
    }
}