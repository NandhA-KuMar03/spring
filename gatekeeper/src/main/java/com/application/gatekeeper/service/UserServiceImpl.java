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

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.application.gatekeeper.constants.ErrorConstants.ABOVE_18_AGE;
import static com.application.gatekeeper.constants.ErrorConstants.APARTMENT_ALREADY_OCCUPIED;
import static com.application.gatekeeper.constants.ErrorConstants.EMAIL_ALREADY_EXISTS;
import static com.application.gatekeeper.constants.ErrorConstants.GENDER_ERROR;
import static com.application.gatekeeper.constants.ErrorConstants.INVALID_CREDS;
import static com.application.gatekeeper.constants.ErrorConstants.USER_NOT_APPROVED;

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
        if (! user.isEmpty())
            throw new GateKeeperApplicationException(EMAIL_ALREADY_EXISTS);
        LocalDate dob = request.getDob().toLocalDate();
        LocalDate now = LocalDate.now();
        int years = Period.between(dob,now).getYears();
        if(years<18)
            throw new GateKeeperApplicationException(ABOVE_18_AGE);
        if(! (request.getGender().equalsIgnoreCase("m") || request.getGender().equalsIgnoreCase("f") || request.getGender().equalsIgnoreCase("o")))
            throw new GateKeeperApplicationException(GENDER_ERROR);
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
        if (request.getApartment() == null)
            saveUser.setRoles(roleGK);
        else
            saveUser.setRoles(roleResident);
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
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (Exception e){
            throw new GateKeeperApplicationException(INVALID_CREDS);
        }
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(INVALID_CREDS));
        if (! user.isApproved())
            throw new GateKeeperApplicationException(USER_NOT_APPROVED);
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken);
        return authenticationResponse;
    }
}
