package test;

import model.estructuras.SetSimple;
import model.atraccion.Atraccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SetSimpleTest {

    private SetSimple<String> set;
    private SetSimple<Atraccion> setAtracciones;
    private Atraccion montana;
    private Atraccion tobogan;

    @BeforeEach
    void setUp() {
        set = new SetSimple<>();
        set.agregar("Montaña Rusa");
        set.agregar("Tobogán");
        set.agregar("Carrusel");

        setAtracciones = new SetSimple<>();
        montana  = new Atraccion(1, "Montaña Rusa", "mecánica de altura",
                20, 1.40, 12, 5000);
        tobogan  = new Atraccion(2, "Tobogán Acuático", "acuática",
                15, 1.20,  8, 3000);
        setAtracciones.agregar(montana);
        setAtracciones.agregar(tobogan);
    }

    @Test
    void agregarElementoNuevo() {
        boolean resultado = set.agregar("Zipper");
        assertTrue(resultado);
        assertEquals(4, set.tamaño());
    }

    @Test
    void agregarDuplicadoRetornaFalse() {
        boolean resultado = set.agregar("Montaña Rusa");
        assertFalse(resultado);
        assertEquals(3, set.tamaño());
    }

    @Test
    void contieneElementoExistente() {
        assertTrue(set.contiene("Tobogán"));
    }

    @Test
    void noContieneElementoInexistente() {
        assertFalse(set.contiene("Torre del Terror"));
    }

    @Test
    void eliminarElementoExistente() {
        boolean resultado = set.eliminar("Carrusel");
        assertTrue(resultado);
        assertFalse(set.contiene("Carrusel"));
        assertEquals(2, set.tamaño());
    }

    @Test
    void eliminarElementoInexistenteRetornaFalse() {
        boolean resultado = set.eliminar("Zipper");
        assertFalse(resultado);
        assertEquals(3, set.tamaño());
    }

    @Test
    void estaVacioEnSetNuevo() {
        SetSimple<String> nuevo = new SetSimple<>();
        assertTrue(nuevo.estaVacio());
    }

    @Test
    void noEstaVacioConElementos() {
        assertFalse(set.estaVacio());
    }

    @Test
    void listarRetornaTodosLosElementos() {
        assertEquals(3, set.listar().tamaño());
    }

    @Test
    void agregarAtraccionComoFavoritoSinDuplicado() {
        setAtracciones.agregar(montana); // duplicado
        assertEquals(2, setAtracciones.tamaño());
    }

    @Test
    void eliminarAtraccionDeFavoritos() {
        setAtracciones.eliminar(montana);
        assertFalse(setAtracciones.contiene(montana));
        assertEquals(1, setAtracciones.tamaño());
    }
}