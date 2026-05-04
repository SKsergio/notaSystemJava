package com.sistema.notas.dto.catalogues;
import com.sistema.notas.entity.enums.StatusEnum;

import java.time.LocalDateTime;
import java.time.LocalDate;

public record PeriodResponseDTO(
        Integer id,
        LocalDateTime createdAt,
        StatusEnum status,
        LocalDate startDate,
        LocalDate endDate
) {}