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
    private int contadorActual;
    private double tiempoEspera;
    private String motivoCierre;

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
        this.contadorActual = 0;
        this.tiempoEspera = 0;
        this.motivoCierre = "";
    }

    public boolean verificarRestricciones(Visitante v) {
        return v.getEdad() >= edadMin && v.getEstatura() >= alturaMin;
    }

    public void incrementarContador() {
        contadorVisitantes++;
        contadorActual++;
        if (contadorVisitantes >= 500) {
            estado = EstadoAtraccion.MANTENIMIENTO;
            motivoCierre = "Mantenimiento preventivo: límite de 500 visitantes alcanzado";
        }
    }

    // Operador registra revisión técnica → resetea y vuelve a ACTIVA
    public void registrarRevisionTecnica() {
        contadorVisitantes = 0;
        contadorActual = 0;
        motivoCierre = "";
        estado = EstadoAtraccion.ACTIVA;
    }

    // Administrador activa alerta climática
    public void cerrarPorClima(String motivo) {
        estado = EstadoAtraccion.CERRADA;
        motivoCierre = motivo;
    }

    public boolean esAfectadaPorClima() {
        return tipo.equalsIgnoreCase("acuática") || tipo.equalsIgnoreCase("mecánica de altura");
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getCapacidad() { return capacidad; }
    public double getAlturaMin() { return alturaMin; }
    public int getEdadMin() { return edadMin; }
    public double getCostoExtra() { return costoExtra; }
    public EstadoAtraccion getEstado() { return estado; }
    public int getContadorVisitantes() { return contadorVisitantes; }
    public int getContadorActual() { return contadorActual; }
    public double getTiempoEspera() { return tiempoEspera; }
    public String getMotivoCierre() { return motivoCierre; }

    // Setters
    public void setEstado(EstadoAtraccion estado) { this.estado = estado; }
    public void setTiempoEspera(double tiempoEspera) { this.tiempoEspera = tiempoEspera; }
    public void setMotivoCierre(String motivoCierre) { this.motivoCierre = motivoCierre; }
    public void resetContadorActual() { this.contadorActual = 0; }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " (" + tipo + ") - " + estado;
    }
}