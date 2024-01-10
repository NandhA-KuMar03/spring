package ticketbooking.Ticket.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.BuyTicketsRequest;
import ticketbooking.Ticket.Booking.request.LocationRequest;
import ticketbooking.Ticket.Booking.request.LoginRequest;
import ticketbooking.Ticket.Booking.response.AuthenticationResponse;
import ticketbooking.Ticket.Booking.response.LocationResponse;
import ticketbooking.Ticket.Booking.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController implements UserOperations{

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> createUser(User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> login(LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @Override
    public ResponseEntity<List<Booking>> getBookings() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getBookings());
    }

    @Override
    public ResponseEntity<Cancellation> requestCancellation(int bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.requestCancellation(bookingId));
    }

    @Override
    public ResponseEntity<Booking> buyTickets(BuyTicketsRequest buyTicketsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.bookTickets(buyTicketsRequest));
    }

    @Override
    public ResponseEntity<List<LocationResponse>> listOfLocations() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getLocations());
    }

    @Override
    public ResponseEntity<LocationRequest> getLocation(String locationName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getLocation(locationName));
    }

    @Override
    public ResponseEntity<List<Hall>> getHalls(String locationName) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getHalls(locationName));
    }

    @Override
    public ResponseEntity<Hall> getHall(String locationName, int hallId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getHall(locationName, hallId));
    }

    @Override
    public ResponseEntity<List<Screen>> getScreens(String locationName, int hallId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getScreens(locationName, hallId));
    }

    @Override
    public ResponseEntity<Screen> getScreen(String locationName, int hallId, int screenId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getScreen(locationName, hallId, screenId));
    }

    @Override
    public ResponseEntity<List<Show>> getShows(String locationName, int hallId, int screenId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getShows(locationName, hallId, screenId));
    }

    @Override
    public ResponseEntity<Show> getShow(String locationName, int hallId, int screenId, int showId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getShow(locationName, hallId, screenId, showId));
    }
}
