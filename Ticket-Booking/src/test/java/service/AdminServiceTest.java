package service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.repository.HallRepository;
import ticketbooking.Ticket.Booking.repository.RoleRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.serviceimpl.AdminServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        hall.setActive(true);
        hall.setHallName("Hall1");
        hall.setHallId(1);
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




}
