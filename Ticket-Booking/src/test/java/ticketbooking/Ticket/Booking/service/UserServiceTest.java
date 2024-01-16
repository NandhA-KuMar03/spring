package ticketbooking.Ticket.Booking.service;

import io.jsonwebtoken.JwsHeader;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.PushBuilder;
import org.apache.tomcat.util.security.Escape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
import ticketbooking.Ticket.Booking.serviceimpl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.ABOVE_18_AGE;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.ALREADY_REQUESTED_CANCELLATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.GENDER_ERROR;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_ACCESS_IN_PRIME_LOCATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_ACCESS_TO_BOOK_FOR_OTHERS;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_BOOKING;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_BOOKING_NON_EXISTING_USER;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SHOWS_IN_THIS_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_BOOKING_BY_YOU;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_HALL;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_LOCATION;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SHOW;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_TICKETS_LEFT;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.SHOW_DEACTIVATED;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CancellationRepository cancellationRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private ScreenRepository screenRepository;
    @Mock
    private HallRepository hallRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private static JwtService jwtService;
    @Mock
    private static AuthenticationManager authenticationManager;

    @Test
    void getBookings(){
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
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        when(userRepository.findUserByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookedByUserId(1)).thenReturn(List.of(booking2, booking1));
        List<Booking> response = userService.getBookings();
        assertEquals(response.get(0).getBookingId(), 2);
    }

    @Test
    void createUser(){
        List<Role> roles = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        User user1 = null;
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.ofNullable(user1));
        when(roleRepository.findById(1)).thenReturn(role1);
        AuthenticationResponse response = userService.createUser(user);
    }

    @Test
    void createUserException(){
        List<Role> roles = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(2, "email", "Test", "2", "pass", Date.valueOf("2002-09-09"), "m", roles);
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.ofNullable(user));
        try {
            userService.createUser(user);
        }catch (Exception e){
            assertEquals(e.getMessage(), EMAIL_ALREADY_EXISTS);
        }
    }

    @Test
    void createUserAbove18Exception(){
        List<Role> roles = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2006-09-09"), "m", roles);
        User user1 = null;
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.ofNullable(user1));
        try {
            userService.createUser(user);
        }catch (Exception e){
            assertEquals(e.getMessage(), ABOVE_18_AGE);
        }
    }

    @Test
    void createUserGenderException(){
        List<Role> roles = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "male", roles);
        User user1 = null;
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.ofNullable(user1));
        try {
            userService.createUser(user);
        }catch (Exception e){
            assertEquals(e.getMessage(), GENDER_ERROR);
        }
    }

    @Test
    void requestCancellation(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        Booking booking1 = new Booking(1,2,false,100,movie,user,show);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Cancellation cancellation = new Cancellation(0,false, booking1);
        when(cancellationRepository.save(cancellation)).thenReturn(cancellation);
        Cancellation response = userService.requestCancellation(1);
        assertEquals(response.getBooking(), booking1);
    }

    @Test
    void requestCancellationException(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Booking booking = new Booking();
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(bookingRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        try {
            userService.requestCancellation(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_BOOKING);
        }
    }

    @Test
    void requestCancellationNotBookedByUException(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Booking booking = new Booking();
        booking.setBookedBy(new User(2,"email", "test", "2", "pass", Date.valueOf("2002-09-09"), "m", roles));
        when(securityContext.getAuthentication()).thenReturn(auth);
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        try {
            userService.requestCancellation(1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_BOOKING_BY_YOU);
        }
    }

    @Test
    void requestCancellationAlreadyRequestedException(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        Booking booking1 = new Booking(1,2,true,100,movie,user,show);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Cancellation cancellation = new Cancellation(0,false, booking1);
        try {
            userService.requestCancellation(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), ALREADY_REQUESTED_CANCELLATION);
        }
    }

    @Test
    void login(){
        LoginRequest request = new LoginRequest();
        request.setEmail("email");
        request.setPassword("pass");
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("wudshcbweud");
        AuthenticationResponse response = new AuthenticationResponse("wudshcbweud");
        AuthenticationResponse authenticationResponse = userService.login(request);
        assertEquals(authenticationResponse.getAuthToken(), response.getAuthToken());
    }

    @Test
    void bookTickets(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        Role role2 = new Role(2,"ROLE_BUSER");
        roles.add(role1);
        roles.add(role2);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_BUSER"));
        Hall hall = new Hall(1,"hall1", true, "Chennai");
        Screen screen = new Screen(1, "VIdya", 30, 30, true, hall);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        Booking booking1 = new Booking(1,2,false,100,movie,user,show);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        BuyTicketsRequest request = new BuyTicketsRequest(10,1,1);
        TicketBookingSystemException exception = new TicketBookingSystemException(NO_ACCESS_TO_BOOK_FOR_OTHERS);
        try{
            Booking response = userService.bookTickets(request);
        }
        catch (Exception e){
            assertEquals(exception.getMessage(), e.getMessage());
        }
    }

    @Test
    void bookTicketsSuccess(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        Role role2 = new Role(2,"ROLE_BUSER");
        roles.add(role1);
        roles.add(role2);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_BUSER"));
        Hall hall = new Hall(1,"hall1", true, "Chennai");
        Screen screen = new Screen(1, "VIdya", 30, 30, true, hall);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        Booking booking1 = new Booking(0,10,false,1,movie,user,show);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        when(bookingRepository.save(booking1)).thenReturn(booking1);
        BuyTicketsRequest request = new BuyTicketsRequest(10,1,0);
        Booking bookingResponse = userService.bookTickets(request);
        assertEquals(bookingResponse.getBookingId(), booking1.getBookingId());
    }

    @Test
    void bookTicketsNoTickets(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        Role role2 = new Role(2,"ROLE_BUSER");
        roles.add(role1);
        roles.add(role2);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        UserDetails userDetails = (UserDetails) user;
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getPrincipal()).thenReturn((UserDetails)user);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_BUSER"));
        Hall hall = new Hall(1,"hall1", true, "Chennai");
        Screen screen = new Screen(1, "VIdya", 30, 10, true, hall);
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        Booking booking1 = new Booking(0,10,false,1,movie,user,show);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie));
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user));
        BuyTicketsRequest request = new BuyTicketsRequest(15,1,0);
        try {
            userService.bookTickets(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_TICKETS_LEFT);
        }
    }

    @Test
    void bookTicketsException2(){
        BuyTicketsRequest request = new BuyTicketsRequest(10,1,0);
        try {
            userService.bookTickets(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SHOW);
        }
    }

    @Test
    void bookTicketsException3(){
        BuyTicketsRequest request = new BuyTicketsRequest(10,1,0);
        Show show = new Show();
        show.setShowId(1);
        show.setActive(false);
        when(showRepository.findById(1)).thenReturn(Optional.of(show));
        try {
            userService.bookTickets(request);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), SHOW_DEACTIVATED);
        }
    }

    @Test
    void getLocations(){
        LocationRequest request = new LocationRequest("North Little Rock", "Oregon", false, "Fidel Flat", "Berkshire", false, "Implemented real-time secured line", "giving-recovery.net", "1");
        List<LocationResponse> response = userService.getLocations();
        assertEquals(response.get(0).getLocationName(), request.getName());
    }

    @Test
    void getLocation(){
        LocationRequest request = new LocationRequest("North Little Rock", "Oregon", false, "Fidel Flat", "Berkshire", false, "Implemented real-time secured line", "giving-recovery.net", "1");
        LocationRequest response = userService.getLocation("North Little Rock");
        assertEquals(response.getId(), request.getId());
    }

    @Test
    void getLocationException2(){
        try{
            userService.getLocation("North Litctle Rock");
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_LOCATION);
        }
    }

    @Test
    void getLocationPrime(){
        LocationRequest request = new LocationRequest("North Little Rock", "Oregon", false, "Fidel Flat", "Berkshire", false, "Implemented real-time secured line", "giving-recovery.net", "1");
        LocationRequest response = userService.getLocation("Haverhill");
        assertEquals(response.getId(), request.getId());
    }

    @Test
    void getLocationException(){
        LocationRequest request = new LocationRequest("North Little Rock", "Oregon", false, "Fidel Flat", "Berkshire", false, "Implemented real-time secured line", "giving-recovery.net", "1");
        TicketBookingSystemException response = new TicketBookingSystemException("No such location found");
        try{
            userService.getLocation("North Little Rock");
        }
        catch (Exception e){
            assertEquals(response.getMessage(), e.getMessage());
        }
    }

    @Test
    void getHalls(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        List<Hall> response = userService.getHalls("Chennai");
        assertEquals(response.get(0).getHallName(), halls.get(0).getHallName());
    }

    @Test
    void getHallsException(){
        List<Hall> halls = new ArrayList<>();
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        TicketBookingSystemException response = new TicketBookingSystemException("No halls in this location");
        try {
            userService.getHalls("Chennai");
        }catch (Exception e){
            assertEquals(e.getMessage(), response.getMessage());
        }
    }

    @Test
    void getHall(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        Hall response = userService.getHall("Chennai", 1);
        assertEquals(response.getHallName(), hall1.getHallName());
    }

    @Test
    void getHallException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        TicketBookingSystemException exception = new TicketBookingSystemException("No such hall");
        try {
            userService.getHall("Chennai", 1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), exception.getMessage());
        }
    }

    @Test
    void getScreens(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findAllByHallHallId(1)).thenReturn(List.of(screen1,screen2));
        List<Screen> response = userService.getScreens("Chennai", 1);
        assertEquals(response.get(0).getScreenId(), screen1.getScreenId());
    }

    @Test
    void getScreensException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        List<Screen> screens = new ArrayList<>();
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findAllByHallHallId(1)).thenReturn(screens);
        TicketBookingSystemException response = new TicketBookingSystemException("No screens in this hall");
        try {
            userService.getScreens("Chennai", 1);
        }catch (Exception e){
            assertEquals(e.getMessage(), response.getMessage());
        }
    }

    @Test
    void getScreen(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findByScreenIdAndHallHallId(1,1)).thenReturn(Optional.of(screen1));
        Screen response = userService.getScreen("Chennai", 1, 1);
        assertEquals(response.getHall(), hall1);
    }

    @Test
    void getHallException1(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        try{
            userService.getScreen("Chennai", 1, 1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_HALL);
        }
    }

    @Test
    void getScreenException1(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        try{
            userService.getScreen("Chennai", 1, 1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SCREEN);
        }
    }

    @Test
    void getScreenException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findByScreenIdAndHallHallId(1,1)).thenReturn(Optional.of(screen1));
        TicketBookingSystemException exception = new TicketBookingSystemException("No such screen");
        try {
            userService.getScreen("Chennai", 1,1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), exception.getMessage());
        }
    }

    @Test
    void getShows(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Movie movie = new Movie(1, "96", "sdv","wdfw");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        Show show1 = new Show(1,"MS", true, screen1, movie);
        Show show2 = new Show(2,"ES", true, screen1, movie);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findByScreenIdAndHallHallId(1,1)).thenReturn(Optional.of(screen1));
        when(showRepository.findAllByScreenScreenId(1)).thenReturn(List.of(show1,show2));
        List<Show> response = userService.getShows("Chennai", 1,1);
        assertEquals(response.get(0), show1);
    }

    @Test
    void getShowsException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        try{
            userService.getShows("Chennai", 1, 1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SCREEN);
        }
    }

    @Test
    void getShowsException2(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findByScreenIdAndHallHallId(1,1)).thenReturn(Optional.of(screen1));
        List<Show> shows = new ArrayList<>();
        when(showRepository.findAllByScreenScreenId(1)).thenReturn(shows);
        try{
            userService.getShows("Chennai", 1, 1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SHOWS_IN_THIS_SCREEN);
        }
    }

    @Test
    void getShow(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Movie movie = new Movie(1, "96", "sdv","wdfw");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        Show show1 = new Show(1,"MS", true, screen1, movie);
        Show show2 = new Show(2,"ES", true, screen1, movie);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findByScreenIdAndHallHallId(1,1)).thenReturn(Optional.of(screen1));
        when(showRepository.findByShowIdAndScreenScreenId(1,1)).thenReturn(Optional.of(show1));
        Show response = userService.getShow("Chennai", 1,1,1);
        assertEquals(response.getShowId(), show1.getShowId());
    }

    @Test
    void getShowException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Movie movie = new Movie(1, "96", "sdv","wdfw");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Screen screen2 = new Screen(1, "Vetri", 30, 30, true, hall1);
        Show show1 = new Show();
        Show show2 = new Show(2,"ES", true, screen1, movie);
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        when(screenRepository.findByScreenIdAndHallHallId(1,1)).thenReturn(Optional.of(screen1));
        try {
            userService.getShow("Chennai", 1,1,1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SHOW);
        }
    }

    @Test
    void getShowNoShowException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Hall hall2 = new Hall(2,"hall2", true, "Chennai");
        Hall hall3 = new Hall(3,"hall3", true, "Chennai");
        List<Hall> halls = List.of(hall1, hall2, hall3);
        Movie movie = new Movie(1, "96", "sdv","wdfw");
        Screen screen1 = new Screen();
        when(hallRepository.findAllByLocationName("Chennai")).thenReturn(halls);
        when(hallRepository.findByHallIdAndLocationName(1, "Chennai")).thenReturn(Optional.of(hall1));
        TicketBookingSystemException exception = new TicketBookingSystemException(NO_SUCH_SCREEN);
        try {
            userService.getShow("Chennai", 1,1,1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SCREEN);
        }
    }

    @Test
    void getMovies(){
        Movie movie1 = new Movie(1, "Interstellar", "Poster", "sci-fi");
        Movie movie2 = new Movie(2, "96", "Poster", "rom");
        when(movieRepository.findAll()).thenReturn(List.of(movie1, movie2));
        List<Movie> response = userService.getMovies();
        assertEquals(response.get(0), movie1);
    }

    @Test
    void getShowsOfMovie(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Movie movie1 = new Movie(1, "Interstellar", "Poster", "sci-fi");
        Show show1 = new Show(1,"MS", true, screen1, movie1);
        Show show2 = new Show(2,"MS", true, screen1, movie1);
        when(movieRepository.findById(1)).thenReturn(Optional.of(movie1));
        when(showRepository.findByMovieMovieId(1)).thenReturn(List.of(show1, show2));
        List<Show> response = userService.getShowsOfMovie(1);
        assertEquals(response.get(0), show1);
    }

    @Test
    void getShowsOfMovieException(){
        Hall hall1 = new Hall(1,"hall1", true, "Chennai");
        Screen screen1 = new Screen(1, "VIdya", 30, 30, true, hall1);
        Movie movie1 = new Movie(1, "Interstellar", "Poster", "sci-fi");
        Show show1 = new Show(1,"MS", true, screen1, movie1);
        Show show2 = new Show(2,"MS", true, screen1, movie1);
        TicketBookingSystemException exception = new TicketBookingSystemException("No such movie");
        try {
            userService.getShowsOfMovie(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), exception.getMessage());
        }
    }

    @Test
    void performLogout() throws ServletException {
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        roles.add(role1);
        HttpServletRequest request = new HttpServletRequest() {
            @Override
            public String getAuthType() {
                return null;
            }

            @Override
            public Cookie[] getCookies() {
                return new Cookie[0];
            }

            @Override
            public long getDateHeader(String s) {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return "Authorization 1231242412`2sacaew";
            }

            @Override
            public Enumeration<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return null;
            }

            @Override
            public int getIntHeader(String s) {
                return 0;
            }

            @Override
            public HttpServletMapping getHttpServletMapping() {
                return HttpServletRequest.super.getHttpServletMapping();
            }

            @Override
            public String getMethod() {
                return null;
            }

            @Override
            public String getPathInfo() {
                return null;
            }

            @Override
            public String getPathTranslated() {
                return null;
            }

            @Override
            public PushBuilder newPushBuilder() {
                return HttpServletRequest.super.newPushBuilder();
            }

            @Override
            public String getContextPath() {
                return null;
            }

            @Override
            public String getQueryString() {
                return null;
            }

            @Override
            public String getRemoteUser() {
                return null;
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getRequestedSessionId() {
                return null;
            }

            @Override
            public String getRequestURI() {
                return null;
            }

            @Override
            public StringBuffer getRequestURL() {
                return null;
            }

            @Override
            public String getServletPath() {
                return null;
            }

            @Override
            public HttpSession getSession(boolean b) {
                return null;
            }

            @Override
            public HttpSession getSession() {
                return null;
            }

            @Override
            public String changeSessionId() {
                return null;
            }

            @Override
            public boolean isRequestedSessionIdValid() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {
                return false;
            }

            @Override
            public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
                return false;
            }

            @Override
            public void login(String s, String s1) throws ServletException {

            }

            @Override
            public void logout() throws ServletException {

            }

            @Override
            public Collection<Part> getParts() throws IOException, ServletException {
                return null;
            }

            @Override
            public Part getPart(String s) throws IOException, ServletException {
                return null;
            }

            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
                return null;
            }

            @Override
            public Map<String, String> getTrailerFields() {
                return HttpServletRequest.super.getTrailerFields();
            }

            @Override
            public boolean isTrailerFieldsReady() {
                return HttpServletRequest.super.isTrailerFieldsReady();
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

            }

            @Override
            public int getContentLength() {
                return 0;
            }

            @Override
            public long getContentLengthLong() {
                return 0;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletInputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public String getParameter(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return null;
            }

            @Override
            public String[] getParameterValues(String s) {
                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public String getScheme() {
                return null;
            }

            @Override
            public String getServerName() {
                return null;
            }

            @Override
            public int getServerPort() {
                return 0;
            }

            @Override
            public BufferedReader getReader() throws IOException {
                return null;
            }

            @Override
            public String getRemoteAddr() {
                return null;
            }

            @Override
            public String getRemoteHost() {
                return null;
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void removeAttribute(String s) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {
                return null;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String s) {
                return null;
            }

            @Override
            public int getRemotePort() {
                return 0;
            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public String getLocalAddr() {
                return null;
            }

            @Override
            public int getLocalPort() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {
                return null;
            }

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                return null;
            }

            @Override
            public boolean isAsyncStarted() {
                return false;
            }

            @Override
            public boolean isAsyncSupported() {
                return false;
            }

            @Override
            public AsyncContext getAsyncContext() {
                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {
                return null;
            }

            @Override
            public String getRequestId() {
                return null;
            }

            @Override
            public String getProtocolRequestId() {
                return null;
            }

            @Override
            public ServletConnection getServletConnection() {
                return null;
            }
        };
        HttpServletResponse response = new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_BUSER"));
        String response1 = userService.performLogout(request, response);
        assertEquals(response1, "Logged out");
    }
}
