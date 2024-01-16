package ticketbooking.Ticket.Booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.request.ShowRequest;

import java.io.IOException;
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

    @PostMapping("/buser/shows")
    ResponseEntity<Show> createShow(@RequestBody ShowRequest show);

    @PostMapping(value = "/buser/movies", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<Movie> createMovie(@RequestPart("movie") Movie movie, @RequestPart("file") MultipartFile file) throws IOException;
}
