package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.repository.BlacklistRepository;
import com.application.gatekeeper.repository.UserRepository;
import com.application.gatekeeper.repository.VisitorDetailsRepository;
import com.application.gatekeeper.repository.VisitorRepository;
import com.application.gatekeeper.request.ApproveVisitorRequest;
import com.application.gatekeeper.request.BlacklistRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR_DETAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GateKeeperServiceTest {

    @InjectMocks
    GatekeeperServiceImpl gatekeeperService;
    @Mock
    private VisitorDetailsRepository visitorDetailsRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void getVisitorsOnDate(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        when(visitorDetailsRepository.findAllByDateOfVisit(Date.valueOf("2024-01-29"))).thenReturn(List.of(visitorDetails1, visitorDetails2));
        List<VisitorDetails> visitorDetails = gatekeeperService.getVisitorsOnDate(Date.valueOf("2024-01-29"));
        assertEquals(visitorDetails.get(0).getVisitor(), visitor1);
    }

    @Test
    void respondToVisitorScheduleRequest(){
        ApproveVisitorRequest request = new ApproveVisitorRequest(1,"true");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        when(visitorDetailsRepository.findById(1)).thenReturn(Optional.of(visitorDetails1));
        VisitorDetails response = gatekeeperService.respondToVisitorScheduleRequest(request);
        assertEquals(response.isApproved(), true);
    }

    @Test
    void respondToVisitorScheduleRequestNoUserException(){
        ApproveVisitorRequest request = new ApproveVisitorRequest(1,"true");
        when(visitorDetailsRepository.findById(1)).thenReturn(Optional.empty());
        try {
            gatekeeperService.respondToVisitorScheduleRequest(request);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_VISITOR_DETAIL);
        }
    }

    @Test
    void getAllVisitorDetailsOfParticularVisitor(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        when(visitorRepository.findById(1)).thenReturn(Optional.of(visitor1));
        when(visitorDetailsRepository.findAllByVisitorVisitorId(1)).thenReturn(List.of(visitorDetails1, visitorDetails2));
        List<VisitorDetails> response = gatekeeperService.getAllVisitorDetailsOfParticularVisitor(1);
        assertEquals(response.get(0).getVisitorDetailId(), visitorDetails1.getVisitorDetailId());
    }

    @Test
    void getAllVisitorDetailsOfParticularVisitorNoSuchVisitorException(){
        when(visitorRepository.findById(1)).thenReturn(Optional.empty());
        try {
            gatekeeperService.getAllVisitorDetailsOfParticularVisitor(1);
        } catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_VISITOR);
        }
    }

    @Test
    void getAllVisitorDetailsOfParticularVisitorActiveData(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-02-29"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor1, Date.valueOf("2024-02-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        when(visitorRepository.findById(1)).thenReturn(Optional.of(visitor1));
        LocalDate now = LocalDate.now();
        when(visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisitAfter(1, Date.valueOf(now))).thenReturn(List.of(visitorDetails1, visitorDetails2));
        List<VisitorDetails> response = gatekeeperService.getAllVisitorDetailsOfParticularVisitorActiveData(1);
        assertEquals(response.get(0).getVisitorDetailId(), visitorDetails1.getVisitorDetailId());
    }

    @Test
    void blackListVisitor(){
        BlacklistRequest request = new BlacklistRequest("bl@email.com", "Crime");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        Visitor visitor1 = new Visitor(1,"visName", "bl@email.com", false);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-02-29"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor1, Date.valueOf("2024-02-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        LocalDate now = LocalDate.now();
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor1));
        Blacklist blacklist = new Blacklist(0, visitor1, user1, request.getReason());
        when(visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisitAfter(1, Date.valueOf(now))).thenReturn(List.of(visitorDetails1, visitorDetails2));
        when(blacklistRepository.save(blacklist)).thenReturn(blacklist);
        Blacklist response = gatekeeperService.blackListVisitor(request);
        assertEquals(response.getVisitor().getEmail(), request.getEmail());
    }

    @Test
    void blackListNoSuchVisitor(){
        BlacklistRequest request = new BlacklistRequest("bl@email.com", "Crime");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        try {
            gatekeeperService.blackListVisitor(request);
        }catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_VISITOR);
        }

    }
}
