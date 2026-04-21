package com.sistema.notas.respository.core;

import com.sistema.notas.entity.core.GradeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GradeDetailRepository extends JpaRepository<GradeDetail, Integer>, JpaSpecificationExecutor<GradeDetail> {
    boolean existsByDegreeIdAndSectionId(Integer degreeId, Integer sectionId);
}
