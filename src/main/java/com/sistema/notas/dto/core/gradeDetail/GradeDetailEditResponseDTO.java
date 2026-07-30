package com.sistema.notas.dto.core.gradeDetail;

import java.time.LocalDate;

import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailEditResponseDTO(
    Integer id,
    Integer ability,
    LocalDate startDate,
    LocalDate endDate,
    Integer degreeId,
    StatusEnum status,
    Integer tutorId,
    Integer sectionId
) {
    
}
