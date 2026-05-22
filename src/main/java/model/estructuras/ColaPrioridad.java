package model.estructuras;

public class ColaPrioridad<T> {

    private class Nodo {
        T dato;
        int prioridad; // 1 = FastPass (primero), 2 = General/Familiar
        Nodo siguiente;

        Nodo(T dato, int prioridad) {
            this.dato = dato;
            this.prioridad = prioridad;
            this.siguiente = null;
        }
    }

    private Nodo frente;
    private int tamaño;

    public ColaPrioridad() {
        frente = null;
        tamaño = 0;
    }

    // Inserta respetando prioridad (menor número = mayor prioridad)
    public void encolar(T dato, int prioridad) {
        Nodo nuevo = new Nodo(dato, prioridad);
        if (frente == null || prioridad < frente.prioridad) {
            nuevo.siguiente = frente;
            frente = nuevo;
        } else {
            Nodo actual = frente;
            while (actual.siguiente != null && actual.siguiente.prioridad <= prioridad) {
                actual = actual.siguiente;
            }
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    // Saca el de mayor prioridad (frente)
    public T desencolar() {
        if (estaVacia()) throw new RuntimeException("La cola está vacía");
        T dato = frente.dato;
        frente = frente.siguiente;
        tamaño--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) throw new RuntimeException("La cola está vacía");
        return frente.dato;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int tamaño() {
        return tamaño;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo actual = frente;
        while (actual != null) {
            sb.append("(").append(actual.dato).append(", P").append(actual.prioridad).append(")");
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}