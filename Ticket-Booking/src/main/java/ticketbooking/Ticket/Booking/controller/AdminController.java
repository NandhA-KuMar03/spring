package ticketbooking.Ticket.Booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.ScreenRequestObject;
import ticketbooking.Ticket.Booking.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController implements AdminOperations{

    @Autowired
    private AdminService adminService;

    @Override
    public ResponseEntity<List<Hall>> getHalls() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getHalls());
    }

    @Override
    public ResponseEntity<Hall> createHall(@RequestBody Hall hall) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createHall(hall));
    }

    @Override
    public ResponseEntity<Hall> deactivateHall(int hallId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.deactivateHall(hallId));
    }

    @Override
    public ResponseEntity<List<Screen>> getScreens() {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getScreens());
    }

    @Override
    public ResponseEntity<Screen> createScreen(ScreenRequestObject screen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createScreen(screen));
    }

    @Override
    public ResponseEntity<Screen> deactivateScreen(int screenId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.deactivateScreen(screenId));
    }

    @Override
    public ResponseEntity<Screen> activateScreen(int screenId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.activateScreen(screenId));
    }

    @Override
    public ResponseEntity<Hall> activateHall(int hallId) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.activateHall(hallId));
    }

    @Override
    public ResponseEntity<User> promoteUser(int userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.promoteUser(userId));
    }
}
