package ticketbooking.Ticket.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketbooking.Ticket.Booking.entity.Screen;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer> {

    List<Screen> findAllByHallHallId(int hallId);

    Optional<Screen> findByScreenIdAndHallHallId(int screenId, int hallId);
}
