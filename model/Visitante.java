package model;

import model.atraccion.Atraccion;
import model.estructuras.ListaEnlazada;
import model.estructuras.SetSimple;
import model.ticket.Ticket;

public class Visitante extends Persona {

    private int edad;
    private double estatura;
    private double saldo;
    private Ticket ticket;
    private ListaEnlazada<Atraccion> historial;
    private SetSimple<Atraccion> favoritos;

    public Visitante(String nombre, String documento, int edad, double estatura) {
        super(nombre, documento);
        this.edad = edad;
        this.estatura = estatura;
        this.saldo = 0;
        this.historial = new ListaEnlazada<>();
        this.favoritos = new SetSimple<>();
    }

    public void recargarSaldo(double valor) {
        if (valor <= 0) throw new IllegalArgumentException("El valor debe ser positivo");
        this.saldo += valor;
    }

    public boolean descontarSaldo(double valor) {
        if (valor > saldo) return false;
        this.saldo -= valor;
        return true;
    }

    public void agregarAlHistorial(Atraccion atraccion) {
        historial.agregar(atraccion);
    }

    public void agregarFavorito(Atraccion atraccion) {
        favoritos.agregar(atraccion);
    }

    public void eliminarFavorito(Atraccion atraccion) {
        favoritos.eliminar(atraccion);
    }

    // Getters
    public int getEdad() { return edad; }
    public double getEstatura() { return estatura; }
    public double getSaldo() { return saldo; }
    public Ticket getTicket() { return ticket; }
    public ListaEnlazada<Atraccion> getHistorial() { return historial; }
    public SetSimple<Atraccion> getFavoritos() { return favoritos; }

    // Setters
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    @Override
    public String toString() {
        return nombre + " (Doc: " + documento + ") | Edad: " + edad
                + " | Estatura: " + estatura + "m | Saldo: $" + saldo;
    }
}