package com.sistema.notas.dto.core.teacher;

import java.util.List;

import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;

public record TeacherFullResponseDTO(
        Integer id,
        String firstName,
        String firstLastName,
        String email,
        String dui,
        String age,
        List<GradeDetailAssignedDTO> assignedGrades 
) {}