package com.sistema.notas.dto.core.course;

public record CourseFullResponseDTO(
    Integer id,
    String name,
    String code,
    String teacherName,
    String gradeDetailName,
    String subjectName,
    Integer totalStudents,
    int year,
    Integer valoraty_unity
) {
    
}
