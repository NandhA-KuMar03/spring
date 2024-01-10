package ticketbooking.Ticket.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketbooking.Ticket.Booking.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int roleId);
}
