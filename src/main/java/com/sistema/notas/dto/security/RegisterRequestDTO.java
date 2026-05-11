package com.sistema.notas.dto.security;

import com.sistema.notas.entity.enums.Role;
import jakarta.validation.constraints.*;

/**
 * Crear un usuario nuevo. Pensado para uso administrativo
 * (un ADMIN crea cuentas para profesores/alumnos).
 */
public record RegisterRequestDTO(

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo no es valido")
        @Size(max = 100, message = "El correo es demasiado largo")
        String email,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 8, max = 100, message = "La contrasena debe tener entre 8 y 100 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "La contrasena debe contener al menos una letra y un numero"
        )
        String password,

        @NotNull(message = "El rol es obligatorio")
        Role role,

        Integer teacherId,

        Integer studentId
) {
}
