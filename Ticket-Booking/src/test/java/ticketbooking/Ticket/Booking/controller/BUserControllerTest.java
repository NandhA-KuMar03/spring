package ticketbooking.Ticket.Booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.ShowRequest;
import ticketbooking.Ticket.Booking.service.BuserService;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BUserControllerTest {

    @InjectMocks
    private BuserController buserController;

    @Mock
    private BuserService buserService;

    @Test
    public void getShows(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show2 = new Show(1,"MS", true, screen, movie);
        Show show1 = new Show(2, "es", true, screen, movie);
        when(buserService.getShows()).thenReturn(List.of(show1, show2));
        ResponseEntity<List<Show>> response = buserController.getShows();
        assertEquals(response.getBody().get(0), show1);
    }

    @Test
    public void deactivateShow(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show2 = new Show(1,"MS", false, screen, movie);
        when(buserService.cancelShow(1)).thenReturn(show2);
        ResponseEntity<Show> response = buserController.deactivateShow(1);
        assertEquals(response.getBody().getShowId(), show2.getShowId());
    }

    @Test
    public void getCancellationReqs(){
        List<Cancellation> cancellations = new ArrayList<>();
        Cancellation cancellation1 = new Cancellation(); cancellation1.setCancellationId(1);
        Cancellation cancellation2 = new Cancellation(); cancellation2.setCancellationId(2);
        cancellations.add(cancellation1);
        cancellations.add(cancellation2);
        when(buserService.getCancellationRequests()).thenReturn(cancellations);
        ResponseEntity<List<Cancellation>> response = buserController.getCancellationRequests();
        assertEquals(response.getBody().get(0).getCancellationId(), cancellation1.getCancellationId());
    }

    @Test
    public void cancelReqs(){
        Cancellation cancellation1 = new Cancellation(); cancellation1.setCancellationId(1);
        cancellation1.setCancelled(true);
        when(buserService.cancelRequest(1)).thenReturn(cancellation1);
        ResponseEntity<Cancellation> response = buserController.cancelRequest(1);
        assertEquals(response.getBody().isCancelled(), true);
    }

    @Test
    public void getBookingsByUser(){
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
        when(buserService.getBookingsByUser(1)).thenReturn(List.of(booking1, booking2));
        ResponseEntity<List<Booking>> response = buserController.getBookingsByUser(1);
        assertEquals(response.getBody().get(0), booking1);
    }

    @Test
    public void activateShow(){
        Show  show1 = new Show();
        show1.setShowId(1); show1.setActive(true);
        when(buserService.activateShow(1)).thenReturn(show1);
        ResponseEntity<Show> response = buserController.activateShow(1);
        assertEquals(response.getBody().isActive(), true);
    }

    @Test
    public void createShow(){
        ShowRequest request = new ShowRequest("Show1", 30,10);
        Show show1 = new Show();
        show1.setShowId(1); show1.setActive(true);
        when(buserService.createShow(request)).thenReturn(show1);
        ResponseEntity<Show> response = buserController.createShow(request);
        assertEquals(response.getBody().getShowId(), 1);
    }

    @Test
    public void createMovie() throws IOException {
        Movie movie = new Movie(1, "96", "sd", "movie");
        MultipartFile file = new MockMultipartFile("name", new byte[3]);
        when(buserService.createMovie(movie,file)).thenReturn(movie);
        ResponseEntity<Movie> movieResponseEntity = buserController.createMovie(movie, file);
        assertEquals(movieResponseEntity.getBody().getMovieId(), 1);
    }


}
