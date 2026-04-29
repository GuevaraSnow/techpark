package model.atraccion;

import model.Visitante;

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

    public Atraccion(int id, String nombre, String tipo, int capacidad,
                     double alturaMin, int edadMin, double costoExtra) {

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.alturaMin = alturaMin;
        this.edadMin = edadMin;
        this.costoExtra = costoExtra;

        this.estado = EstadoAtraccion.ACTIVA;
        this.contadorVisitantes = 0;
    }

    public boolean verificarRestricciones(Visitante v) {
        return v.getEdad() >= edadMin && v.getEstatura() >= alturaMin;
    }

    public void incrementarContador() {
        contadorVisitantes++;
        if (contadorVisitantes >= 500) {
            estado = EstadoAtraccion.MANTENIMIENTO;
        }
    }

    public void setEstado(EstadoAtraccion estado) {
        this.estado = estado;
    }

    public EstadoAtraccion getEstado() {
        return estado;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }
}