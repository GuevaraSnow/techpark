package Model.Atraccion;

public class Atraccion {

    private int id;
    private String nombre;
    private String tipo;
    private int capacidad;
    private double alturaMin;
    private int edadMin;
    private double costoExtra;
    private EstadoAtraccion estado;
    private int contadorVisitantes;

    public Atraccion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.estado = EstadoAtraccion.ACTIVA;
    }

    public void incrementarContador() {
        contadorVisitantes++;
        if (contadorVisitantes >= 500) {
            estado = EstadoAtraccion.MANTENIMIENTO;
        }
    }
}