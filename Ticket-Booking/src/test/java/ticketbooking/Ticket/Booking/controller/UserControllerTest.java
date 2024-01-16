package ticketbooking.Ticket.Booking.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.LocationRequest;
import ticketbooking.Ticket.Booking.request.LoginRequest;
import ticketbooking.Ticket.Booking.response.AuthenticationResponse;
import ticketbooking.Ticket.Booking.response.LocationResponse;
import ticketbooking.Ticket.Booking.service.UserService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


    @Test
    public void createUser(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        AuthenticationResponse response = new AuthenticationResponse();
        when(userService.createUser(user)).thenReturn(response);
        ResponseEntity<AuthenticationResponse> responseResponseEntity = userController.createUser(user);
        assertEquals(responseResponseEntity.getStatusCode(), CREATED);
    }

    @Test
    public void login(){
        LoginRequest request = new LoginRequest("mail.com", "pass");
        AuthenticationResponse response = new AuthenticationResponse();
        when(userService.login(request)).thenReturn(response);
        ResponseEntity<AuthenticationResponse> responseEntity = userController.login(request);
        assertEquals(responseEntity.getStatusCode(), OK);
    }

    @Test
    public void getBookings(){
        List<Role> roles = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        Booking booking1 = new Booking(1,2,true,100,movie,user,show);
        Booking booking2 = new Booking(2,2,true,100,movie,user,show);
        when(userService.getBookings()).thenReturn(List.of(booking1, booking2));
        ResponseEntity<List<Booking>> response = userController.getBookings();
        assertEquals(response.getBody(), List.of(booking1,booking2));
    }

    @Test
    public void requestCancellation(){
        List<Role> roles = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        Booking booking1 = new Booking(1,2,true,100,movie,user,show);
        Cancellation cancellation = new Cancellation(0,false, booking1);
        when(userService.requestCancellation(1)).thenReturn(cancellation);
        ResponseEntity<Cancellation> response = userController.requestCancellation(1);
        assertEquals(response.getBody(), cancellation);
    }

    @Test
    public void getLocations(){
        LocationRequest request = new LocationRequest("North Little Rock", "Oregon", false, "Fidel Flat", "Berkshire", false, "Implemented real-time secured line", "giving-recovery.net", "1");
        LocationResponse response = new LocationResponse("North Little Rock", true);
        when(userService.getLocations()).thenReturn(List.of(response));
        ResponseEntity<List<LocationResponse>> responseEntity = userController.listOfLocations();
        assertEquals(responseEntity.getBody().get(0).getLocationName(), request.getName());
    }

    @Test
    public void getLocation(){
        LocationRequest request = new LocationRequest("North Little Rock", "Oregon", false, "Fidel Flat", "Berkshire", false, "Implemented real-time secured line", "giving-recovery.net", "1");
        when(userService.getLocation("North Little Rock")).thenReturn(request);
        ResponseEntity<LocationRequest> responseEntity = userController.getLocation("North Little Rock");
        assertEquals(responseEntity.getBody(), request);
    }

    @Test
    public void getHalls(){
        Hall hall1 = new Hall(); hall1.setHallId(1);
        Hall hall2 = new Hall(); hall2.setHallId(2);
        when(userService.getHalls("Chennai")).thenReturn(List.of(hall2, hall1));
        ResponseEntity<List<Hall>> response = userController.getHalls("Chennai");
        assertEquals(response.getBody().get(0).getHallId(), hall2.getHallId());
    }

    @Test
    public void getHall(){
        Hall hall1 = new Hall(); hall1.setHallId(1);
        Hall hall2 = new Hall(); hall2.setHallId(2);
        when(userService.getHall("Chennai",1)).thenReturn(hall1);
        ResponseEntity<Hall> response = userController.getHall("Chennai", 1);
        assertEquals(response.getBody().getHallId(), hall1.getHallId());
    }

    @Test
    public void getScreens(){
        Screen screen1 = new Screen(); screen1.setScreenId(1);
        Screen screen2 = new Screen(); screen2.setScreenId(2);
        when(userService.getScreens("Chennai",1 )).thenReturn(List.of(screen2, screen1));
        ResponseEntity<List<Screen>> response = userController.getScreens("Chennai", 1);
        assertEquals(response.getBody().get(0), screen2);
    }

    @Test
    public void getScreen(){
        Screen screen1 = new Screen(); screen1.setScreenId(1);
        when(userService.getScreen("Chennai",1,1 )).thenReturn(screen1);
        ResponseEntity<Screen> response = userController.getScreen("Chennai",1,1);
        assertEquals(response.getBody().getScreenId(), screen1.getScreenId());
    }

    @Test
    public void getShows(){
        Show show1 = new Show(); show1.setShowId(1);
        Show show2 = new Show(); show2.setShowId(2);
        when(userService.getShows("Chennai",1,1)).thenReturn(List.of(show2, show1));
        ResponseEntity<List<Show>> response = userController.getShows("Chennai",1,1);
        assertEquals(response.getBody().get(0), show2);
    }

    @Test
    public void getShow(){
        Show show1 = new Show(); show1.setShowId(1);
        when(userService.getShow("Chennai",1,1,1)).thenReturn(show1);
        ResponseEntity<Show> response = userController.getShow("Chennai",1,1,1);
        assertEquals(response.getBody().getShowId(), show1.getShowId());
    }

    @Test
    public void getMovies(){
        Movie movie = new Movie(1, "96", "poster", "movie description");
        when(userService.getMovies()).thenReturn(List.of(movie));
        ResponseEntity<List<Movie>> response = userController.getAllMovies();
        assertEquals(response.getBody().get(0), movie);
    }

    @Test
    public void getShowsOfMovies(){
        Movie movie = new Movie(1, "96", "poster", "movie description");
        Show show1 = new Show(); show1.setShowId(1); show1.setMovie(movie);
        Show show2 = new Show(); show1.setShowId(2); show1.setMovie(movie);
        Show show3 = new Show(); show1.setShowId(3); show1.setMovie(movie);
        when(userService.getShowsOfMovie(1)).thenReturn(List.of(show3,show2,show1));
        ResponseEntity<List<Show>> response = userController.getShowsOfMovie(1);
        assertEquals(response.getBody().get(0), show3);
    }
}
