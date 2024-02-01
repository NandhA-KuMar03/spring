package com.application.gatekeeper.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

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
    String generateTokenForVisitor(UserDetails userDetails, Date entry, Date exit, String email);
    boolean isVisitorValid(String jwtToken);
}
