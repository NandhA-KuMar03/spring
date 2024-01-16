package ticketbooking.Ticket.Booking.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.BuyTicketsRequest;
import ticketbooking.Ticket.Booking.request.LocationRequest;
import ticketbooking.Ticket.Booking.request.LoginRequest;
import ticketbooking.Ticket.Booking.response.AuthenticationResponse;
import ticketbooking.Ticket.Booking.response.LocationResponse;

import java.util.List;

public interface UserService {

    public AuthenticationResponse createUser(User user);

    public List<Booking> getBookings();

    public Cancellation requestCancellation(int bookingId);

    public AuthenticationResponse login(LoginRequest request);

    public Booking bookTickets(BuyTicketsRequest buyTicketsRequest);

    public List<LocationResponse> getLocations();

    public LocationRequest getLocation(String locationName);

    public List<Hall> getHalls(String locationName);

    public Hall getHall(String locationName, int hallId);

    public List<Screen> getScreens(String locationName, int hallId);

    public Screen getScreen(String locationName, int hallId, int screenId);

    public List<Show> getShows(String locationName, int hallId, int screenId);

    Show getShow(String locationName, int hallId, int screenId, int showId);

    public String performLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    public  List<Movie> getMovies();

    List<Show> getShowsOfMovie(int movieId);
}
