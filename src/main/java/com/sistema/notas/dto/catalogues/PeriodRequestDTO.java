package com.sistema.notas.dto.catalogues;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PeriodRequestDTO(
        @NotNull(message = "La fecha de inicio no puede venir vacia")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin no puede venir vacia")
        LocalDate endDate
)
{
}
