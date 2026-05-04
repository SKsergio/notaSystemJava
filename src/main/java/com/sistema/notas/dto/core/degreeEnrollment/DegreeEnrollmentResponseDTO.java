package com.sistema.notas.dto.core.degreeEnrollment;

import com.sistema.notas.entity.enums.EnrollmentStatus;

public record DegreeEnrollmentResponseDTO(
        Integer id,
        String studentName,
        String degreeName,
        EnrollmentStatus status
) {
}
