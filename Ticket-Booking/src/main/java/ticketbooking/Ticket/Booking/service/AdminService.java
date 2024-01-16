package ticketbooking.Ticket.Booking.service;

import ticketbooking.Ticket.Booking.entity.Hall;
import ticketbooking.Ticket.Booking.entity.Screen;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.request.ScreenRequestObject;

import java.util.List;

public interface AdminService {

    public List<Hall> getHalls();

    public Hall createHall(Hall hall);

    public Hall deactivateHall(int hallId);

    public List<Screen> getScreens();

    public Screen createScreen(ScreenRequestObject screen);

    public Screen deactivateScreen(int screenId);

    public Screen activateScreen(int screenId);

    public Hall activateHall(int hallId);

    public User promoteUser(int userId);
}
