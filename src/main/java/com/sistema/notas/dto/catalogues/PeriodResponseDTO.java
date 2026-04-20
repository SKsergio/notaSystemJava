package com.sistema.notas.dto.catalogues;
import java.time.LocalDateTime;
import java.time.LocalDate;

public record PeriodResponseDTO(
        Integer id,
        LocalDateTime createdAt,
        LocalDate startDate,
        LocalDate endDate
) {}