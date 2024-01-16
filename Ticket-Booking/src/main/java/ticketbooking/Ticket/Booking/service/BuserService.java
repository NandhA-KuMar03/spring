package ticketbooking.Ticket.Booking.service;

import org.springframework.web.multipart.MultipartFile;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.request.ShowRequest;

import java.io.IOException;
import java.util.List;

public interface BuserService {

    public List<Show> getShows();

    public Show cancelShow(int showId);

    public List<Cancellation> getCancellationRequests();

    public Cancellation cancelRequest(int cancellationId);

    public List<Booking> getBookingsByUser(int userId);

    public Show activateShow(int showId);

    public Show createShow(ShowRequest show);

    public Movie createMovie(Movie movie, MultipartFile file) throws IOException;
}
