package com.sistema.notas.dto.catalogues;

import java.time.LocalDate;

public record PeriodSimpleResponseDto(Integer id, LocalDate startDate, LocalDate endDate) {
}
