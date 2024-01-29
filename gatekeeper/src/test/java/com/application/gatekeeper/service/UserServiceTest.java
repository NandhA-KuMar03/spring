package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.repository.RoleRepository;
import com.application.gatekeeper.repository.UserDetailsRepository;
import com.application.gatekeeper.repository.UserRepository;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.response.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.ErrorConstants.ABOVE_18_AGE;
import static com.application.gatekeeper.constants.ErrorConstants.APARTMENT_ALREADY_OCCUPIED;
import static com.application.gatekeeper.constants.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.GENDER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private static PasswordEncoder passwordEncoder;
    @Mock
    private static JwtService jwtService;
    @Mock
    private static AuthenticationManager authenticationManager;

    @Test
    void login(){
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        LoginRequest request = new LoginRequest();
        request.setEmail("email");
        request.setPassword("pass");
        when(userRepository.findUserByEmail("email")).thenReturn(Optional.of(user1));
        when(jwtService.generateToken(user1)).thenReturn("wudshcbweud");
        AuthenticationResponse response = new AuthenticationResponse("wudshcbweud");
        AuthenticationResponse authenticationResponse = userService.login(request);
        assertEquals(authenticationResponse.getAuthToken(), response.getAuthToken());
    }

    @Test
    void registerEmailException(){
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "m", Date.valueOf("2001-01-01"), "a101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user1));
        try {
            userService.createUser(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), EMAIL_ALREADY_EXISTS);
        }
    }

    @Test
    void registerAgeException(){
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "m", Date.valueOf("2011-01-01"), "a101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email1@e.com", "pass", true, true, roles);
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        try {
            userService.createUser(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), ABOVE_18_AGE);
        }
    }

    @Test
    void registerGenderException(){
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "K", Date.valueOf("2001-01-01"), "a101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email1@e.com", "pass", true, true, roles);
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        try {
            userService.createUser(request);
        } catch (Exception e){
            assertEquals(e.getMessage(), GENDER_ERROR);
        }
    }

    @Test
    void ApartmentException(){
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "M", Date.valueOf("2001-01-01"), "a101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email1@e.com", "pass", true, true, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "lastName", "m", Date.valueOf("2002-01-01"), "a101");
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userDetailsRepository.findAllByApartmentIgnoreCase(request.getApartment())).thenReturn(List.of(userDetails1));
        when(userRepository.findByIsActiveTrueAndUserIdIn(List.of(1))).thenReturn(user1);
        try {
            userService.createUser(request);
        }
        catch (Exception e){
            assertEquals(e.getMessage(), APARTMENT_ALREADY_OCCUPIED);
        }
    }

    @Test
    void createUser(){
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "M", Date.valueOf("2001-01-01"), "a101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email1@e.com", "pass", true, true, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "lastName", "m", Date.valueOf("2002-01-01"), "a101");
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userDetailsRepository.findAllByApartmentIgnoreCase(request.getApartment())).thenReturn(List.of(userDetails1));
        when(userRepository.findByIsActiveTrueAndUserIdIn(List.of(1))).thenReturn(null);
        when(roleRepository.findById(1)).thenReturn(role1);
        when(roleRepository.findById(2)).thenReturn(role2);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("pass");
        User user = new User(0, request.getEmail(), "pass", false, false, roles);
        UserDetails userDetails = new UserDetails(0, user, request.getFirstName(), request.getLastName(), request.getGender(), request.getDob(), request.getApartment());
        when(userRepository.save(user)).thenReturn(user);
        when(userDetailsRepository.save(userDetails)).thenReturn(userDetails);
        UserDetails response = userService.createUser(request);
        assertEquals(response.getUser(), user);
    }

    @Test
    void createUserGK(){
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "M", Date.valueOf("2001-01-01"), null);
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        Role role2 = new Role(2, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email1@e.com", "pass", true, true, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "lastName", "m", Date.valueOf("2002-01-01"), "a101");
        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(1)).thenReturn(role1);
        when(roleRepository.findById(2)).thenReturn(role2);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("pass");
        User user = new User(0, request.getEmail(), "pass", false, false, roles);
        UserDetails userDetails = new UserDetails(0, user, request.getFirstName(), request.getLastName(), request.getGender(), request.getDob(), request.getApartment());
        when(userRepository.save(user)).thenReturn(user);
        when(userDetailsRepository.save(userDetails)).thenReturn(userDetails);
        UserDetails response = userService.createUser(request);
        assertEquals(response.getUser(), user);
    }
}
