package com.sistema.notas.dto.core.gradeDetail;

public record GradeDetailResponseDTO(
        Integer id,
        Integer ability,
        Integer year,
        String sectionName,
        String degreeName,
        String tutorName
) {
}
