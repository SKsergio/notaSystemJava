package com.sistema.notas.respository.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.notas.dto.security.tenant.UserTenantSummaryDTO;
import com.sistema.notas.entity.security.UserTenantAccess;

public interface UserTenantAccessRepository extends JpaRepository<UserTenantAccess, Integer> {
    List<UserTenantAccess> findByUserId(Integer userId);// obtener todas las membresias

    @Query("""
                SELECT uta
                FROM UserTenantAccess uta
                JOIN FETCH uta.user
                JOIN FETCH uta.role
                JOIN FETCH uta.tenant
                WHERE uta.user.id = :userId
                  AND uta.tenant.id = :tenantId
            """)
    Optional<UserTenantAccess> findByUserIdAndTenantId(
            @Param("userId") Integer userId,
            @Param("tenantId") Integer tenantId);

    Optional<UserTenantAccess> findByInstitutionalPersonIdAndTenantIdAndRoleName(
            Integer institutionalPersonId, Integer tenantId, String roleName);

    @Query("""
                SELECT new com.sistema.notas.dto.security.tenant.UserTenantSummaryDTO(
                    uta.tenant.id,
                    uta.tenant.name,
                    uta.role.name
                )
                FROM UserTenantAccess uta
                WHERE uta.user.id = :userId
            """)
    List<UserTenantSummaryDTO> findTenantsByUserId(@Param("userId") Integer userId);
}
