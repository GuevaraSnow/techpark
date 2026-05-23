package test;

import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AtraccionTest {

    private Atraccion atraccion;

    @BeforeEach
    void setUp() {
        atraccion = new Atraccion(1, "Montaña Rusa", "mecánica de altura",
                20, 1.40, 12, 5000);
    }

    @Test
    void estadoInicialEsActiva() {
        assertEquals(EstadoAtraccion.ACTIVA, atraccion.getEstado());
    }

    @Test
    void bloqueoAutomaticoA500Visitantes() {
        for (int i = 0; i < 500; i++) {
            atraccion.incrementarContador();
        }
        assertEquals(EstadoAtraccion.MANTENIMIENTO, atraccion.getEstado());
    }

    @Test
    void noSeBloqueoAntesDe500() {
        for (int i = 0; i < 499; i++) {
            atraccion.incrementarContador();
        }
        assertEquals(EstadoAtraccion.ACTIVA, atraccion.getEstado());
    }

    @Test
    void registrarRevisionTecnicaReactivaAtraccion() {
        for (int i = 0; i < 500; i++) {
            atraccion.incrementarContador();
        }
        assertEquals(EstadoAtraccion.MANTENIMIENTO, atraccion.getEstado());
        atraccion.registrarRevisionTecnica();
        assertEquals(EstadoAtraccion.ACTIVA, atraccion.getEstado());
    }

    @Test
    void registrarRevisionTecnicaResetaContador() {
        for (int i = 0; i < 500; i++) {
            atraccion.incrementarContador();
        }
        atraccion.registrarRevisionTecnica();
        assertEquals(0, atraccion.getContadorVisitantes());
    }

    @Test
    void cerrarPorClimaCambiaEstado() {
        atraccion.cerrarPorClima("Tormenta eléctrica");
        assertEquals(EstadoAtraccion.CERRADA, atraccion.getEstado());
    }

    @Test
    void cerrarPorClimaGuardaMotivo() {
        atraccion.cerrarPorClima("Lluvia fuerte");
        assertEquals("Cierre por clima: Lluvia fuerte",
                atraccion.getMotivoCierre());
    }

    @Test
    void esAfectadaPorClimaAcuatica() {
        Atraccion acuatica = new Atraccion(2, "Tobogán", "acuática",
                15, 1.20, 8, 3000);
        assertTrue(acuatica.esAfectadaPorClima());
    }

    @Test
    void esAfectadaPorClimaMecanicaAltura() {
        assertTrue(atraccion.esAfectadaPorClima());
    }

    @Test
    void noEsAfectadaPorClimaOtroTipo() {
        Atraccion carrusel = new Atraccion(3, "Carrusel", "otra",
                30, 0.80, 3, 0);
        assertFalse(carrusel.esAfectadaPorClima());
    }
}