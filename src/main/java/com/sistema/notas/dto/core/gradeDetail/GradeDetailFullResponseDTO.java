package com.sistema.notas.dto.core.gradeDetail;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;

public record GradeDetailFullResponseDTO(
    Integer id,
    Integer ability,
    Integer year,
    CatalogueSimpleResponseDTO section,
    CatalogueSimpleResponseDTO degree,
    TeacherSimpleResponseDTO tutor
) {
    
}
