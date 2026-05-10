package com.sistema.notas.respository.core;

import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.enums.EnrollmentStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DegreeEnrollmentRepository extends JpaRepository<DegreeEnrollment, Integer>, JpaSpecificationExecutor<DegreeEnrollment> {

    @Query("SELECT COUNT(de) > 0 FROM DegreeEnrollment de WHERE de.gradeDetail.id = :gradeDetailId AND de.student.id = :studentId")
    boolean isEnrollmentDuplicated(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("studentId") Integer studentId
    );


    @Query("SELECT COUNT(de) > 0 FROM DegreeEnrollment de WHERE de.student.id = :studentId AND de.gradeDetail.year = :year")
    boolean hasEnrollmentInYear(
            @Param("studentId") Integer studentId,
            @Param("year") Integer year
    );

    // 1. Devuelve los IDs de los alumnos que YA están en este grado exacto
    @Query("SELECT de.student.id FROM DegreeEnrollment de WHERE de.gradeDetail.id = :gradeDetailId AND de.student.id IN :studentIds")
    List<Integer> findDuplicatedStudentIdsInGrade(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("studentIds") List<Integer> studentIds
    );

    // 2. Devuelve los IDs de los alumnos que YA están matriculados en OTRA sección en ese mismo año
    @Query("SELECT de.student.id FROM DegreeEnrollment de WHERE de.gradeDetail.year = :year AND de.student.id IN :studentIds")
    List<Integer> findStudentIdsAlreadyEnrolledInYear(
            @Param("year") Integer year,
            @Param("studentIds") List<Integer> studentIds
    );

    @Modifying
    @Query("UPDATE DegreeEnrollment de SET de.status = :newEnrollmentStatus WHERE de.gradeDetail.id = :gradeDetailId AND de.status = :oldEnrollmentStatus")
    void finalizeEnrollmentsByGradeDetail(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("oldEnrollmentStatus") EnrollmentStatus oldEnrollmentStatus,
            @Param("newEnrollmentStatus") EnrollmentStatus newEnrollmentStatus
    );

    boolean existsByStudentIdAndGradeDetailIdAndStatus(Integer studentId, Integer gradeDetailId, EnrollmentStatus status);

    @Query("SELECT de.student.id FROM DegreeEnrollment de WHERE de.gradeDetail.id = :gradeDetailId AND de.student.id IN :studentIds AND de.status = :status")
    List<Integer> findValidEnrolledStudentIds(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("studentIds") List<Integer> studentIds,
            @Param("status") EnrollmentStatus status
    );

    Integer countByGradeDetailIdAndStatus(Integer gradeDetailId, EnrollmentStatus status);
    Page<DegreeEnrollment> findByGradeDetailId(Integer gradeDetailId, Pageable pageable);

}
