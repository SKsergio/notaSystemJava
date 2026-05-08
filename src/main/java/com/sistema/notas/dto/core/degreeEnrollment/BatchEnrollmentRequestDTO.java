package com.sistema.notas.dto.core.degreeEnrollment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BatchEnrollmentRequestDTO(
    @NotNull Integer gradeDetailId,
    @NotEmpty List<Integer> studentIds
) {}