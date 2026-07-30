package com.sistema.notas.dto.core.gradeDetail;

import java.time.LocalDate;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailFullResponseDTO(
    Integer id,
    Integer ability,
    Integer totalStudents, 
    Integer availableSlots,
    Integer year,
    LocalDate startDate,
    LocalDate endDate,
    StatusEnum status,
    CatalogueSimpleResponseDTO section,
    CatalogueSimpleResponseDTO degree,
    TeacherSimpleResponseDTO tutor
) {
    
}
