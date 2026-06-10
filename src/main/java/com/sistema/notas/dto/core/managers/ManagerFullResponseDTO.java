package com.sistema.notas.dto.core.managers;

import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ManagerFullResponseDTO(
        Integer id,
        String fullName,
        String address,
        String email,
        String phoneNumber,
        String dui,
        int age,
        String routePhoto,
        LocalDateTime createdAt,
        List<StudentSimpleResponseDTO> assignedStudents
) {
}
