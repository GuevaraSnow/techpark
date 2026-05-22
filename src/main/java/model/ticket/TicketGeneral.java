// TicketGeneral.java
package model.ticket;

public class TicketGeneral extends Ticket {

    public TicketGeneral(double precio) {
        super(TipoTicket.GENERAL, precio);
    }

    @Override
    public int getPrioridad() { return 2; }
}