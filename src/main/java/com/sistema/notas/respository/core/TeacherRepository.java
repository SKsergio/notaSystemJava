package com.sistema.notas.respository.core;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.notas.entity.core.Teacher;


public interface TeacherRepository extends JpaRepository<Teacher, Integer>, JpaSpecificationExecutor<Teacher> {
    @Modifying
    @Query(value = "INSERT INTO teachers (id, specialty) VALUES (:personId, :specialty)", nativeQuery = true)
    void assignTeacherRoleToExistingPerson(@Param("personId") Integer personId, @Param("specialty") String specialty);

}
