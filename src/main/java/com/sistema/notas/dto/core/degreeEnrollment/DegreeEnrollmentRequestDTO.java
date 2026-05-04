package com.sistema.notas.dto.core.degreeEnrollment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DegreeEnrollmentRequestDTO(
        @NotNull(message = "El Grado es obligatorio.")
        @Positive(message = "El ID del detalle de Grado debe ser un número positivo válido.")
        Integer gradeDetailId,

        @NotNull(message = "El estudiante asignado es obligatorio.")
        @Positive(message = "El ID del estudiante debe ser un número positivo válido.")
        Integer studentId
) {

}
