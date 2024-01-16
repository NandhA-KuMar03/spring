package ticketbooking.Ticket.Booking.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Role;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.Show;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.exception.TicketBookingSystemException;
import ticketbooking.Ticket.Booking.repository.HallRepository;
import ticketbooking.Ticket.Booking.repository.RoleRepository;
import ticketbooking.Ticket.Booking.repository.ScreenRepository;
import ticketbooking.Ticket.Booking.repository.ShowRepository;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.request.ScreenRequestObject;
import ticketbooking.Ticket.Booking.service.AdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_HALL;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_SCREEN;
import static ticketbooking.Ticket.Booking.constants.ErrorConstants.NO_SUCH_USER;

@Service
public class AdminServiceImpl implements AdminService {

    private HallRepository hallRepository;
    private ScreenRepository screenRepository;
    private ShowRepository showRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(HallRepository hallRepository, ScreenRepository screenRepository, ShowRepository showRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.hallRepository = hallRepository;
        this.screenRepository = screenRepository;
        this.showRepository = showRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Hall> getHalls() {
        return hallRepository.findAll();
    }

    @Override
    public Hall createHall(Hall hall) {
        Hall temporarySaveHall = new Hall();
        temporarySaveHall.setHallName(hall.getHallName());
        temporarySaveHall.setLocationName(hall.getLocationName());
        temporarySaveHall.setActive(false);
        Hall responseHall = hallRepository.save(temporarySaveHall);
        return responseHall;
    }

    @Override
    public Hall deactivateHall(int hallId) {
        Optional<Hall> hall = hallRepository.findById(hallId);
        if(! hall.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_HALL);
        hall.get().setActive(false);
        List<Screen> screens = screenRepository.findAllByHallHallId(hallId);
        List<Integer> screenIds = screens.stream().map(screen -> screen.getScreenId()).collect(Collectors.toList());
        List<Show> shows = showRepository.findAllByScreenScreenIdIn(screenIds);
        System.out.println(screens);
        System.out.println(shows);
        screens.stream()
                .forEach(screen -> screen.setActive(false));
        shows.stream()
                        .forEach(show -> show.setActive(false));
        hallRepository.save(hall.get());
        screenRepository.saveAll(screens);
        showRepository.saveAll(shows);
        return hall.get();
    }

    @Override
    public List<Screen> getScreens() {
        return screenRepository.findAll();
    }

    @Override
    public Screen createScreen(ScreenRequestObject screen) {
        Optional<Hall> hall = hallRepository.findById(screen.getHallId());
        if(! hall.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_HALL);
        Screen temporarySaveScreen = new Screen();
        temporarySaveScreen.setActive(false);
        temporarySaveScreen.setScreenName(screen.getScreenName());
        temporarySaveScreen.setCapacity(screen.getCapacity());
        temporarySaveScreen.setHall(hall.get());
        temporarySaveScreen.setSeatsRemaining(screen.getCapacity());
        Screen responseScreen = screenRepository.save(temporarySaveScreen);
        return responseScreen;
    }

    @Override
    public Screen deactivateScreen(int screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        if (! screen.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SCREEN);
        screen.get().setActive(false);
        List<Show> shows = showRepository.findAllByScreenScreenId(screenId);
        shows.stream()
                        .forEach(show -> show.setActive(false));
        showRepository.saveAll(shows);
        screenRepository.save(screen.get());
        return screen.get();
    }

    @Override
    public Screen activateScreen(int screenId) {
        Optional<Screen> screen = screenRepository.findById(screenId);
        if (! screen.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_SCREEN);
        screen.get().setActive(true);
        screenRepository.save(screen.get());
        return screen.get();
    }

    @Override
    public Hall activateHall(int hallId) {
        Optional<Hall> hall = hallRepository.findById(hallId);
        if(! hall.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_HALL);
        hall.get().setActive(true);
        hallRepository.save(hall.get());
        return hall.get();
    }

    @Override
    public User promoteUser(int userId) {
        Role role = roleRepository.findById(2);
        Role role1 = roleRepository.findById(1);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        roles.add(role1);
        Optional<User> user = userRepository.findById(userId);
        if(! user.isPresent())
            throw new TicketBookingSystemException(NO_SUCH_USER);
        user.get().setRoles(roles);
        userRepository.save(user.get());
        return user.get();
    }
}
