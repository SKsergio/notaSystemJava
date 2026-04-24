package com.sistema.notas.dto.core.teacher;

import java.util.List;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;

public record TeacherFullResponseDTO(
        Integer id,
        // String firstName,
        String fullName,
        String email,
        String dui,
        int age,
        String routePhoto,
        List<GradeDetailAssignedDTO> assignedGrades,
        List<CourseSimpleResponseDTO> assignedCourses
) {}