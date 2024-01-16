package ticketbooking.Ticket.Booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketbooking.Ticket.Booking.entity.Cancellation;

import java.util.List;

@Repository
public interface CancellationRepository extends JpaRepository<Cancellation, Integer> {

    List<Cancellation> findAllByIsCancelledFalse();

}
