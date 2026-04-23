package com.sistema.notas.dto.core.evaluations;

import java.time.LocalDate;

public record EvaluationsResponseDTO(
    Integer id,
    String name,
    String description,
    String courseName,
    Double percentage,
    LocalDate endDate,
    Integer status,
    Long daysRemaning
) {
    
}
