package com.application.gatekeeper.service;


import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.exception.GateKeeperApplicationException;
import com.application.gatekeeper.repository.RoleRepository;
import com.application.gatekeeper.repository.UserDetailsRepository;
import com.application.gatekeeper.repository.UserRepository;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.application.gatekeeper.constants.ErrorConstants.APARTMENT_ALREADY_OCCUPIED;
import static com.application.gatekeeper.constants.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.INVALID_CREDS;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private UserDetailsRepository userDetailsRepository;
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails createUser(RegisterRequest request) {
        Optional<User> user = userRepository.findUserByEmail(request.getEmail());
        if (! user.isPresent())
            throw new GateKeeperApplicationException(EMAIL_ALREADY_EXISTS);
        UserDetails saveUserDetails = new UserDetails();
        User saveUser = new User();
        if (request.getApartment() != null){
            List<UserDetails> userDetails = userDetailsRepository.findAllByApartmentIgnoreCase(request.getApartment());
            List<Integer> userIds = userDetails.stream()
                    .map(userDetail -> userDetail.getUser().getUserId())
                    .collect(Collectors.toList());
            User activeUserInApartment = userRepository.findByIsActiveTrueAndUserIdIn(userIds);
            if (activeUserInApartment != null)
                throw new GateKeeperApplicationException(APARTMENT_ALREADY_OCCUPIED);
            saveUserDetails.setApartment(request.getApartment());
        }
        List<Role> roleResident = new ArrayList<>();
        List<Role> roleGK = new ArrayList<>();
        Role role = roleRepository.findById(1);
        Role role1 = roleRepository.findById(2);
        roleResident.add(role);
        roleGK.add(role1);
        saveUser.setRoles(roleResident);
        saveUser.setActive(false);
        saveUser.setApproved(false);
        saveUser.setEmail(request.getEmail());
        saveUser.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getApartment() != null)
            saveUser.setRoles(roleGK);
        saveUserDetails.setUserFirstName(request.getFirstName());
        saveUserDetails.setUserLastName(request.getLastName());
        saveUserDetails.setUser(saveUser);
        saveUserDetails.setDob(request.getDob());
        saveUserDetails.setGender(request.getGender());
        userRepository.save(saveUser);
        return userDetailsRepository.save(saveUserDetails);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(INVALID_CREDS));
        var jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .authToken(jwtToken)
                .build();
    }
}
