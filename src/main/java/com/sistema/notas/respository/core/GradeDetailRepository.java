package com.sistema.notas.respository.core;

import com.sistema.notas.entity.core.GradeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface GradeDetailRepository extends JpaRepository<GradeDetail, Integer>, JpaSpecificationExecutor<GradeDetail> {

    @Query("""
            SELECT COUNT(gd) > 0 FROM GradeDetail gd
            WHERE gd.degree.id = :degreeId
              AND gd.section.id = :sectionId
              AND gd.startDate <= :endDate
              AND gd.enDate >= :startDate
            """)
    boolean existsOverlappingGradeDetail(
            @Param("degreeId") Integer degreeId,
            @Param("sectionId") Integer sectionId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT COUNT(gd) > 0 FROM GradeDetail gd
            WHERE gd.degree.id = :degreeId
              AND gd.section.id = :sectionId
              AND gd.startDate <= :endDate
              AND gd.enDate >= :startDate
              AND gd.id != :gradeDetailId
            """)
    boolean existsOverlappingGradeDetailForUpdate(
            @Param("degreeId") Integer degreeId,
            @Param("sectionId") Integer sectionId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("gradeDetailId") Integer gradeDetailId);

    boolean existsByDegreeId(Integer degreeId);
    boolean existsBySectionId(Integer sectionId);
}
