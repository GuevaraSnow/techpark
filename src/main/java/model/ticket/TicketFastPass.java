// TicketFastPass.java
package model.ticket;

public class TicketFastPass extends Ticket {

    public TicketFastPass(double precio) {
        super(TipoTicket.FASTPASS, precio);
    }

    @Override
    public int getPrioridad() { return 1; }
}