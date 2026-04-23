package com.sistema.notas.dto.core.course;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.PeriodSimpleResponseDto;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;

public record CourseFullResponseDTO(
    Integer id,
    String name,
    String code,
    TeacherSimpleResponseDTO teacher,
    GradeDetailSimpleResponseDTO gradeDetail,
    CatalogueSimpleResponseDTO subject,
    PeriodSimpleResponseDto period,
    Integer totalStudents,
    Integer status,
    int year,
    Double valorityUnity
) {
    
}
