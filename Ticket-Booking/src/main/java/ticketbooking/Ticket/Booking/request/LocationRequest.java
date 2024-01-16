package ticketbooking.Ticket.Booking.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("state")
    private String state;
    @JsonProperty("isPrime")
    private Boolean isPrime;
    @JsonProperty("street")
    private String street;
    @JsonProperty("county")
    private String county;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("catchPhrase")
    private String catchPhrase;
    @JsonProperty("internetDomain")
    private String internetDomain;
    @JsonProperty("id")
    private String id;
}