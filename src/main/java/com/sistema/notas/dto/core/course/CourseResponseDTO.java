package com.sistema.notas.dto.core.course;

import com.sistema.notas.entity.enums.StatusEnum;

public record CourseResponseDTO(
    Integer id,
    String name,
    String code,
    String teacherName,
    String gradeDetailName,
    String subjectName,
    Integer totalStudents,
    StatusEnum status,
    int year,
    Double valorityUnity
) {
} 
