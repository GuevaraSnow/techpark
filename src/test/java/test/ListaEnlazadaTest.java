// src/test/java/ListaEnlazadaTest.java
package test;

import model.estructuras.ListaEnlazada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListaEnlazadaTest {

    private ListaEnlazada<String> lista;

    @BeforeEach
    void setUp() {
        lista = new ListaEnlazada<>();
        lista.agregar("Montaña Rusa");
        lista.agregar("Tobogán");
        lista.agregar("Carrusel");
    }

    @Test
    void agregarYObtenerElemento() {
        assertEquals("Montaña Rusa", lista.obtener(0));
        assertEquals("Tobogán",      lista.obtener(1));
        assertEquals("Carrusel",     lista.obtener(2));
    }

    @Test
    void tamañoActualizaCorrectamente() {
        assertEquals(3, lista.tamaño());
        lista.agregar("Zipper");
        assertEquals(4, lista.tamaño());
    }

    @Test
    void eliminarPorIndice() {
        lista.eliminar(1); // elimina "Tobogán"
        assertEquals(2,          lista.tamaño());
        assertEquals("Carrusel", lista.obtener(1));
    }

    @Test
    void eliminarDato() {
        boolean resultado = lista.eliminarDato("Carrusel");
        assertTrue(resultado);
        assertFalse(lista.contiene("Carrusel"));
        assertEquals(2, lista.tamaño());
    }

    @Test
    void eliminarDatoInexistenteRetornaFalse() {
        boolean resultado = lista.eliminarDato("Zipper");
        assertFalse(resultado);
        assertEquals(3, lista.tamaño());
    }

    @Test
    void contieneElementoExistente() {
        assertTrue(lista.contiene("Tobogán"));
    }

    @Test
    void contieneElementoInexistente() {
        assertFalse(lista.contiene("Torre del Terror"));
    }

    @Test
    void estaVaciaEnListaNueva() {
        ListaEnlazada<String> nueva = new ListaEnlazada<>();
        assertTrue(nueva.estaVacia());
    }

    @Test
    void noEstaVaciaConElementos() {
        assertFalse(lista.estaVacia());
    }

    @Test
    void indiceInvalidoLanzaExcepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(10));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(-1));
    }

    @Test
    void toArrayRetornaTamañoCorrecto() {
        Object[] arr = lista.toArray();
        assertEquals(3, arr.length);
        assertEquals("Montaña Rusa", arr[0]);
    }
}