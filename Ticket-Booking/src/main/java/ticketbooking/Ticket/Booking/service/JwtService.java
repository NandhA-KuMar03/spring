package ticketbooking.Ticket.Booking.service;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public interface JwtService {
    String extractUserName(String jwtToken);

    boolean isTokenValid(String jwtToken, UserDetails user);

    boolean isTokenExpired(String jwtToken);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateToken(UserDetails userDetails);
    Key getSignInKey();
    Claims extractAllClaims(String jwtToken);
    Date extractExpiration(String jwtToken);

    boolean isTokenInvalid(String token);

    void invalidateToken(String token);


}
