package ticketbooking.Ticket.Booking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

public interface UserOperations {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> createUser(@RequestBody User user);

    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request);

    @GetMapping("/bookings")
    ResponseEntity<List<Booking>> getBookings();

    @PutMapping("/cancellation")
    ResponseEntity<Cancellation> requestCancellation(@RequestParam int bookingId);

    @PostMapping("/buy-tickets")
    ResponseEntity<Booking> buyTickets(@RequestBody BuyTicketsRequest buyTicketsRequest);

    @GetMapping("/locations")
    ResponseEntity<List<LocationResponse>> listOfLocations();

    @GetMapping("/locations/{locationName}")
    ResponseEntity<LocationRequest> getLocation(@PathVariable("locationName") String locationName);

    @GetMapping("/locations/{locationName}/halls")
    ResponseEntity<List<Hall>> getHalls(@PathVariable("locationName") String locationName);

    @GetMapping("/locations/{locationName}/halls/{hallId}")
    ResponseEntity<Hall> getHall(@PathVariable("locationName") String locationName, @PathVariable("hallId") int hallId);

    @GetMapping("/locations/{locationName}/halls/{hallId}/screens")
    ResponseEntity<List<Screen>> getScreens(@PathVariable("locationName") String locationName, @PathVariable("hallId") int hallId);

    @GetMapping("/locations/{locationName}/halls/{hallId}/screens/{screenId}")
    ResponseEntity<Screen> getScreen(@PathVariable("locationName") String locationName, @PathVariable("hallId") int hallId, @PathVariable("screenId") int screenId);

    @GetMapping("/locations/{locationName}/halls/{hallId}/screens/{screenId}/shows")
    ResponseEntity<List<Show>> getShows(@PathVariable("locationName") String locationName, @PathVariable("hallId") int hallId, @PathVariable("screenId") int screenId);

    @GetMapping("/locations/{locationName}/halls/{hallId}/screens/{screenId}/shows/{showId}")
    ResponseEntity<Show> getShow(@PathVariable("locationName") String locationName, @PathVariable("hallId") int hallId, @PathVariable("screenId") int screenId, @PathVariable("showId") int showId);

    @GetMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    @GetMapping("/movies")
    ResponseEntity<List<Movie>> getAllMovies();

    @GetMapping("/movies/{movieId}")
    ResponseEntity<List<Show>> getShowsOfMovie(@PathVariable("movieId") int movieId);
}
