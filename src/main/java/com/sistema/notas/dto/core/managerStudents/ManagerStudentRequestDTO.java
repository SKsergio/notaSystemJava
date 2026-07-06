package com.sistema.notas.dto.core.managerStudents;

import com.sistema.notas.entity.enums.RelationTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ManagerStudentRequestDTO(
        @NotNull(message = "EL alumno debe ser obligatorio.")
        @Positive(message = "El ID del ALumno debe ser un número positivo válido.")
        Integer studentId,

        @NotNull(message = "El encargado es obligatorio.")
        @Positive(message = "El ID del encargado debe ser un número positivo válido.")
        Integer managerId,

        @NotNull(message = "El tipo de relacion con los alumnos es obligatoria")
        RelationTypeEnum relationType,

        @NotNull(message = "Es requerido que se especifique si el encargado es contacto de emergencia")
        Boolean emergencyContact
){
}
