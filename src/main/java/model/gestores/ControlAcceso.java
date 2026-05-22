package model.gestores;

import model.Visitante;
import model.atraccion.Atraccion;
import model.atraccion.EstadoAtraccion;
import model.ticket.TipoTicket;

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

    // Valida si el visitante tiene saldo suficiente para la atracción
    public ResultadoValidacion validarSaldo(Visitante visitante, Atraccion atraccion) {

        // FastPass no paga costo extra
        if (visitante.getTicket().getTipo() == TipoTicket.FASTPASS) {
            return new ResultadoValidacion(true,
                    "✅ Ticket FastPass no requiere pago adicional.");
        }

        // Si la atracción no tiene costo extra no se valida saldo
        if (atraccion.getCostoExtra() <= 0) {
            return new ResultadoValidacion(true,
                    "✅ Atracción sin costo adicional.");
        }

        // Verificar saldo suficiente
        if (visitante.getSaldo() < atraccion.getCostoExtra()) {
            return new ResultadoValidacion(false,
                    "❌ Saldo insuficiente. Costo requerido: $"
                            + atraccion.getCostoExtra()
                            + " | Saldo actual: $" + visitante.getSaldo());
        }

        return new ResultadoValidacion(true,
                "✅ Saldo suficiente. Se descontarán $"
                        + atraccion.getCostoExtra()
                        + " al autorizar el ingreso.");
    }

    // Ejecuta toda la cadena de validación y autoriza o rechaza el ingreso
    public ResultadoValidacion autorizarIngreso(Visitante visitante, Atraccion atraccion) {

        // 1. Validar estado de la atracción
        ResultadoValidacion estadoResult = validarEstadoAtraccion(atraccion);
        if (!estadoResult.aprobado) return estadoResult;

        // 2. Validar restricciones de edad y estatura
        ResultadoValidacion restriccionResult = validarRestricciones(visitante, atraccion);
        if (!restriccionResult.aprobado) return restriccionResult;

        // 3. Validar capacidad del ciclo actual
        ResultadoValidacion capacidadResult = validarCapacidad(atraccion);
        if (!capacidadResult.aprobado) return capacidadResult;

        // 4. Validar saldo si aplica
        ResultadoValidacion saldoResult = validarSaldo(visitante, atraccion);
        if (!saldoResult.aprobado) return saldoResult;
        

        // Descontar saldo si hay costo extra
        if (atraccion.getCostoExtra() > 0
                && visitante.getTicket().getTipo() != model.ticket.TipoTicket.FASTPASS) {
            visitante.descontarSaldo(atraccion.getCostoExtra());
        }

        // Incrementar contadores de la atracción
        atraccion.incrementarContador();

        // Registrar en historial del visitante
        visitante.agregarAlHistorial(atraccion);

        return new ResultadoValidacion(true,
                "✅ Ingreso autorizado. Bienvenido/a a "
                        + atraccion.getNombre() + ", " + visitante.getNombre() + "."
                        + (atraccion.getCostoExtra() > 0 ? " Se descontaron $"
                        + atraccion.getCostoExtra() + " de tu saldo." : ""));
    }
}