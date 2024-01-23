package com.application.gatekeeper.service;

import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.exception.GateKeeperApplicationException;
import com.application.gatekeeper.repository.UserDetailsRepository;
import com.application.gatekeeper.repository.UserRepository;
import com.application.gatekeeper.request.EditInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static com.application.gatekeeper.constants.ErrorConstants.APARTMENT_ALREADY_OCCUPIED;
import static com.application.gatekeeper.constants.ErrorConstants.NO_SUCH_USER;
import static com.application.gatekeeper.constants.ErrorConstants.USER_ALREADY_APPROVED;

@Service
public class AdminServiceImpl implements AdminService{

    private UserRepository userRepository;
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    UserDetails getUserDetails(int userId){
        Optional<UserDetails> userDetails = userDetailsRepository.findByUserUserId(userId);
        if (! userDetails.isPresent())
            throw new GateKeeperApplicationException(NO_SUCH_USER);
        return userDetails.get();
    }

    User getUser(int userId){
        Optional<User> user = userRepository.findById(userId);
        if (! user.isPresent())
            throw new GateKeeperApplicationException(NO_SUCH_USER);
        return user.get();
    }

    @Override
    public List<User> getAwaitingApprovals() {
        return userRepository.findAllUserByIsActiveTrue();
    }


    @Override
    public UserDetails getUserAwaitingApproval(int userId) {
        return getUserDetails(userId);
    }

    @Override
    public User approveUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (! user.isPresent())
            throw new GateKeeperApplicationException(NO_SUCH_USER);
        if (user.get().isApproved())
            throw new GateKeeperApplicationException(USER_ALREADY_APPROVED);
        user.get().setApproved(true);
        userRepository.save(user.get());
        return user.get();
    }

    @Override
    public UserDetails editInfo(EditInfoRequest request) {
        int userId = request.getUserId();
        UserDetails userDetails = getUserDetails(userId);
        if (request.getFirstName() != null)
            userDetails.setUserFirstName(request.getFirstName());
        if (request.getLastName() != null)
            userDetails.setUserLastName(request.getLastName());
        if (request.getDob() != null)
            userDetails.setDob(request.getDob());
        if (request.getApartment() != null){
            UserDetails userDetailsByApartment = userDetailsRepository.findByApartmentIgnoreCase(request.getApartment());
            if (userDetailsByApartment != null)
                throw new GateKeeperApplicationException(APARTMENT_ALREADY_OCCUPIED);
            userDetails.setApartment(request.getApartment());
        }
        userDetailsRepository.save(userDetails);
        return userDetails;
    }

    @Override
    public void removeUser(int userId) {
        User user = getUser(userId);
        user.setActive(false);
        userRepository.save(user);
    }
}
