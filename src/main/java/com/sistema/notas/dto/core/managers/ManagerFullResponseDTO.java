package com.sistema.notas.dto.core.managers;

import com.sistema.notas.dto.core.student.AssignedStudentDTO;
import com.sistema.notas.entity.enums.GenderEnum;

import java.time.LocalDate;
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
        GenderEnum gender,
        LocalDate birthDate,
        List<AssignedStudentDTO> assignedStudents
) {
}
