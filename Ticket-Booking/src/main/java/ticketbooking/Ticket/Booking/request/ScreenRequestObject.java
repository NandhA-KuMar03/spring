package ticketbooking.Ticket.Booking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenRequestObject {

    private int hallId;
    private String screenName;
    private int capacity;
    private boolean isActive;

}
