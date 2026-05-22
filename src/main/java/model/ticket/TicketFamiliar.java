// TicketFamiliar.java
package model.ticket;

public class TicketFamiliar extends Ticket {

    private int integrantes;

    public TicketFamiliar(double precio, int integrantes) {
        super(TipoTicket.FAMILIAR, precio);
        this.integrantes = integrantes;
    }

    public int getIntegrantes() { return integrantes; }

    @Override
    public int getPrioridad() { return 2; }
}