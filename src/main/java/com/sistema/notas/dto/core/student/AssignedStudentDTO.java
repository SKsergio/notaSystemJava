package com.sistema.notas.dto.core.student;

public record AssignedStudentDTO(
        Integer studentId,
        String fullName,
        String carnet,
        String relationType,
        Boolean emergencyContact
) {}

//por si acaso