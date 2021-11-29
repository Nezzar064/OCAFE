package com.nezzar064.ocafe.repository;

import com.nezzar064.ocafe.model.UserRole;
import com.nezzar064.ocafe.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByUserRole(UserRole name);

}
