package ticketbooking.Ticket.Booking.serviceimpl;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.exception.TicketBookingSystemException;
import ticketbooking.Ticket.Booking.repository.BookingRepository;
import ticketbooking.Ticket.Booking.repository.CancellationRepository;
import ticketbooking.Ticket.Booking.repository.MovieRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.request.ShowRequest;
import ticketbooking.Ticket.Booking.service.BuserService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

import static ticketbooking.Ticket.Booking.constants.ErrorConstants.BOOKING_ALREADY_CANCELLED;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_CANCEL_REQUEST;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_MOVIE;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SHOW;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_USER;

@Service
public class BuserServiceImpl implements BuserService {

    private ShowRepository showRepository;
    private CancellationRepository cancellationRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private ScreenRepository screenRepository;
    private MovieRepository movieRepository;

    @Autowired
    public BuserServiceImpl(ShowRepository showRepository, CancellationRepository cancellationRepository, UserRepository userRepository, BookingRepository bookingRepository, ScreenRepository screenRepository, MovieRepository movieRepository) {
        this.showRepository = showRepository;
        this.cancellationRepository = cancellationRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.screenRepository = screenRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Show> getShows() {
        return showRepository.findAll();
    }

    @Override
    public Show cancelShow(int showId) {
        Optional<Show> show = showRepository.findById(showId);
        if (! show.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SHOW);
        show.get().setActive(false);
        showRepository.save(show.get());
        return show.get();
    }

    @Override
    public List<Cancellation> getCancellationRequests() {
        List<Cancellation> cancellations = cancellationRepository.findAllByIsCancelledFalse();
        return cancellations;
    }

    @Override
    public Cancellation cancelRequest(int cancellationId) {
        Optional<Cancellation> cancellation = cancellationRepository.findById(cancellationId);
        if(! cancellation.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_CANCEL_REQUEST);
        if( cancellation.get().isCancelled())
            throw new TicketBookingSystemException(BOOKING_ALREADY_CANCELLED);
        cancellation.get().setCancelled(true);
        cancellationRepository.save(cancellation.get());
        Optional<Booking> booking = bookingRepository.findById(cancellation.get().getBooking().getBookingId());
        int numberOfSeats = booking.get().getCountOfTickets();
        Optional<Show> show = showRepository.findById(booking.get().getShow().getShowId());
        Optional<Screen> screen = screenRepository.findById(show.get().getScreen().getScreenId());
        screen.get().setSeatsRemaining(screen.get().getSeatsRemaining() + numberOfSeats);
        screenRepository.save(screen.get());
        return cancellation.get();
    }

    @Override
    public List<Booking> getBookingsByUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if(! user.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_USER);
        List<Booking> bookings = bookingRepository.findAllByBookedByUserId(userId);
        return bookings;
    }

    @Override
    public Show activateShow(int showId) {
        Optional<Show> show = showRepository.findById(showId);
        if (! show.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SHOW);
        show.get().setActive(true);
        showRepository.save(show.get());
        return show.get();
    }

    @Override
    public Show createShow(ShowRequest show) {
        Optional<Screen> screen = screenRepository.findById(show.getScreenId());
        if (! screen.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SCREEN);
        Optional<Movie> movie = movieRepository.findById(show.getMovieId());
        if (! movie.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_MOVIE);
        Show saveShow = new Show();
        saveShow.setActive(true);
        saveShow.setShowName(show.getShowName());
        saveShow.setScreen(screen.get());
        saveShow.setMovie(movie.get());
        return showRepository.save(saveShow);
    }

    @Override
    public Movie createMovie(Movie movie, MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(content.getBytes());
        gzip.close();
        Movie tempMovie = new Movie();
        tempMovie.setMovieName(movie.getMovieName());
        tempMovie.setDescription(movie.getDescription());
        tempMovie.setPoster("Poster");
        return movieRepository.save(tempMovie);
    }
}
