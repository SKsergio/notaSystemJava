package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Period;

import com.sistema.notas.entity.catalogues.Section;
import com.sistema.notas.entity.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PeriodRespository extends JpaRepository<Period, Integer>, JpaSpecificationExecutor<Period> {

    @Query("SELECT COUNT(p) > 0 FROM Period p WHERE p.startDate <= :endDate AND p.endDate >= :startDate AND p.gradeDetail.id = :gradeDetailId")
    boolean existsOverlappingPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("gradeDetailId") Integer gradeDetailId
            );

    @Query("SELECT COUNT(p) > 0 FROM Period p WHERE p.startDate <= :endDate AND p.endDate >= :startDate AND p.id != :periodId AND p.gradeDetail.id = :gradeDetailId")
    boolean existsOverlappingPeriodForUpdate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("periodId") Integer periodId,
            @Param("gradeDetailId") Integer gradeDetailId
            );

    @Modifying
    @Query("UPDATE Period p SET p.status = :newState WHERE p.gradeDetail.id = :gradeDetailId AND p.status != :newState")
    void updatePeriodStatusByGradeDetailId(@Param("gradeDetailId") Integer gradeDetailId, @Param("newState") StatusEnum newState);


    List<Period> findByGradeDetailId(Integer gradeDetailId);

    List<Period> findByStatus(Integer status);

}
