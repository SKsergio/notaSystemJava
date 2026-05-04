package com.sistema.notas.respository.core;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.notas.entity.core.EvaluationDetail;

public interface EvaluationDetailRepository extends JpaRepository<EvaluationDetail, Integer>, Specification<EvaluationDetail> {
    
    boolean existsByStudentIdAndEvaluationId(Integer studentId, Integer evaluationId);

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
