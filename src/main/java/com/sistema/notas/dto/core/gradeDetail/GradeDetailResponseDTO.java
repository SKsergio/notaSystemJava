package com.sistema.notas.dto.core.gradeDetail;

import java.time.LocalDate;

import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailResponseDTO(
        Integer id,
        Integer ability,
        String fullName,
        String code,
        Integer year,
        LocalDate startDate,
        LocalDate endDate,
        StatusEnum status,
        String sectionName,
        String degreeName,
        String tutorName
) {
}
