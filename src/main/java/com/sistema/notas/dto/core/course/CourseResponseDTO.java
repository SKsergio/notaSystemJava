package com.sistema.notas.dto.core.course;

public record CourseResponseDTO(
    Integer id,
    String name,
    String code,
    String teacherName,
    String gradeDetailName,
    String subjectName,
    Integer totalStudents,
    Integer status,
    int year,
    Double valorityUnity
) {
} 
