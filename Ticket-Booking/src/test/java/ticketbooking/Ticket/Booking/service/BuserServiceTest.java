package ticketbooking.Ticket.Booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.repository.BookingRepository;
import ticketbooking.Ticket.Booking.repository.CancellationRepository;

import ticketbooking.Ticket.Booking.repository.MovieRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.request.ShowRequest;
import ticketbooking.Ticket.Booking.serviceimpl.BuserServiceImpl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.BOOKING_ALREADY_CANCELLED;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_CANCEL_REQUEST;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_MOVIE;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SHOW;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_USER;

@ExtendWith(MockitoExtension.class)
public class BuserServiceTest {

    @InjectMocks
    BuserServiceImpl buserService;

    @Mock
    private ScreenRepository screenRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private CancellationRepository cancellationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MovieRepository movieRepository;
    @Test
    void getShows(){
        List<Show> shows = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        shows.add(show);
        when(showRepository.findAll()).thenReturn(shows);
        List<Show> response = buserService.getShows();
        assertEquals(response.get(0), shows.get(0));
    }

    @Test
    void cancelShow(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        Show response = buserService.cancelShow(1);
        assertEquals(response.isActive(), false);
    }

    @Test
    void cancelShowException(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        try {
            Show response = buserService.cancelShow(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SHOW);
        }
    }

    @Test
    void getCancellationRequests(){
        List<Cancellation> cancellations = new ArrayList<>();
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
        Cancellation cancellation1 = new Cancellation(1,false,booking1);
        Booking booking2 = new Booking(2,2,true,100,movie,user,show);
        Cancellation cancellation2 = new Cancellation(1,false,booking2);
        cancellations.add(cancellation1);
        cancellations.add(cancellation2);
        when(cancellationRepository.findAllByIsCancelledFalse()).thenReturn(cancellations);
        List<Cancellation> response = buserService.getCancellationRequests();
        assertEquals(response.get(0).getCancellationId(), cancellation1.getCancellationId());
    }

    @Test
    void cancelRequest(){
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
        Cancellation cancellation1 = new Cancellation(1,false,booking1);
        when(cancellationRepository.findById(1)).thenReturn(Optional.of(cancellation1));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        Cancellation response = buserService.cancelRequest(1);
        assertEquals(response.isCancelled(), cancellation1.isCancelled());
        assertEquals(response.getBooking().getShow().getScreen().getSeatsRemaining(), 42);
    }

    @Test
    void cancelRequestException(){
        try {
            buserService.cancelRequest(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_CANCEL_REQUEST);
        }
    }

    @Test
    void cancelRequestException11(){
        Cancellation cancellation = new Cancellation();
        cancellation.setCancelled(true);
        cancellation.setCancellationId(1);
        when(cancellationRepository.findById(1)).thenReturn(Optional.of(cancellation));
        try {
            buserService.cancelRequest(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), BOOKING_ALREADY_CANCELLED);
        }
    }

    @Test
    void getBookingsByUser(){
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
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookedByUserId(1)).thenReturn(List.of(booking1));
        List<Booking> response = buserService.getBookingsByUser(1);
        assertEquals(response.get(0).getBookingId(), booking1.getBookingId());
    }

    @Test
    void getBookingsByUserException(){
        try {
            buserService.getBookingsByUser(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_USER);
        }
    }

    @Test
    void activateShow(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", false, screen, movie);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        Show response = buserService.activateShow(1);
        assertEquals(response.isActive(), true);
    }

    @Test
    void activateShowException(){
        try {
            buserService.activateShow(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SHOW);
        }
    }

    @Test
    void createShow(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        ShowRequest request = new ShowRequest("MS", 1, 1);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(0,"MS", true, screen, movie);
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(showRepository.save(show)).thenReturn(show);
        Show response = buserService.createShow(request);
        assertEquals(response.getShowId(), show.getShowId());
    }

    @Test
    void createShowException(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        ShowRequest request = new ShowRequest("MS", 1, 1);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(0,"MS", true, screen, movie);
        try {
            buserService.createShow(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SCREEN);
        }
    }

    @Test
    void createShowException2(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        ShowRequest request = new ShowRequest("MS", 1, 1);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(0,"MS", true, screen, movie);
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        try {
            buserService.createShow(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_MOVIE);
        }
    }

    @Test
    void createMovie() throws IOException {
        Movie movie = new Movie(0,"96", "Poster", "movie");
        MultipartFile file = new MockMultipartFile("name", new byte[3]);
        when(movieRepository.save(movie)).thenReturn(movie);
        Movie response = buserService.createMovie(movie, file);
        assertEquals(response.getMovieId(), movie.getMovieId());
    }
}
