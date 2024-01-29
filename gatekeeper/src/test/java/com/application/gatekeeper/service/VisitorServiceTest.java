package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.repository.BlacklistRepository;
import com.application.gatekeeper.repository.VisitorDetailsRepository;
import com.application.gatekeeper.repository.VisitorRepository;
import com.application.gatekeeper.response.VisitorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.CommonConstants.VISITOR_VISITED;
import static com.application.gatekeeper.constants.ErrorConstants.JWT_TOKEN_MISSING;
import static com.application.gatekeeper.constants.ErrorConstants.TIME_MISMATCH;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BLACKLISTED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_NOT_APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceTest {

    @InjectMocks
    private VisitorServiceImpl visitorService;
    @Mock
    private VisitorDetailsRepository visitorDetailsRepository;
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private JwtService jwtService;

    @Test
    void visitorEntry(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String authHeader = "Bearer dqwfgrethy4t3.rftg4y3.rg45grfe";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        String jwtToken = authHeader.substring(7);
        when(jwtService.isVisitorValid(jwtToken)).thenReturn(true);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        Visitor visitor = new Visitor(0, "visitor", "v1@v.com", false);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), jwtToken, true, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        when(visitorDetailsRepository.findByVisitorPasskey(jwtToken)).thenReturn(visitorDetails1);
        when(visitorRepository.findByEmail(visitor.getEmail())).thenReturn(Optional.of(visitor));
        VisitorResponse response = visitorService.visitorEntry(request);
        assertEquals(response.getMessage(), VISITOR_VISITED);
    }

    @Test
    void visitorEntryBlacklistedException(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String authHeader = "Bearer dqwfgrethy4t3.rftg4y3.rg45grfe";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        String jwtToken = authHeader.substring(7);
        when(jwtService.isVisitorValid(jwtToken)).thenReturn(true);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        Visitor visitor = new Visitor(0, "visitor", "v1@v.com", true);
        Blacklist blacklist = new Blacklist(1, visitor, user1, "Crime");
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), jwtToken, true, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        when(visitorDetailsRepository.findByVisitorPasskey(jwtToken)).thenReturn(visitorDetails1);
        when(visitorRepository.findByEmail(visitor.getEmail())).thenReturn(Optional.of(visitor));
        when(blacklistRepository.findAllByVisitorEmail(visitor.getEmail())).thenReturn(List.of(blacklist));
        try {
            visitorService.visitorEntry(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), VISITOR_BLACKLISTED);
        }
    }

    @Test
    void visitorEntryNotApprovedException(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String authHeader = "Bearer dqwfgrethy4t3.rftg4y3.rg45grfe";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        String jwtToken = authHeader.substring(7);
        when(jwtService.isVisitorValid(jwtToken)).thenReturn(true);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        Visitor visitor = new Visitor(0, "visitor", "v1@v.com", true);
        Blacklist blacklist = new Blacklist(1, visitor, user1, "Crime");
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), jwtToken, false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        when(visitorDetailsRepository.findByVisitorPasskey(jwtToken)).thenReturn(visitorDetails1);
        try {
            visitorService.visitorEntry(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), VISITOR_NOT_APPROVED);
        }
    }

    @Test
    void visitorEntryTimeException(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String authHeader = "Bearer dqwfgrethy4t3.rftg4y3.rg45grfe";
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        String jwtToken = authHeader.substring(7);
        when(jwtService.isVisitorValid(jwtToken)).thenReturn(false);
        try {
            visitorService.visitorEntry(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), TIME_MISMATCH);
        }
    }

    @Test
    void visitorEntryTokenException(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);
        try {
            visitorService.visitorEntry(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), JWT_TOKEN_MISSING);
        }
    }



}
