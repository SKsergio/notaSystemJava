package com.sistema.notas.dto.core.evaluations;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record EvaluationRequestDTO(
    @NotBlank(message = "El nombre de la evaluación es obligatorio y no puede estar vacío.")
    @Size(max = 50, message = "El nombre de la evaluación no puede tener más de 50 caracteres.")
    String name,

    @NotBlank(message = "La descripción de la evaluación es obligatoria y no puede estar vacía.")
    @Size(max = 255, message = "La descripción de la evaluación no puede tener más de 255 caracteres.")
    String description,

    @NotNull(message = "El porcentaje de la evaluación es obligatorio.")
    Double percentage,

    @NotNull(message = "La fecha de apertura es obligatoria.")
    LocalDate startDate,

    @NotNull(message = "La fecha de cierre es obligatoria.")
    LocalDate endDate,

    @NotNull(message = "El ID del curso es obligatorio.")
    @Positive(message = "El ID del curso debe ser un número positivo válido.")
    Integer courseId
) {
    
}
