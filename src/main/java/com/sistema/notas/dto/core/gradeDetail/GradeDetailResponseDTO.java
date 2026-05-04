package com.sistema.notas.dto.core.gradeDetail;

import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailResponseDTO(
        Integer id,
        Integer ability,
        String fullName,
        Integer year,
        StatusEnum status,
        String sectionName,
        String degreeName,
        String tutorName
) {
}
