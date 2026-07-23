package com.sistema.notas.respository.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.notas.entity.security.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
