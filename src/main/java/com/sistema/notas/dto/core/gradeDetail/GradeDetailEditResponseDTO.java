package com.sistema.notas.dto.core.gradeDetail;

import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailEditResponseDTO(
    Integer id,
    Integer ability,
    Integer year,
    Integer degreeId,
    StatusEnum status,
    Integer tutorId,
    Integer sectionId
) {
    
}
