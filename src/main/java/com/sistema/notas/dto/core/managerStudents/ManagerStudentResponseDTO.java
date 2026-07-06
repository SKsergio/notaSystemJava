package com.sistema.notas.dto.core.managerStudents;

public record ManagerStudentResponseDTO(
        Integer id,
        String studentName,
        String managerName,
        String relationType
) {
}
