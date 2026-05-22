package model.estructuras;

public class SetSimple<T> {

    private ListaEnlazada<T> elementos;

    public SetSimple() {
        this.elementos = new ListaEnlazada<>();
    }

    // Solo agrega si no existe (sin duplicados)
    public boolean agregar(T dato) {
        if (elementos.contiene(dato)) return false;
        elementos.agregar(dato);
        return true;
    }

    public boolean eliminar(T dato) {
        return elementos.eliminarDato(dato);
    }

    public boolean contiene(T dato) {
        return elementos.contiene(dato);
    }

    // Devuelve copia de los elementos como ListaEnlazada
    public ListaEnlazada<T> listar() {
        return elementos;
    }

    public int tamaño() {
        return elementos.tamaño();
    }

    public boolean estaVacio() {
        return elementos.estaVacia();
    }

    @Override
    public String toString() {
        return elementos.toString();
    }
}