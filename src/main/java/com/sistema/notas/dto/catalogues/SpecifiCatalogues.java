package com.sistema.notas.dto.catalogues;

import java.time.LocalDateTime;
import java.time.LocalDate;
public class SpecifiCatalogues {

    public record SubjectResponseDTO(
            Integer id,
            String name,
            String code,
            LocalDateTime createdAt,
            String description
    ) {}

    public record PeriodResponseDTO(
            Integer id,
            String name,
            String code,
            LocalDateTime createdAt,
            LocalDate fromDate,
            LocalDate toDate
    ) {}
}
