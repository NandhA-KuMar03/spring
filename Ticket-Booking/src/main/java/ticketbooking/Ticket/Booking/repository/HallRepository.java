package ticketbooking.Ticket.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketbooking.Ticket.Booking.entity.Hall;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {

    List<Hall> findAllByLocationName(String locationName);

    Optional<Hall> findByHallIdAndLocationName(int hallId, String locationName);
}
