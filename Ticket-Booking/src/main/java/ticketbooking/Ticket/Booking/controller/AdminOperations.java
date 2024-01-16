package ticketbooking.Ticket.Booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.ScreenRequestObject;

import java.util.List;

public interface AdminOperations {

    @GetMapping("/admin/halls")
    ResponseEntity<List<Hall>> getHalls();

    @PostMapping("/admin/halls")
    ResponseEntity<Hall> createHall(@RequestBody Hall hall);

    @PatchMapping("/admin/deactivate/halls")
    ResponseEntity<Hall> deactivateHall(@RequestParam int hallId);

    @GetMapping("/admin/screens")
    ResponseEntity<List<Screen>> getScreens();

    @PostMapping("/admin/screens")
    ResponseEntity<Screen> createScreen(@RequestBody ScreenRequestObject screen);

    @PatchMapping("/admin/deactivate/screens")
    ResponseEntity<Screen> deactivateScreen(@RequestParam int screenId);

    @PatchMapping("/admin/activate/screens")
    ResponseEntity<Screen> activateScreen(@RequestParam int screenId);

    @PatchMapping("/admin/activate/halls")
    ResponseEntity<Hall> activateHall(@RequestParam int hallId);

    @PostMapping("/admin/promote")
    ResponseEntity<User> promoteUser(@RequestParam int userId);
}
