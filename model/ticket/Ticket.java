// Ticket.java
package model.ticket;

public abstract class Ticket {

    protected TipoTicket tipo;
    protected double precio;
    protected boolean activo;

    public Ticket(TipoTicket tipo, double precio) {
        this.tipo = tipo;
        this.precio = precio;
        this.activo = true;
    }

    // Prioridad para la cola: 1 = FastPass, 2 = General/Familiar
    public abstract int getPrioridad();

    public TipoTicket getTipo() { return tipo; }
    public double getPrecio() { return precio; }
    public boolean isActivo() { return activo; }
    public void desactivar() { this.activo = false; }

    @Override
    public String toString() {
        return tipo + " | $" + precio + " | " + (activo ? "Activo" : "Inactivo");
    }
}