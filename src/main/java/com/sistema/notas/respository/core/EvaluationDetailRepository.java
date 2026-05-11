package com.sistema.notas.respository.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.notas.entity.core.EvaluationDetail;

public interface EvaluationDetailRepository extends JpaRepository<EvaluationDetail, Integer>, JpaSpecificationExecutor<EvaluationDetail> {
    
    boolean existsByStudentIdAndEvaluationId(Integer studentId, Integer evaluationId);

    // 1. Devuelve los IDs de los alumnos que YA están en este cursi exacto
    @Query("SELECT ed.student.id FROM EvaluationDetail ed " + "WHERE ed.evaluation.id = :evaluationId " + "AND ed.student.id IN :studentIds")
    List<Integer> findDuplicatedStudentIdsInEvaluation(
            @Param("evaluationId") Integer evaluationId,
            @Param("studentIds") List<Integer> studentIds
    );

    // Spring sabe que buscará por "evaluation.id"
    List<EvaluationDetail> findByEvaluationId(Integer evaluationId);

    @Query("SELECT ed FROM EvaluationDetail ed JOIN FETCH ed.evaluation e WHERE ed.student.id = :studentId AND e.course.id = :courseId")
    List<EvaluationDetail> findByStudentIdAndCourseId(
            @Param("studentId") Integer studentId,
            @Param("courseId") Integer courseId
    );

    //TODAS LAS NOTAS DE UNA EVALUACION ESPECIFICA
    @Query("SELECT ed FROM EvaluationDetail ed JOIN FETCH ed.student WHERE ed.evaluation.id =:evaluationId")
    List<EvaluationDetail> findByEvaluationIdWithStudent(@Param("evaluationId") Integer evaluationId);

    //TODAS LAS NOTAS DE UN ESTUDIANTE ESPECIFICO EN UN CURSO ESPECIFICO
    @Query("SELECT ed FROM EvaluationDetail ed JOIN FETCH ed.evaluation e WHERE ed.student.id = :studentId AND e.course.id = :courseId")
    List<EvaluationDetail> findByStudentAndCourse(
            @Param("studentId") Integer studentId, 
            @Param("courseId") Integer courseId
    );

    //CALCULAR POMEDIO DE UN ESTUDIANTE PARA UN CURSO ESPECIFICO
    @Query("SELECT COALESCE(SUM(ed.grade * (e.percentage / 100.0)), 0.0) " +
           "FROM EvaluationDetail ed JOIN ed.evaluation e " +
           "WHERE ed.student.id = :studentId AND e.course.id = :courseId")
    Double calculateCurrentAverageByStudentAndCourse(
            @Param("studentId") Integer studentId, 
            @Param("courseId") Integer courseId
    );

}
