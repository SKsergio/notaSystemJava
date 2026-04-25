package com.sistema.notas.dto.core.teacher;

import java.time.LocalDateTime;
import java.util.List;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;

public record TeacherFullResponseDTO(
        Integer id,
        String fullName,
        String address,
        String email,
        String phoneNumber,
        String dui,
        int age,
        String routePhoto,
        String speciality,
        LocalDateTime createdAt,
        List<GradeDetailAssignedDTO> assignedGrades,
        List<CourseSimpleResponseDTO> assignedCourses
) {}