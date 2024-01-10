package ticketbooking.Ticket.Booking.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.exception.TicketBookingSystemException;
import ticketbooking.Ticket.Booking.repository.BookingRepository;
import ticketbooking.Ticket.Booking.repository.CancellationRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.service.BuserService;

import java.util.List;
import java.util.Optional;

@Service
public class BuserServiceImpl implements BuserService {

    private ShowRepository showRepository;
    private CancellationRepository cancellationRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private ScreenRepository screenRepository;

    @Autowired
    public BuserServiceImpl(ShowRepository showRepository, CancellationRepository cancellationRepository, UserRepository userRepository, BookingRepository bookingRepository, ScreenRepository screenRepository) {
        this.showRepository = showRepository;
        this.cancellationRepository = cancellationRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.screenRepository = screenRepository;
    }

    @Override
    public List<Show> getShows() {
        return showRepository.findAll();
    }

    @Override
    public Show cancelShow(int showId) {
        Optional<Show> show = showRepository.findById(showId);
        if (! show.isPresent())
            throw new TicketBookingSystemException("No such show found");
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
            throw new TicketBookingSystemException("No such cancel request found");
        if( cancellation.get().isCancelled())
            throw new TicketBookingSystemException("This booking is already cancelled");
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
            throw new TicketBookingSystemException("No such user");
        List<Booking> bookings = bookingRepository.findAllByBookedByUserId(userId);
        return bookings;
    }

    @Override
    public Show activateShow(int showId) {
        Optional<Show> show = showRepository.findById(showId);
        if (! show.isPresent())
            throw new TicketBookingSystemException("No such show found");
        show.get().setActive(true);
        showRepository.save(show.get());
        return show.get();
    }
}
