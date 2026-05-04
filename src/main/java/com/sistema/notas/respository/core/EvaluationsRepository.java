package com.sistema.notas.respository.core;

import java.util.List;

import com.sistema.notas.entity.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sistema.notas.entity.core.Evaluation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationsRepository
        extends JpaRepository<Evaluation, Integer>, JpaSpecificationExecutor<Evaluation> {

    List<Evaluation> findByCourseId(Integer courseId);
    boolean existsByName(String name);
    List<Evaluation> findByStatus(Integer status);

    // evaluaciones de periodos
    @Modifying
    @Query("UPDATE Evaluation e SET e.status = :newState WHERE e.course.id IN (SELECT c.id FROM Course c WHERE c.period.id = :periodId) AND e.status != :newState")
    void updateEvaluationStatusByPeriodId(@Param("periodId") Integer periodId, @Param("newState") StatusEnum newState);

    //evaluaciones desde grados
    @Modifying
    @Query("UPDATE Evaluation e SET e.status = :newState WHERE  e.course.id IN(SELECT c.id FROM Course c WHERE c.gradeDetail.id =:gradeDetailId) AND e.status !=:newState")
    void updateEvaluationStatusByGradeDetailId(@Param("gradeDetailId") Integer gradeDetailId, @Param("newState") StatusEnum newState);

    //evaluaciones desde cursos
    @Modifying
    @Query("UPDATE Evaluation e SET e.status = :newState WHERE e.course.id = :courseId AND e.status != :newState")
    void updateEvaluationStatusByCourseId(@Param("courseId") Integer courseId, @Param("newState") StatusEnum newState);


    @Query("SELECT COALESCE(SUM(e.percentage), 0.0) FROM Evaluation e WHERE e.course.id = :courseId AND (:excludeEvaluationId IS NULL OR e.id != :excludeEvaluationId)")
    Double getAccumulatedPercentage(@Param("courseId") Integer courseId, @Param("excludeEvaluationId") Integer excludeEvaluationId);
}
