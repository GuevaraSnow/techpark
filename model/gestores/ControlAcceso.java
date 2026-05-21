package model.gestores;

import model.Visitante;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;

public class ControlAcceso {

    // Resultado de validación con mensaje descriptivo
    public static class ResultadoValidacion {
        public final boolean aprobado;
        public final String mensaje;

        ResultadoValidacion(boolean aprobado, String mensaje) {
            this.aprobado = aprobado;
            this.mensaje = mensaje;
        }
    }

    // Valida edad y estatura del visitante contra los requisitos de la atracción
    public ResultadoValidacion validarRestricciones(Visitante visitante, Atraccion atraccion) {

        if (visitante.getEdad() < atraccion.getEdadMin()) {
            return new ResultadoValidacion(false,
                    "❌ Edad insuficiente. Mínimo requerido: "
                            + atraccion.getEdadMin() + " años. "
                            + "Edad del visitante: " + visitante.getEdad() + " años.");
        }

        if (visitante.getEstatura() < atraccion.getAlturaMin()) {
            return new ResultadoValidacion(false,
                    "❌ Estatura insuficiente. Mínimo requerido: "
                            + atraccion.getAlturaMin() + " m. "
                            + "Estatura del visitante: " + visitante.getEstatura() + " m.");
        }

        return new ResultadoValidacion(true,
                "✅ Restricciones cumplidas. Edad y estatura verificadas.");
    }

    // Valida que la atracción esté activa y no en mantenimiento o cerrada
    public ResultadoValidacion validarEstadoAtraccion(Atraccion atraccion) {
        if (atraccion.getEstado() != EstadoAtraccion.ACTIVA) {
            return new ResultadoValidacion(false,
                    "❌ Atracción no disponible. Estado actual: "
                            + atraccion.getEstado()
                            + (atraccion.getMotivoCierre().isEmpty() ? "" :
                            " | Motivo: " + atraccion.getMotivoCierre()));
        }
        return new ResultadoValidacion(true, "✅ Atracción activa.");
    }

    // Valida capacidad del ciclo actual
    public ResultadoValidacion validarCapacidad(Atraccion atraccion) {
        if (atraccion.getContadorActual() >= atraccion.getCapacidad()) {
            return new ResultadoValidacion(false,
                    "❌ Capacidad del ciclo llena. Máximo: "
                            + atraccion.getCapacidad()
                            + " visitantes por ciclo.");
        }
        return new ResultadoValidacion(true,
                "✅ Capacidad disponible: "
                        + (atraccion.getCapacidad() - atraccion.getContadorActual())
                        + " lugares.");
    }
}