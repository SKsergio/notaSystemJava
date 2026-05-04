package com.sistema.notas.dto.core.gradeDetail;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.entity.enums.StatusEnum;

public record GradeDetailFullResponseDTO(
    Integer id,
    Integer ability,
    Integer year,
    StatusEnum status,
    CatalogueSimpleResponseDTO section,
    CatalogueSimpleResponseDTO degree,
    TeacherSimpleResponseDTO tutor
) {
    
}
