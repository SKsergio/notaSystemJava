package com.sistema.notas.respository.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sistema.notas.entity.core.Evaluation;

public interface EvaluationsRepository
        extends JpaRepository<Evaluation, Integer>, JpaSpecificationExecutor<Evaluation> {

    List<Evaluation> findByCourseId(Integer courseId);

    boolean existsByName(String name);

    List<Evaluation> findByStatus(Integer status);
}
