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

        // 1. Devuelve los IDs de los alumnos que YA están en este cursi exacto
        @Query("SELECT co.student.id FROM CourseRegistration co WHERE co.course.id = :courseId AND co.student.id IN :studentIds")
        List<Integer> findDuplicatedStudentIdsInCourse(
                @Param("courseId") Integer courseId,
                @Param("studentIds") List<Integer> studentIds
        );

        // Devuelve los IDs de los estudiantes que SÍ tienen una inscripción activa en el curso
        @Query("SELECT cr.student.id FROM CourseRegistration cr " +
                "WHERE cr.course.id = :courseId " +
                "AND cr.student.id IN :studentIds " +
                "AND cr.status = :status")
        List<Integer> findEnrolledStudentIds(
                @Param("courseId") Integer courseId,
                @Param("studentIds") List<Integer> studentIds,
                @Param("status") EnrollmentStatus status
        );

        // EXPLICACIÓN: Busca las materias. Pero no cualquier materia.
        // Filtra las inscripciones Activas de este Alumno, PERO cruza la tabla (JOIN) con Course
        // para asegurarse de que esos cursos realmente pertenecen al Grado que estamos consultando.
        // El "JOIN FETCH" es un truco Senior para traerte los datos del curso de un solo golpe y evitar el error N+1.
        @Query("SELECT cr FROM CourseRegistration cr JOIN FETCH cr.course c WHERE cr.student.id = :studentId AND c.gradeDetail.id = :gradeDetailId AND cr.status = :status")
        List<CourseRegistration> findActiveCoursesByStudentAndGrade(
                @Param("studentId") Integer studentId,
                @Param("gradeDetailId") Integer gradeDetailId,
                @Param("status") EnrollmentStatus status
        );

        Page<CourseRegistration> findByCourseIdAndStatus(Integer courseId, EnrollmentStatus status, Pageable pageable);

        boolean existsByStudentIdAndCourseIdAndStatus(Integer studentId, Integer courseId, EnrollmentStatus status);

        @Query("SELECT cr FROM CourseRegistration cr JOIN FETCH cr.course c WHERE cr.student.id = :studentId AND c.gradeDetail.id = :gradeDetailId")
        List<CourseRegistration> findRegistrationsStudents(
                        @Param("studentId") Integer studentId,
                        @Param("gradeDetailId") Integer gradeDetailId);

        Integer countByCourseIdAndStatus(Integer courseId, EnrollmentStatus status);
        Page<CourseRegistration> findByCourseId(Integer courseId, Pageable pageable);

}
