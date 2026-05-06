package com.sistema.notas.respository.core;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.notas.entity.core.CourseRegistration;
import com.sistema.notas.entity.enums.EnrollmentStatus;

public interface CourseRegistrationRepository
                extends JpaRepository<CourseRegistration, Integer>, JpaSpecificationExecutor<CourseRegistration> {

        @Query("SELECT COUNT(cr) > 0 FROM CourseRegistration cr WHERE cr.course.id = :courseId AND cr.student.id = :studentId")
        boolean isEnrollmentDuplicated(
                        @Param("courseId") Integer courseId,
                        @Param("studentId") Integer studentId);

        @Modifying
        @Query("UPDATE CourseRegistration cr SET cr.status = :newStatus WHERE cr.course.id = :courseId AND cr.status = :oldStatus")
        void updateStatusByCourseId(
                        @Param("courseId") Integer courseId,
                        @Param("oldStatus") EnrollmentStatus oldStatus,
                        @Param("newStatus") EnrollmentStatus newStatus);

        boolean existsByStudentIdAndCourseIdAndStatus(Integer studentId, Integer courseId, EnrollmentStatus status);

        @Query("SELECT cr FROM CourseRegistration cr JOIN FETCH cr.course c WHERE cr.student.id = :studentId AND c.gradeDetail.id = :gradeDetailId")
        List<CourseRegistration> findRegistrationsStudents(
                        @Param("studentId") Integer studentId,
                        @Param("gradeDetailId") Integer gradeDetailId);

        Integer countByCourseIdAndStatus(Integer courseId, EnrollmentStatus status);
        Page<CourseRegistration> findByCourseId(Integer courseId, Pageable pageable);

}
