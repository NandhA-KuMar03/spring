package ticketbooking.Ticket.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketbooking.Ticket.Booking.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
