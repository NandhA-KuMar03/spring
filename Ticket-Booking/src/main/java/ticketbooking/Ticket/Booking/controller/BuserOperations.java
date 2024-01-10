package ticketbooking.Ticket.Booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Show;

import java.util.List;

public interface BuserOperations {

    @GetMapping("/buser/shows")
    ResponseEntity<List<Show>> getShows();

    @PatchMapping("/buser/deactivate/shows")
    ResponseEntity<Show> deactivateShow(@RequestParam int showId);

    @GetMapping("/buser/cancellations")
    ResponseEntity<List<Cancellation>> getCancellationRequests();

    @PatchMapping("/buser/cancellations")
    ResponseEntity<Cancellation> cancelRequest(@RequestParam int cancellationId);

    @GetMapping("/buser/bookings")
    ResponseEntity<List<Booking>> getBookingsByUser(@RequestParam int userId);

    @PatchMapping("/buser/activate/shows")
    ResponseEntity<Show> activateShow(@RequestParam int showId);
}
