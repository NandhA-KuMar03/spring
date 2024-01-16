package ticketbooking.Ticket.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.request.ShowRequest;
import ticketbooking.Ticket.Booking.service.BuserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BuserController implements BuserOperations{

    private BuserService buserService;

    @Autowired
    public BuserController(BuserService buserService) {
        this.buserService = buserService;
    }

    @Override
    public ResponseEntity<List<Show>> getShows() {
        return ResponseEntity.status(HttpStatus.OK).body(buserService.getShows());
    }

    @Override
    public ResponseEntity<Show> deactivateShow(int showId) {
        return ResponseEntity.status(HttpStatus.OK).body(buserService.cancelShow(showId));
    }

    @Override
    public ResponseEntity<List<Cancellation>> getCancellationRequests() {
        return ResponseEntity.status(HttpStatus.OK).body(buserService.getCancellationRequests());
    }

    @Override
    public ResponseEntity<Cancellation> cancelRequest(int cancellationId) {
        return ResponseEntity.status(HttpStatus.OK).body(buserService.cancelRequest(cancellationId));
    }

    @Override
    public ResponseEntity<List<Booking>> getBookingsByUser(int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(buserService.getBookingsByUser(userId));
    }

    @Override
    public ResponseEntity<Show> activateShow(int showId) {
        return ResponseEntity.status(HttpStatus.OK).body(buserService.activateShow(showId));
    }

    @Override
    public ResponseEntity<Show> createShow(ShowRequest show) {
        return ResponseEntity.status(HttpStatus.CREATED).body(buserService.createShow(show));
    }

    @Override
    public ResponseEntity<Movie> createMovie(Movie movie, MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(buserService.createMovie(movie, file));
    }
}
