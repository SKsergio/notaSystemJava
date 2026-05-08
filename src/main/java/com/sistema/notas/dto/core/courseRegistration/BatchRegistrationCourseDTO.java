package com.sistema.notas.dto.core.courseRegistration;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BatchRegistrationCourseDTO(
    @NotNull Integer courseId,
    @NotEmpty List<Integer> studentIds
) {
} 

