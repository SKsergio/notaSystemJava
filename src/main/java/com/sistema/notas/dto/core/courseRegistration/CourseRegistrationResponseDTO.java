package com.sistema.notas.dto.core.courseRegistration;

import com.sistema.notas.entity.enums.EnrollmentStatus;

public record CourseRegistrationResponseDTO(
    Integer id,
    String courseName,
    String studentName,
    Double finalAverage,
    EnrollmentStatus status
) {
} 
