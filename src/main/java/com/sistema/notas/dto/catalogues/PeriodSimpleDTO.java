package com.sistema.notas.dto.catalogues;

import java.time.LocalDate;

public record PeriodSimpleDTO(Integer id, LocalDate periodStart, LocalDate periodEnd) {
}
