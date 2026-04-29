package model;

import model.ticket.Ticket;

public class Visitante extends Persona {

    private int edad;
    private double estatura;
    private double saldo;
    private Ticket ticket;

    public Visitante(String nombre, String documento, int edad, double estatura) {
        super(nombre, documento);
        this.edad = edad;
        this.estatura = estatura;
        this.saldo = 0;
    }

    public int getEdad() {
        return edad;
    }

    public double getEstatura() {
        return estatura;
    }

    public double getSaldo() {
        return saldo;
    }

    public void recargarSaldo(double valor) {
        this.saldo += valor;
    }
}