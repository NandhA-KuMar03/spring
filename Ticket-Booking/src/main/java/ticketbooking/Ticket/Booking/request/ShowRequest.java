package ticketbooking.Ticket.Booking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowRequest {

    private String showName;
    private int movieId;
    private int screenId;

}
