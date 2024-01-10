package ticketbooking.Ticket.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketbooking.Ticket.Booking.entity.Show;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    List<Show> findAllByScreenScreenIdIn(List<Integer> list);
    List<Show> findAllByScreenScreenId(int screenId);

    Optional<Show> findByShowIdAndScreenScreenId(int showId, int screenId);
}
