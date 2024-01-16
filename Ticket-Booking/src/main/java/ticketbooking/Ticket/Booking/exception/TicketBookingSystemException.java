package ticketbooking.Ticket.Booking.exception;

public class TicketBookingSystemException extends RuntimeException{
    public TicketBookingSystemException(String message) {
        super(message);
    }

    public TicketBookingSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketBookingSystemException(Throwable cause) {
        super(cause);
    }
}
