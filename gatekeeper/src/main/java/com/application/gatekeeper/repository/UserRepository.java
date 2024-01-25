package com.application.gatekeeper.repository;

import com.application.gatekeeper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllUserByIsActiveTrue();
    Optional<User> findUserByEmail(String email);
    User findByIsActiveTrueAndUserIdIn(List<Integer> ids);

}