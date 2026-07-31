package com.sistema.notas.dto.catalogues;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sistema.notas.entity.enums.StatusEnum;

public record PeriodResponseEditDto(
     Integer id,
        LocalDateTime createdAt,
        StatusEnum status,
        LocalDate startDate,
        LocalDate endDate,
        Integer gradeDetailId
) {
    
}
