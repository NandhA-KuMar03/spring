package com.application.gatekeeper.repository;

import com.application.gatekeeper.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

    Optional<Visitor> findByEmail(String email);

}
