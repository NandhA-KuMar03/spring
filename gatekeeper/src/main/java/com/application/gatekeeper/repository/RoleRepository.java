package com.application.gatekeeper.repository;

import com.application.gatekeeper.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int roleId);
}
