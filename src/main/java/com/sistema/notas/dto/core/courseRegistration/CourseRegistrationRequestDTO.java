package com.sistema.notas.dto.core.courseRegistration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseRegistrationRequestDTO(
        @NotNull(message = "El Curso es obligatorio.")
        @Positive(message = "El ID del Curso debe ser un número positivo válido.") 
        Integer courseId,

        @NotNull(message = "El estudiante asignado es obligatorio.")
        @Positive(message = "El ID del estudiante debe ser un número positivo válido.") 
        Integer studentId) {
}
