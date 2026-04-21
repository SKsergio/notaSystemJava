package com.sistema.notas.dto.core.teacher;

import com.sistema.notas.entity.enums.GenderEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

public record TeacherRequestDTO(
    
    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 25, message = "El primer nombre no puede tener más de 25 caracteres")
    String firstName,

     @NotBlank(message = "El segundo nombre es obligatorio")
    @Size(max = 25, message = "El segundo nombre no puede tener más de 25 caracteres")
    String secondName,

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 25, message = "El primer apellido no puede tener más de 25 caracteres")
    String firstLastName,

    @NotBlank(message = "El segundo apellido es obligatorio")
    @Size(max = 25, message = "El segundo apellido no puede tener más de 25 caracteres")
    String secondLastName,

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección excede el límite de caracteres")
    String address,

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Size(max = 15, message = "El número de teléfono es muy largo")
    String phoneNumber,

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max= 50, message = "El email excede la cantidad de caracteres")
    String email,

    @NotNull(message = "El género es obligatorio")
    GenderEnum gender,

    @NotNull(message = "La foto es obligatoria")
    MultipartFile photo,

    // Usamos @NotNull y @Past para fechas
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    LocalDate birthDate,

    @NotBlank(message = "El campo de especialidad es obligatorio")
    @Size(max = 20, message = "La especialidad es demasiado larga")
    String speciality,

    @NotBlank(message = "El dui no puede venir vacio")
    @Size(max = 9, message = "El dui no cumple con el formato requerido")
    String dui
) {
}