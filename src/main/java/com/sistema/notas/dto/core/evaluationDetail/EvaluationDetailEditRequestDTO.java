package com.sistema.notas.dto.core.evaluationDetail;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EvaluationDetailEditRequestDTO(
    Integer id,
    @NotNull @Min(0) @Max(10) Double grade,
    String feedback
) {
    
}
