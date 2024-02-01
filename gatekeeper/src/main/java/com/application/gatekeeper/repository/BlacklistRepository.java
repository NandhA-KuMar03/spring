package com.application.gatekeeper.repository;

import com.application.gatekeeper.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {

    List<Blacklist> findAllByVisitorEmail(String email);
    Optional<Blacklist> findByVisitorVisitorIdAndUserUserId(int visitorId, int userId);

}
