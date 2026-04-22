package com.sistema.notas.dto.core.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourseRequestDTO(
        @NotBlank(message = "El nombre del curso es obligatorio") 
        @Size(max = 50, message = "El nombre del curso es demasiado largo") 
        String name,

        @NotNull(message = "La unidad Valorativa obligatoria") 
        Integer valoraty_unity,

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
