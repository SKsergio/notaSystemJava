package com.sistema.notas.dto.core.evaluations;

import java.time.LocalDate;

public record EvaluationEditResponse(
        Integer id,
        String name,
        String description,
        Double percentage,
        LocalDate startDate,
        LocalDate endDate,
        Integer courseId
) {
}
