package ticketbooking.Ticket.Booking.serviceimpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ticketbooking.Ticket.Booking.entity.Booking;
import ticketbooking.Ticket.Booking.entity.Cancellation;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.exception.TicketBookingSystemException;
import ticketbooking.Ticket.Booking.repository.BookingRepository;
import ticketbooking.Ticket.Booking.repository.CancellationRepository;
import ticketbooking.Ticket.Booking.repository.HallRepository;
import ticketbooking.Ticket.Booking.repository.MovieRepository;
import ticketbooking.Ticket.Booking.repository.RoleRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.request.BuyTicketsRequest;
import ticketbooking.Ticket.Booking.request.LocationRequest;
import ticketbooking.Ticket.Booking.request.LoginRequest;
import ticketbooking.Ticket.Booking.response.AuthenticationResponse;
import ticketbooking.Ticket.Booking.response.LocationResponse;
import ticketbooking.Ticket.Booking.service.JwtService;
import ticketbooking.Ticket.Booking.service.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ticketbooking.Ticket.Booking.constants.CommonConstants.BUSER;
import static ticketbooking.Ticket.Booking.constants.CommonConstants.PRIME_URL;
import static ticketbooking.Ticket.Booking.constants.CommonConstants.URL;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.ABOVE_18_AGE;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.ALREADY_REQUESTED_CANCELLATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.GENDER_ERROR;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.INVALID_CREDS;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_ACCESS_IN_PRIME_LOCATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_ACCESS_TO_BOOK_FOR_OTHERS;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_BOOKING;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_BOOKING_NON_EXISTING_USER;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_HALLS_IN_LOCATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SCREENS;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SHOWS_IN_THIS_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_BOOKING_BY_YOU;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_HALL;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_LOCATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_MOVIE;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SHOW;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_TICKETS_LEFT;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.SHOW_DEACTIVATED;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private CancellationRepository cancellationRepository;
    private BookingRepository bookingRepository;
    private RoleRepository roleRepository;
    private ShowRepository showRepository;
    private ScreenRepository screenRepository;
    private HallRepository hallRepository;
    private MovieRepository movieRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CancellationRepository cancellationRepository, BookingRepository bookingRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, RoleRepository roleRepository, ShowRepository showRepository, ScreenRepository screenRepository, HallRepository hallRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.cancellationRepository = cancellationRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.showRepository = showRepository;
        this.screenRepository = screenRepository;
        this.hallRepository = hallRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public AuthenticationResponse createUser(User user) {
        String email = user.getEmail();
        Optional<User> user1 = userRepository.findUserByEmail(email);
        if(user1.isPresent())
            throw new TicketBookingSystemException(EMAIL_ALREADY_EXISTS);
        LocalDate dob = user.getDob().toLocalDate();
        LocalDate now = LocalDate.now();
        int years = Period.between(dob,now).getYears();
        if(years<18)
            throw new TicketBookingSystemException(ABOVE_18_AGE);
        if(! (user.getGender().equalsIgnoreCase("m") || user.getGender().equalsIgnoreCase("f") || user.getGender().equalsIgnoreCase("o")))
            throw new TicketBookingSystemException(GENDER_ERROR);
        User tempUser = new User();
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findById(1);
        roles.add(role);
        tempUser.setDob(user.getDob());
        tempUser.setEmail(user.getEmail());
        tempUser.setPassword(passwordEncoder.encode(user.getPassword()));
        tempUser.setFirstName(user.getFirstName());
        tempUser.setLastName(user.getLastName());
        tempUser.setGender(user.getGender());
        tempUser.setRoles(roles);
        tempUser.setLastActivity(System.currentTimeMillis());
        User responseUser = userRepository.save(tempUser);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .authToken(jwtToken)
                .build();
    }

    @Override
    public List<Booking> getBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        List<Booking> bookings = bookingRepository.findAllByBookedByUserId(user.get().getUserId());
        return bookings;
    }

    @Override
    public Cancellation requestCancellation(int bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        if(! booking.isPresent())
            throw new TicketBookingSystemException(NO_BOOKING);
        if( booking.get().getBookedBy().getUserId() != user.get().getUserId())
            throw new TicketBookingSystemException(NO_SUCH_BOOKING_BY_YOU);
        if(booking.get().isCancelRequested())
            throw new TicketBookingSystemException(ALREADY_REQUESTED_CANCELLATION);
        booking.get().setCancelRequested(true);
        Cancellation cancellation = new Cancellation();
        cancellation.setCancelled(false);
        cancellation.setBooking(booking.get());
        bookingRepository.save(booking.get());
        Cancellation responseCancellation = cancellationRepository.save(cancellation);
        return responseCancellation;
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(INVALID_CREDS));
        var jwtToken = jwtService.generateToken(user);
        user.setLastActivity(System.currentTimeMillis());
        userRepository.save(user);2
        return AuthenticationResponse.builder()
                .authToken(jwtToken)
                .build();
    }

    @Override
    public Booking bookTickets(BuyTicketsRequest buyTicketsRequest) {
        int showId = buyTicketsRequest.getShowId();
        int numberOfTickets = buyTicketsRequest.getCount();
        int userId = buyTicketsRequest.getUserId();
        Optional<Show> show = showRepository.findById(showId);
        if(! show.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SHOW);
        if(! show.get().isActive())
            throw new TicketBookingSystemException(SHOW_DEACTIVATED);
        Optional<Movie> movie = movieRepository.findById(show.get().getMovie().getMovieId());
        Optional<Screen> screen = screenRepository.findById(show.get().getScreen().getScreenId());
        Optional<Hall> hall = hallRepository.findById(screen.get().getHall().getHallId());
        String locationName = hall.get().getLocationName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findUserByEmail(userDetails.getUsername());
        String primeUrl = PRIME_URL;
        String url = URL;
        RestTemplate restTemplate = new RestTemplate();
        LocationRequest[] locations = restTemplate.getForObject(primeUrl, LocationRequest[].class);
        List<LocationRequest> list = Arrays.stream(locations)
                .collect(Collectors.toList());
        Optional<LocationRequest> locationRequest = list.stream()
                .filter(lis -> lis.getName().equalsIgnoreCase(locationName) && lis.getIsPrime() == true).findFirst();
        Booking bookingResponse = null;
        Booking booking = new Booking();
        boolean isAuthorized = false;
        System.out.println(authentication.getAuthorities());
        if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(BUSER)))
            isAuthorized = true;

        if(locationRequest.isPresent()){
            if(! isAuthorized)
                throw new TicketBookingSystemException(NO_ACCESS_IN_PRIME_LOCATION);
        }

        if(userId != 0 ){
            if(! isAuthorized)
                throw new TicketBookingSystemException(NO_ACCESS_TO_BOOK_FOR_OTHERS);
            Optional<User> user1 = userRepository.findById(userId);
            if (! user1.isPresent())
                throw new TicketBookingSystemException(NO_BOOKING_NON_EXISTING_USER);
            booking.setBookedFor(userId);
        }
        else{
            booking.setBookedFor(user.get().getUserId());
        }

        booking.setBookedBy(user.get());
        booking.setCancelRequested(false);
        if(screen.get().getSeatsRemaining() < numberOfTickets)
            throw new TicketBookingSystemException(NO_TICKETS_LEFT);
        booking.setCountOfTickets(numberOfTickets);
        booking.setMovie(movie.get());
        booking.setShow(show.get());
        bookingResponse = bookingRepository.save(booking);
        screen.get().setSeatsRemaining(screen.get().getSeatsRemaining() - numberOfTickets);
        screenRepository.save(screen.get());
        return bookingResponse;
    }

    @Override
    public List<LocationResponse> getLocations() {
        List<LocationResponse> list = new ArrayList<>();
        String primeUrl = PRIME_URL;
        String url = URL;
        RestTemplate restTemplate = new RestTemplate();
        LocationRequest[] locations = restTemplate.getForObject(primeUrl, LocationRequest[].class);
        List<LocationRequest> locationRequests = Arrays.stream(locations)
                .collect(Collectors.toList());
        locationRequests.stream().forEach(locationRequest -> {
                list.add(new LocationResponse(locationRequest.getName(), locationRequest.getIsPrime()));
                }
        );
        LocationRequest[] locationRequests1 = restTemplate.getForObject(url, LocationRequest[].class);
        List<LocationRequest> locationRequests2 = Arrays.stream(locationRequests1).collect(Collectors.toList());
        locationRequests2.stream().forEach(locationRequest -> {
                list.add(new LocationResponse(locationRequest.getName(), false));
                }
        );
        return list;
    }

    @Override
    public LocationRequest getLocation(String locationName) {
        Optional<LocationRequest> response = Optional.of(new LocationRequest());
        String primeUrl = PRIME_URL;
        String url = URL;
        RestTemplate restTemplate = new RestTemplate();
        LocationRequest[] locations = restTemplate.getForObject(primeUrl, LocationRequest[].class);
        List<LocationRequest> locationRequests = Arrays.stream(locations)
                .collect(Collectors.toList());
        response = locationRequests.stream().filter(locationRequest -> locationRequest.getName().equalsIgnoreCase(locationName)).findFirst();
        if (!response.isPresent()) {
            LocationRequest[] locationRequests1 = restTemplate.getForObject(url, LocationRequest[].class);
            List<LocationRequest> locationRequests2 = Arrays.stream(locationRequests1).collect(Collectors.toList());
            response = locationRequests2.stream().filter(locationRequest -> locationRequest.getName().equalsIgnoreCase(locationName)).findFirst();
        }
        if (! response.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_LOCATION);
        return response.get();
    }

    public List<Hall> returnHalls(String locationName){
        List<Hall> halls = hallRepository.findAllByLocationName(locationName);
        if (halls.isEmpty())
            throw new TicketBookingSystemException(NO_HALLS_IN_LOCATION);
        return halls;
    }

    @Override
    public List<Hall> getHalls(String locationName) {
        List<Hall> halls = returnHalls(locationName);
        return halls;
    }

    public Optional<Hall> returnHall(int hallId, String locationName){
        Optional<Hall> hall = hallRepository.findByHallIdAndLocationName(hallId, locationName);
        if (! hall.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_HALL);
        return hall;
    }

    @Override
    public Hall getHall(String locationName, int hallId) {
        List<Hall> halls = returnHalls(locationName);
        Optional<Hall> hall = returnHall(hallId, locationName);
        return hall.get();
    }

    @Override
    public List<Screen> getScreens(String locationName, int hallId) {
        List<Hall> halls = returnHalls(locationName);
        Optional<Hall> hall = returnHall(hallId, locationName);
        List<Screen> screens = screenRepository.findAllByHallHallId(hallId);
        if (screens.isEmpty())
            throw new TicketBookingSystemException(NO_SCREENS);
        return screens;
    }

    @Override
    public Screen getScreen(String locationName, int hallId, int screenId) {
        List<Hall> halls = returnHalls(locationName);
        Optional<Hall> hall = returnHall(hallId, locationName);
        Optional<Screen> screen = screenRepository.findByScreenIdAndHallHallId(screenId, hallId);
        if (! screen.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SCREEN);
        return screen.get();
    }

    @Override
    public List<Show> getShows(String locationName, int hallId, int screenId) {
        List<Hall> halls = returnHalls(locationName);
        Optional<Hall> hall = returnHall(hallId, locationName);
        Optional<Screen> screen = screenRepository.findByScreenIdAndHallHallId(screenId, hallId);
        if (! screen.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SCREEN);
        List<Show> shows = showRepository.findAllByScreenScreenId(screenId);
        if(shows.isEmpty())
            throw new TicketBookingSystemException(NO_SHOWS_IN_THIS_SCREEN);
        return shows;
    }

    @Override
    public Show getShow(String locationName, int hallId, int screenId, int showId) {
        List<Hall> halls = returnHalls(locationName);
        Optional<Hall> hall = returnHall(hallId, locationName);
        Optional<Screen> screen = screenRepository.findByScreenIdAndHallHallId(screenId, hallId);
        if (! screen.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SCREEN);
        Optional<Show> show = showRepository.findByShowIdAndScreenScreenId(showId, screenId);
        if (! show.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SHOW);
        return show.get();
    }

    @Override
    public String performLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authHeader = request.getHeader("Authorization");
        String jwtToken = authHeader.substring(7);
        if(authentication!=null)
            jwtService.invalidateToken(jwtToken);
        return "Logged out";
    }

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Show> getShowsOfMovie(int movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (! movie.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_MOVIE);
        List<Show> shows = showRepository.findByMovieMovieId(movieId);
        return shows;
    }
}
