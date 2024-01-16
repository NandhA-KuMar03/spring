package ticketbooking.Ticket.Booking.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.ScreenRequestObject;
import ticketbooking.Ticket.Booking.service.AdminService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Test
    public void getHalls() throws Exception {
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        when(adminService.getHalls()).thenReturn(halls);
        ResponseEntity<List<Hall>> response = adminController.getHalls();
        assertEquals(response.getBody(), halls);
    }

    @Test
    public void createHall(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        when(adminService.createHall(hall1)).thenReturn(hall1);
        ResponseEntity<Hall> response = adminController.createHall(hall1);
        assertEquals(response.getBody(), hall1);
    }

    @Test
    public void deactivateHall(){
        Hall hall1 = new Hall(1,"hall1", false, "Chennai");
        when(adminService.deactivateHall(1)).thenReturn(hall1);
        ResponseEntity<Hall> response = adminController.deactivateHall(1);
        assertEquals(response.getBody(), hall1);
    }

    @Test
    public void getScreens(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(adminService.getScreens()).thenReturn(List.of(screen1, screen2));
        ResponseEntity<List<Screen>> response = adminController.getScreens();
        assertEquals(response.getBody().get(0), screen1);
    }

    @Test
    public void createScreen(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        ScreenRequestObject requestObject = new ScreenRequestObject(1,"Vidya", 30, true);
        when(adminService.createScreen(requestObject)).thenReturn(screen1);
        ResponseEntity<Screen> response = adminController.createScreen(requestObject);
        assertEquals(response.getBody(), screen1);
    }

    @Test
    public void deactivateScreen(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, false, hall1);
        when(adminService.deactivateScreen(1)).thenReturn(screen1);
        ResponseEntity<Screen> response = adminController.deactivateScreen(1);
        assertEquals(response.getBody(), screen1);
    }

    @Test
    public void activateScreen(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        when(adminService.activateScreen(1)).thenReturn(screen1);
        ResponseEntity<Screen> response = adminController.activateScreen(1);
        assertEquals(response.getBody(), screen1);
    }

    @Test
    public void activateHall(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        when(adminService.activateHall(1)).thenReturn(hall1);
        ResponseEntity<Hall> response = adminController.activateHall(1);
        assertEquals(response.getBody(), hall1);
    }

    @Test
    public void promoteUser(){
        List<Role> roles = new ArrayList<>();
        List<Role> roles1 = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        Role role2 = new Role(2,"BUSER");
        roles.add(role1);
        roles1.add(role1);
        roles1.add(role2);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        User responseUser = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles1);
        when(adminService.promoteUser(1)).thenReturn(responseUser);
        ResponseEntity<User> response = adminController.promoteUser(1);
        assertEquals(response.getBody(), responseUser);
    }
}
