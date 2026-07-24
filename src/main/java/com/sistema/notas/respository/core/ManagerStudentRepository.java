package com.sistema.notas.respository.core;


import com.sistema.notas.entity.core.ManagerStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerStudentRepository extends JpaRepository<ManagerStudents, Integer>,
        JpaSpecificationExecutor<ManagerStudents> {

    //validar duplicados
    @Query("SELECT COUNT(mgs) > 0 FROM ManagerStudents mgs " +
            "WHERE mgs.manager.id = :managerId " +
            "AND mgs.student.id = :studentId " +
            "AND (:id IS NULL OR mgs.id != :id)")
    boolean isManagerStudentDuplicated(
            @Param("managerId") Integer managerId,
            @Param("studentId") Integer studentId,
            @Param("id") Integer id);
}
