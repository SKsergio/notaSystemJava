package com.sistema.notas.dto.catalogues;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record PeriodRequestDTO(
        @NotNull(message = "La fecha de inicio no puede venir vacia")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin no puede venir vacia")
        LocalDate endDate,

        @NotNull(message = "El Ciclo Academico es obligatorio.")
        @Positive(message = "El ID del ciclo academico debe ser un número positivo válido.")
        Integer gradeDetailId
)
{
}
