package com.sistema.notas.respository.core;

import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DegreeEnrollmentRepository
        extends JpaRepository<DegreeEnrollment, Integer>, JpaSpecificationExecutor<DegreeEnrollment> {

    @Query("SELECT COUNT(de) > 0 FROM DegreeEnrollment de WHERE de.gradeDetail.id = :gradeDetailId AND de.student.id = :studentId")
    boolean isEnrollmentDuplicated(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("studentId") Integer studentId);

    // verifica que no exista una matricula para el alumno en un rango de fechas
    @Query("SELECT COUNT(de) > 0 FROM DegreeEnrollment de " +
            "WHERE de.student.id = :studentId " +
            "AND de.gradeDetail.startDate <= :endDate " +
            "AND de.gradeDetail.endDate >= :startDate")
    boolean hasOverlappingEnrollment(
            @Param("studentId") Integer studentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 1. Devuelve los IDs de los alumnos que YA están en este grado exacto
    @Query("SELECT de.student.id FROM DegreeEnrollment de WHERE de.gradeDetail.id = :gradeDetailId AND de.student.id IN :studentIds")
    List<Integer> findDuplicatedStudentIdsInGrade(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("studentIds") List<Integer> studentIds);

    // EXPLICACIÓN: Busca el "contrato" principal. Queremos el registro exacto que
    // dice que
    // este Alumno específico pertenece a este Grado específico, y que su matrícula
    // está Activa.
    // Retorna un Optional porque puede que el alumno no esté matriculado ahí.
    @Query("SELECT de FROM DegreeEnrollment de WHERE de.student.id = :studentId AND de.gradeDetail.id = :gradeDetailId AND de.status = :status")
    Optional<DegreeEnrollment> findActiveEnrollment(
            @Param("studentId") Integer studentId,
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("status") EnrollmentStatus status);

    @Query("SELECT de.gradeDetail FROM DegreeEnrollment de " +
            "WHERE de.student.id = :studentId " +
            "AND :date BETWEEN de.gradeDetail.startDate AND de.gradeDetail.endDate " +
            "AND de.status = :status")
    Optional<GradeDetail> findCurrentGradeDetail(
            @Param("studentId") Integer studentId,
            @Param("date") LocalDate date,
            @Param("status") EnrollmentStatus status);

    //
    @Query("SELECT de FROM DegreeEnrollment de " +
            "JOIN FETCH de.gradeDetail gd " +
            "JOIN FETCH gd.degree " +
            "JOIN FETCH gd.section " +
            "WHERE de.student.id IN :studentIds " +
            "AND :date BETWEEN de.gradeDetail.startDate AND de.gradeDetail.endDate " +
            "AND de.status = :status")
    List<DegreeEnrollment> findActiveEnrollmentsByStudentIdsAndDate(
            @Param("studentIds") List<Integer> studentIds,
            @Param("date") LocalDate date,
            @Param("status") EnrollmentStatus status);

    // 2. Devuelve los IDs de los alumnos que YA están matriculados en OTRA sección
    // en ese mismo año
    @Query("SELECT de.student.id FROM DegreeEnrollment de " +
            "WHERE de.gradeDetail.startDate <= :endDate " +
            "AND de.gradeDetail.endDate >= :startDate " +
            "AND de.student.id IN :studentIds")
    List<Integer> findStudentIdsAlreadyEnrolledInPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("studentIds") List<Integer> studentIds);

    @Modifying
    @Query("UPDATE DegreeEnrollment de SET de.status = :newEnrollmentStatus WHERE de.gradeDetail.id = :gradeDetailId AND de.status = :oldEnrollmentStatus")
    void finalizeEnrollmentsByGradeDetail(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("oldEnrollmentStatus") EnrollmentStatus oldEnrollmentStatus,
            @Param("newEnrollmentStatus") EnrollmentStatus newEnrollmentStatus);

    boolean existsByStudentIdAndGradeDetailIdAndStatus(Integer studentId, Integer gradeDetailId,
            EnrollmentStatus status);

    @Query("SELECT de.student.id FROM DegreeEnrollment de WHERE de.gradeDetail.id = :gradeDetailId AND de.student.id IN :studentIds AND de.status = :status")
    List<Integer> findValidEnrolledStudentIds(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("studentIds") List<Integer> studentIds,
            @Param("status") EnrollmentStatus status);

    Integer countByGradeDetailIdAndStatus(Integer gradeDetailId, EnrollmentStatus status);

    Page<DegreeEnrollment> findByGradeDetailId(Integer gradeDetailId, Pageable pageable);

}
