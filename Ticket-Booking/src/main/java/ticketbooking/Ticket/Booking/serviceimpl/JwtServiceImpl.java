package ticketbooking.Ticket.Booking.serviceimpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ticketbooking.Ticket.Booking.entity.User;
import ticketbooking.Ticket.Booking.exception.TicketBookingSystemException;
import ticketbooking.Ticket.Booking.repository.UserRepository;
import ticketbooking.Ticket.Booking.service.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private UserRepository userRepository;

    @Autowired
    public JwtServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final String secretKey = "eyJhbGciOiJIUzI1NiJ9ew0KICAic3ViIjogIjEyMzQ1Njc4OTAiLA0KICAibmFtZSI6ICJBbmlzaCBOYXRoIiwNCiAgImlhdCI6IDE1MTYyMzkwMjINCn0AUOwfaHjT73Vd5MsqVDna9l7FlvwF6GuEQQHabxhGc";

    private Set<String> invalidatedTokens = new HashSet<>();
    public String extractUserName(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        System.out.println(extraClaims);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1800000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }

    public Date extractExpiration(String jwtToken){
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidatedTokens.contains(token);
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String userEmail = extractUserName(jwtToken);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken) && !isTokenInvalid(jwtToken) && isUserActive(userEmail);
    }

    private boolean isUserActive(String userEmail) {
        System.out.println("heer");
        Optional<User> user =  userRepository.findUserByEmail(userEmail);
        if (user.isPresent()){
            System.out.println(System.currentTimeMillis() - user.get().getLastActivity());
            if ((System.currentTimeMillis() - user.get().getLastActivity()) > 60000){
                throw new TicketBookingSystemException("User inactive above 1 minute, try logging in again");
            }
            user.get().setLastActivity(System.currentTimeMillis());
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
