package com.sistema.notas.respository.core;

import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.enums.EnrollmentStatus;

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

    @Modifying
    @Query("UPDATE DegreeEnrollment de SET de.status = :newEnrollmentStatus WHERE de.gradeDetail.id = :gradeDetailId AND de.status = :oldEnrollmentStatus")
    void finalizeEnrollmentsByGradeDetail(
            @Param("gradeDetailId") Integer gradeDetailId,
            @Param("oldEnrollmentStatus") EnrollmentStatus oldEnrollmentStatus,
            @Param("newEnrollmentStatus") EnrollmentStatus newEnrollmentStatus
    );

    Integer countByGradeDetailIdAndStatus(Integer gradeDetailId, EnrollmentStatus status);
    Page<DegreeEnrollment> findByGradeDetailId(Integer gradeDetailId, Pageable pageable);

}
