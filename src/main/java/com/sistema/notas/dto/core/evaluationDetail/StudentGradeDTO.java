package com.sistema.notas.dto.core.evaluationDetail;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StudentGradeDTO(
        Integer evaluationDetailId,
        @NotNull Integer studentId,
        @NotNull @Min(0) @Max(10) Double grade,
        String feedback
) {}