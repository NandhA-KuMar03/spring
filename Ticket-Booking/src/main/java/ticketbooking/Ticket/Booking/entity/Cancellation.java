package ticketbooking.Ticket.Booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cancellation")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "booking"})
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cancellationId;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isCancelled;

    @JsonIgnoreProperties
    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
