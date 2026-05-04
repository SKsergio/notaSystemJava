package com.sistema.notas.dto.core.degreeEnrollment;

import com.sistema.notas.entity.enums.EnrollmentStatus;

public record DegreeEnrollmentSimpleResponseDTO(
        Integer id,
        String studentName,
        String DegreeName,
        EnrollmentStatus status
) {
}
