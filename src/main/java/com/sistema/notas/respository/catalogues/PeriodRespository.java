package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Period;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PeriodRespository extends JpaRepository<Period, Integer>, JpaSpecificationExecutor<Period> {

    @Query("SELECT COUNT(p) > 0 FROM Period p WHERE p.startDate <= :endDate AND p.endDate >= :startDate")
    boolean existsOverlappingPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) > 0 FROM Period p WHERE p.startDate <= :endDate AND p.endDate >= :startDate AND p.id != :periodId")
    boolean existsOverlappingPeriodForUpdate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("periodId") Integer periodId);

    List<Period> findByStatus(Integer status);

}
