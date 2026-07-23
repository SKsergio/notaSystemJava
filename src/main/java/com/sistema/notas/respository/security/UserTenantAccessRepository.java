package com.sistema.notas.respository.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.notas.entity.security.UserTenantAccess;

public interface UserTenantAccessRepository extends JpaRepository<UserTenantAccess, Integer> {
    List<UserTenantAccess> findByUserId(Integer userId);//obtener todas las membresias

    Optional<UserTenantAccess> findByUserIdAndTenantId(Integer userId, Integer tenantId);//obtener una membresia especifica

}
