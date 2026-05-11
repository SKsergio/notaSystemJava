package com.sistema.notas.dto.core.evaluationDetail;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BatchEvaluationDetailDTO(
        @NotNull Integer evaluationId,
        @NotEmpty List<StudentGradeDTO> grades // <-- ¡Esto es clave para tener las notas!
) {}
