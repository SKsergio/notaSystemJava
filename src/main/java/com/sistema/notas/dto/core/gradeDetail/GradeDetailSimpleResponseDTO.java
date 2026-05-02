package com.sistema.notas.dto.core.gradeDetail;

public record GradeDetailSimpleResponseDTO(
    Integer id,
    String fullName,
    String sectionName,
    String degreeName,
    String tutorName
) {
}
