package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.repository.UserDetailsRepository;
import com.application.gatekeeper.repository.UserRepository;
import com.application.gatekeeper.request.EditInfoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.ErrorConstants.APARTMENT_ALREADY_OCCUPIED;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_USER;
import static com.application.gatekeeper.constants.ErrorConstants.USER_ALREADY_APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    AdminServiceImpl adminService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Test
    void getAwaitingApprovals(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        User user2 = new User(2, "email@cc.com", "pass", false, false, roles);
        User user3 = new User(3, "email@cd.com", "pass", false, false, roles);
        when(userRepository.findAllUserByIsActiveFalse()).thenReturn(List.of(user1, user2, user3));
        List<User> response = adminService.getAwaitingApprovals();
        assertEquals(response.get(0).getUserId(), user1.getUserId());
    }

    @Test
    void getUserAwaitingApproval(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "firstName", "lastName", "m", Date.valueOf("2002-01-01"), null);
        when(userDetailsRepository.findByUserUserId(1)).thenReturn(Optional.of(userDetails1));
        UserDetails response = adminService.getUserAwaitingApproval(1);
        assertEquals(response.getUser().getUserId(), user1.getUserId());
    }

    @Test
    void getUserAwaitingApprovalException(){
        try {
            adminService.getUserAwaitingApproval(1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_USER);
        }
    }

    @Test
    void approveUser(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        User response = adminService.approveUser(1);
        assertEquals(response.isApproved(), true);
    }

    @Test
    void approveUserNoUserException(){
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        try {
            adminService.approveUser(1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), NO_SUCH_USER);
        }
    }

    @Test
    void approveUserAlreadyApprovedException(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        try {
            adminService.approveUser(1);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), USER_ALREADY_APPROVED);
        }
    }

    @Test
    void editInfo(){
        EditInfoRequest request = new EditInfoRequest();
        request.setUserId(1);
        request.setFirstName("firstName");
        request.setLastName("lastName");
        request.setDob(Date.valueOf("2000-01-01"));
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "last", "m", Date.valueOf("2002-01-01"), null);
        when(userDetailsRepository.findByUserUserId(1)).thenReturn(Optional.of(userDetails1));
        UserDetails response = adminService.editInfo(request);
        assertEquals(response.getUserFirstName(), request.getFirstName());
    }

    @Test
    void editInfoApartment(){
        EditInfoRequest request = new EditInfoRequest();
        request.setUserId(1);
        request.setFirstName("firstName");
        request.setApartment("A101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "lastName", "m", Date.valueOf("2002-01-01"), null);
        UserDetails userDetails2 = new UserDetails(2,user1, "name", "name2", "M", Date.valueOf("2001-01-01"), "A102");
        when(userDetailsRepository.findByUserUserId(1)).thenReturn(Optional.of(userDetails1));
        when(userDetailsRepository.findAllByApartmentIgnoreCase(request.getApartment())).thenReturn(List.of(userDetails2));
        when(userRepository.findByIsActiveTrueAndUserIdIn(List.of(1))).thenReturn(null);
        UserDetails response = adminService.editInfo(request);
        assertEquals(response.getApartment(), request.getApartment());
    }

    @Test
    void editInfoApartmentException(){
        EditInfoRequest request = new EditInfoRequest();
        request.setUserId(1);
        request.setFirstName("firstName");
        request.setApartment("A101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "lastName", "m", Date.valueOf("2002-01-01"), null);
        UserDetails userDetails2 = new UserDetails(2,user1, "name", "name2", "M", Date.valueOf("2001-01-01"), "A101");
        when(userDetailsRepository.findByUserUserId(1)).thenReturn(Optional.of(userDetails1));
        when(userDetailsRepository.findAllByApartmentIgnoreCase(request.getApartment())).thenReturn(List.of(userDetails2));
        when(userRepository.findByIsActiveTrueAndUserIdIn(List.of(1))).thenReturn(user1);
        try {
            adminService.editInfo(request);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), APARTMENT_ALREADY_OCCUPIED);
        }
    }

    @Test
    void removeUser(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        adminService.removeUser(1);
        assertEquals(user1.isActive(), false);
    }

}
