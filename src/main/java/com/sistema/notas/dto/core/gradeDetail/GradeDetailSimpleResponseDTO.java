package com.sistema.notas.dto.core.gradeDetail;

import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailSimpleResponseDTO(
    Integer id,
    StatusEnum status,
    String fullName,
    String sectionName,
    String degreeName,
    String tutorName
) {
}
