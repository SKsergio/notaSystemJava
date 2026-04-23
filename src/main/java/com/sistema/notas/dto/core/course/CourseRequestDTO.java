package com.sistema.notas.dto.core.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CourseRequestDTO(
        @JsonProperty("valorityUnity")
        @NotNull(message = "La unidad Valorativa obligatoria") 
        Double valorityUnity,

        @NotNull(message = "El Detalle de grado es obligatorio.")
        @Positive(message = "El ID del Detalle Grado debe ser un número positivo válido.")
        Integer gradeDetailId,

        @NotNull(message = "El Periodo es obligatorio.")
        @Positive(message = "El ID del Periodo debe ser un número positivo válido.")
        Integer periodId,

        @NotNull(message = "El maestro asignado es obligatorio.")
        @Positive(message = "El ID del Docente debe ser un número positivo válido.")
        Integer teacherId,

        @NotNull(message = "La Materia asignada al curso es obligatoria.")
        @Positive(message = "El ID de la Materia debe ser un número positivo válido.")
        Integer subjectId
) {
}
