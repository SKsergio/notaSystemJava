package com.sistema.notas.dto.core.managerStudents;

public record AssignedStudentDetailDTO(
        Integer id,
        Integer studentId,
        String fullName,
        String carnet,
        Boolean emergencyContact,
        String relationType,
        String routePhoto,
        int age
) {
}
