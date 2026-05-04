package com.sistema.notas.dto.core.evaluationDetail;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EvaluationDetailRequestDTO(

    @NotNull(message = "El ID del estudiante es obligatorio.")
    @Positive(message = "El ID del estudiante debe ser un número positivo válido.") 
    Integer evaluationId,

    @NotNull(message = "El ID del estudiante es obligatorio.")
    @Positive(message = "El ID del estudiante debe ser un número positivo válido.") 
    Integer studentId,

    @NotNull(message = "La calificación es obligatoria.")
    @Min(0) @Max(10)
    Double grade,

    String feedback//NO ES OBLIGATORIA
    ) {
}
