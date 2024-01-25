package com.application.gatekeeper.repository;

import com.application.gatekeeper.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {
    Optional<UserDetails> findByUserUserId(int userId);
    UserDetails findByApartmentIgnoreCase(String apartment);
    List<UserDetails> findAllByApartmentIgnoreCase(String apartment);

}
