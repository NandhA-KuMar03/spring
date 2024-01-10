package ticketbooking.Ticket.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.service.BuserService;

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
}
