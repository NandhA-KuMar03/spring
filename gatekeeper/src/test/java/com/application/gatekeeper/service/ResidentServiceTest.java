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
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.VisitorRegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.ErrorConstants.ALREADY_BLACKLISTED;
import static com.application.gatekeeper.constants.ErrorConstants.GATEKEEPER_CANNOT_CREATE_VISITOR;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_VISITOR_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.TIME_OVER;
import static com.application.gatekeeper.constants.ErrorConstants.USER_DEACTIVATED;
import static com.application.gatekeeper.constants.ErrorConstants.USER_NOT_APPROVED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_ALREADY_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BLOCKED;
import static com.application.gatekeeper.constants.ErrorConstants.VISITOR_BUSY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResidentServiceTest {

    @InjectMocks
    private ResidentServiceImpl residentService;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private VisitorDetailsRepository visitorDetailsRepository;
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private static JwtService jwtService;

    @Test
    void createVisitorNotApprovedException(){
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        VisitorRegisterRequest request = new VisitorRegisterRequest("v1@v.com", "Visitor");
        Visitor visitor = new Visitor(0, request.getName(), request.getEmail(), false);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        try {
            residentService.createAndScheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), USER_NOT_APPROVED);
        }
    }

    @Test
    void createVisitorDeactivatedException(){
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        VisitorRegisterRequest request = new VisitorRegisterRequest("v1@v.com", "Visitor");
        Visitor visitor = new Visitor(0, request.getName(), request.getEmail(), false);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        try {
            residentService.createAndScheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), USER_DEACTIVATED);
        }
    }


    @Test
    void createVisitorAlreadyExistsException(){
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        VisitorRegisterRequest request = new VisitorRegisterRequest("v1@v.com", "Visitor");
        Visitor visitor = new Visitor(0, request.getName(), request.getEmail(), false);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
        try {
            residentService.createAndScheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), VISITOR_ALREADY_EXISTS);
        }
    }

    @Test
    void createVisitor(){
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        VisitorRegisterRequest request = new VisitorRegisterRequest("v1@v.com", "Visitor");
        Visitor visitor = new Visitor(0, request.getName(), request.getEmail(), false);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(visitorRepository.save(visitor)).thenReturn(visitor);
        Visitor response = residentService.createAndScheduleVisitor(request);
        assertEquals(response.getEmail(), request.getEmail());
    }

    @Test
    void scheduleVisitor(){
        VisitorRequest request = new VisitorRequest("v1@v.com", Date.valueOf("2024-02-29"), Time.valueOf("19:00:00"), Time.valueOf("19:30:00"));
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), false);
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
//        when(visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisitAfter(0, request.getDateOfVisit())).thenReturn(List.of());
        LocalDate localDateEntry = request.getDateOfVisit().toLocalDate();
        LocalDateTime dateTime  = localDateEntry.atTime(request.getTimeOfEntry().toLocalTime());
        Instant instantStart = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date dateEntry = java.util.Date.from(instantStart);
        LocalDate localDateExit = request.getDateOfVisit().toLocalDate();
        LocalDateTime dateTimeExit  = localDateExit.atTime(request.getTimeOfExit().toLocalTime());
        Instant instantEnd = dateTimeExit.atZone(ZoneId.systemDefault()).toInstant();
        java.util.Date dateExit = java.util.Date.from(instantEnd);
        String passkey = "ewggaedfsWTYRJNDGHSBFV.WRHTEJHFSBV";
        when(jwtService.generateTokenForVisitor(user1, dateEntry, dateExit, request.getEmail())).thenReturn(passkey);
        VisitorDetails visitorDetails = new VisitorDetails(0, visitor, request.getDateOfVisit(), passkey, false, user1, request.getTimeOfEntry(), request.getTimeOfExit(), false);
        when(visitorDetailsRepository.save(visitorDetails)).thenReturn(visitorDetails);
        VisitorDetails response = residentService.scheduleVisitor(request);
        assertEquals(response.getVisitor(), visitor);
    }

    @Test
    void scheduleNoSuchVisitorExists(){
        VisitorRequest request = new VisitorRequest("v1@v.com", Date.valueOf("2024-02-29"), Time.valueOf("19:00:00"), Time.valueOf("19:30:00"));
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), false);
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        try {
            residentService.scheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_VISITOR_EXISTS);
        }
    }

    @Test
    void scheduleBlacklisted(){
        VisitorRequest request = new VisitorRequest("v1@v.com", Date.valueOf("2024-02-29"), Time.valueOf("19:00:00"), Time.valueOf("19:30:00"));
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), true);
        Blacklist blacklist1 = new Blacklist(0, visitor, user1,"Crime");
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
        when(blacklistRepository.findAllByVisitorEmail(request.getEmail())).thenReturn(List.of(blacklist1));
        try {
            residentService.scheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), VISITOR_BLOCKED);
        }
    }

    @Test
    void scheduleTimeOverException(){
        VisitorRequest request = new VisitorRequest("v1@v.com", Date.valueOf("2024-01-28"), Time.valueOf("19:00:00"), Time.valueOf("19:30:00"));
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), false);
        Blacklist blacklist1 = new Blacklist(0, visitor, user1,"Crime");
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
        try {
            residentService.scheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), TIME_OVER);
        }
    }

    @Test
    void scheduleBusyException(){
        VisitorRequest request = new VisitorRequest("v1@v.com", Date.valueOf("2024-02-28"), Time.valueOf("19:00:00"), Time.valueOf("19:30:00"));
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), false);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:25:00"), Time.valueOf("19:50:00"), false);
        Blacklist blacklist1 = new Blacklist(0, visitor, user1,"Crime");
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
//        when(visitorDetailsRepository.findAllByVisitorVisitorIdAndDateOfVisitAfter(0, request.getDateOfVisit())).thenReturn(List.of(visitorDetails2));
        try {
            residentService.scheduleVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), VISITOR_BUSY);
        }
    }

    @Test
    void blackListVisitor(){
        BlacklistRequest request = new BlacklistRequest("v1@v.com", "Crime");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), false);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
        when(blacklistRepository.findByVisitorVisitorIdAndUserUserId(0,1)).thenReturn(Optional.empty());
        Blacklist blacklist = new Blacklist(0, visitor, user1, "Crime");
        when(blacklistRepository.save(blacklist)).thenReturn(blacklist);
        Blacklist response = residentService.blackListVisitor(request);
        assertEquals(response.getVisitor(), visitor);
    }

    @Test
    void blackListVisitorNoVisitorExists(){
        BlacklistRequest request = new BlacklistRequest("v1@v.com", "Crime");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role1 = new Role(2, "ROLE_GATEKEEPER");
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), false);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        try {
            residentService.blackListVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_VISITOR_EXISTS);
        }
    }

    @Test
    void blackListVisitorAlreadyBlacklistedExists(){
        BlacklistRequest request = new BlacklistRequest("v1@v.com", "Crime");
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        Visitor visitor = new Visitor(0, "visitor", request.getEmail(), true);
        Blacklist blacklist = new Blacklist(0, visitor, user1, "Crime");
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(visitor));
        when(blacklistRepository.findByVisitorVisitorIdAndUserUserId(0,1)).thenReturn(Optional.of(blacklist));
        try {
            residentService.blackListVisitor(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), ALREADY_BLACKLISTED);
        }
    }

    @Test
    void getVisitors(){
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        Visitor visitor = new Visitor(1, "visitor", "v1@v.com", true);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:25:00"), Time.valueOf("19:50:00"), false);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        when(visitorDetailsRepository.findAllByUserUserId(1)).thenReturn(List.of(visitorDetails1, visitorDetails2));
        List<VisitorDetails> response = residentService.getVisitors();
        assertEquals(response.get(0), visitorDetails1);
    }

    @Test
    void getVisitorsActive(){
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        Visitor visitor = new Visitor(1, "visitor", "v1@v.com", true);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:25:00"), Time.valueOf("19:50:00"), false);
        when(auth.getPrincipal()).thenReturn((UserDetails)user1);
        when(userRepository.findUserByEmail("email@e.com")).thenReturn(Optional.of(user1));
        LocalDate now = LocalDate.now();
        when(visitorDetailsRepository.findAllByUserUserIdAndDateOfVisitAfter(1, Date.valueOf(now))).thenReturn(List.of(visitorDetails1, visitorDetails2));
        List<VisitorDetails> response = residentService.getActiveVisitors();
        assertEquals(response.get(0), visitorDetails1);
    }



}
