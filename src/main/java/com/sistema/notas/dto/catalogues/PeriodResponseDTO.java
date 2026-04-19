package com.sistema.notas.dto.catalogues;
import java.time.LocalDateTime;
import java.time.LocalDate;

public record PeriodResponseDTO(
        Integer id,
        String name,
        String code,
        LocalDateTime createdAt,
        LocalDate fromDate,
        LocalDate toDate
) {}