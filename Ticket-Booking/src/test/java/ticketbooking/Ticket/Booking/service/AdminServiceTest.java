package ticketbooking.Ticket.Booking.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Movie;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.repository.HallRepository;
import ticketbooking.Ticket.Booking.repository.RoleRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.request.ScreenRequestObject;
import ticketbooking.Ticket.Booking.serviceimpl.AdminServiceImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_HALL;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_USER;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    AdminServiceImpl adminService;

    @Mock
    private HallRepository hallRepository;
    @Mock
    private ScreenRepository screenRepository;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void getHalls(){
        List<Hall> halls = new ArrayList<>();
        Hall temporarySaveHall = new Hall();
        temporarySaveHall.setHallName("Hall1");
        temporarySaveHall.setLocationName("Haverhill");
        temporarySaveHall.setHallId(1);
        temporarySaveHall.setActive(true);
        halls.add(temporarySaveHall);
        when(hallRepository.findAll()).thenReturn(halls);
        List<Hall> response = adminService.getHalls();
        assertEquals(response.get(0).getHallName(), halls.get(0).getHallName());
    }

    @Test
    void createHall(){
        Hall hall = new Hall();
        hall.setActive(false);
        hall.setHallName("Hall1");
        hall.setHallId(0);
        when(hallRepository.save(hall)).thenReturn(hall);
        Hall response = adminService.createHall(hall);
        assertEquals(hall.getHallId(), response.getHallId());
    }

    @Test
    void getScreens(){
        List<Screen> screens = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        screens.add(screen);
        when(screenRepository.findAll()).thenReturn(screens);
        List<Screen> response = adminService.getScreens();
        assertEquals(response, screens);
    }

    @Test
    void deactivateHall(){
        Hall hall = new Hall();
        hall.setActive(true);
        hall.setHallName("Hall1");
        hall.setHallId(1);
        List<Screen> screens = new ArrayList<>();
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        screens.add(screen);
        Movie movie = new Movie(1,"96", "ede", "movie");
        List<Show> shows = new ArrayList<>();
        Show show = new Show(1,"MS", true, screen, movie);
        shows.add(show);
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));
        when(screenRepository.findAllByHallHallId(1)).thenReturn(screens);
        when(showRepository.findAllByScreenScreenIdIn(List.of(1))).thenReturn(shows);
        Hall response = adminService.deactivateHall(1);
        assertEquals(response.isActive(), false);
    }

    @Test
    void deactivateHallException(){
        try {
            adminService.deactivateHall(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_HALL);
        }
    }

    @Test
    void createScreen(){
        Hall hall = new Hall();
        hall.setActive(true);
        hall.setHallName("Hall1");
        hall.setHallId(1);
        Screen screen = new Screen(1, "Hall1", 30, 30, true, hall);
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));
        ScreenRequestObject requestObject = new ScreenRequestObject(1,"Hall1",30,true);
        Screen response = adminService.createScreen(requestObject);
        assertEquals(screen.getScreenName(), requestObject.getScreenName());
    }

    @Test
    void createScreenException(){
        ScreenRequestObject requestObject = new ScreenRequestObject();
        requestObject.setActive(false);
        try {
            adminService.createScreen(requestObject);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_HALL);
        }
    }

    @Test
    void deactivateScreen(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(true);
        screen.setSeatsRemaining(40);
        List<Show> shows = new ArrayList<>();
        Movie movie = new Movie(1,"96", "ede", "movie");
        Show show = new Show(1,"MS", true, screen, movie);
        shows.add(show);
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        when(showRepository.findAllByScreenScreenId(1)).thenReturn(shows);
        Screen response = adminService.deactivateScreen(1);
        assertEquals(response.isActive(), false);
    }

    @Test
    void deactivateScreenException(){
        try {
            adminService.deactivateScreen(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SCREEN);
        }
    }

    @Test
    void activateScreen(){
        Screen screen = new Screen();
        screen.setScreenName("Vidya");
        screen.setScreenId(1);
        screen.setCapacity(40);
        screen.setActive(false);
        screen.setSeatsRemaining(40);
        when(screenRepository.findById(1)).thenReturn(Optional.of(screen));
        Screen response = adminService.activateScreen(1);
        assertEquals(response.isActive(), true);
    }

    @Test
    void activateScreenException(){
        try {
            adminService.activateScreen(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_SCREEN);
        }
    }

    @Test
    void activateHall(){
        Hall hall = new Hall();
        hall.setActive(false);
        hall.setHallName("Hall1");
        hall.setHallId(1);
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));
        Hall response = adminService.activateHall(1);
        assertEquals(response.isActive(), true);
    }

    @Test
    void activateHallException(){
        try {
            adminService.activateHall(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_HALL);
        }
    }

    @Test
    void promoteUserException(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        Role role2 = new Role(2, "B_USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        when(roleRepository.findById(1)).thenReturn(role1);
        when(roleRepository.findById(2)).thenReturn(role2);
        try {
            adminService.promoteUser(1);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_USER);
        }
    }

    @Test
    void promoteUser(){
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"USER");
        Role role2 = new Role(2, "B_USER");
        roles.add(role1);
        User user = new User(1,"email","Test", "name", "pass", Date.valueOf("2002-09-09"), "m", roles);
        when(roleRepository.findById(1)).thenReturn(role1);
        when(roleRepository.findById(2)).thenReturn(role2);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        User response = adminService.promoteUser(1);
        assertEquals(response.getRoles().get(0), role2);
    }
}
