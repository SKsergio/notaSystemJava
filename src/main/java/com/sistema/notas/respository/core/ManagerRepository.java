package com.sistema.notas.respository.core;

import com.sistema.notas.entity.core.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerRepository extends JpaRepository<Manager, Integer>, JpaSpecificationExecutor<Manager> {
    @Modifying
    @Query(value = "INSERT INTO managers (id) VALUES (:personId)", nativeQuery = true)
    void assignManagerRoleToExistingPerson(@Param("personId") Integer personId);
}
