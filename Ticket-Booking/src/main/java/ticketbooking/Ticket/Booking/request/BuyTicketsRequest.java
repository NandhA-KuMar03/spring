package ticketbooking.Ticket.Booking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyTicketsRequest {

    private int count;
    private int showId;
    private int userId;

}
