package com.sistema.notas.dto.core.course;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.entity.enums.StatusEnum;

public record CourseFullResponseDTO(
    Integer id,
    String name,
    String code,
    TeacherSimpleResponseDTO teacher,
    GradeDetailSimpleResponseDTO gradeDetail,
    CatalogueSimpleResponseDTO subject,
    Integer totalStudents,
    Integer availableSlots,
    Double evaluatedPercentage,
    StatusEnum status,
    int year,
    Double valorityUnity
) {
    
}
