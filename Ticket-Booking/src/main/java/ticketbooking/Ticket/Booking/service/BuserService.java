package ticketbooking.Ticket.Booking.service;

import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Show;

import java.util.List;

public interface BuserService {

    public List<Show> getShows();

    public Show cancelShow(int showId);

    public List<Cancellation> getCancellationRequests();

    public Cancellation cancelRequest(int cancellationId);

    public List<Booking> getBookingsByUser(int userId);

    public Show activateShow(int showId);
}
