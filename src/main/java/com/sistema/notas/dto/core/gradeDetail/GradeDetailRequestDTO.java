package com.sistema.notas.dto.core.gradeDetail;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GradeDetailRequestDTO (
        @NotNull(message = "La disponibilidad/capacidad es obligatoria.")
        @Min(value = 1, message = "La capacidad mínima debe ser de al menos 1 alumno.")
        Integer ability,

        @NotNull(message = "La fecha de inicio no puede venir vacia")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin no puede venir vacia")
        LocalDate endDate,

        @NotNull(message = "El Grado es obligatorio.")
        @Positive(message = "El ID del Grado debe ser un número positivo válido.")
        Integer degreeId,

        @NotNull(message = "El Tutor asignado es obligatorio.")
        @Positive(message = "El ID del Docente debe ser un número positivo válido.")
        Integer tutorId,

        @NotNull(message = "La Sección es obligatoria.")
        @Positive(message = "El ID de la Sección debe ser un número positivo válido.")
        Integer sectionId
)
{
}
