package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.student.StudentFullResponseDTO;
import com.sistema.notas.dto.core.student.StudentRequestDTO;
import com.sistema.notas.dto.core.student.StudentResponseDTO;
import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

public interface StudentService {
    //consultar promedio
    //cambiar de grado

    //aprobar curso
    //reprobar curso

    //Aprobar grado
    //reprobar grado

    //generar boleta de notas
    StudentResponseDTO save(StudentRequestDTO studentRequestDTO);
    StudentResponseDTO update(Integer id, StudentRequestDTO studentRequestDTO);
    void delete(Integer id);
    PaginateResponse<StudentResponseDTO> obtenerStudentPaginados(int page, int size, String search, LocalDate startDate, LocalDate endDate);
    List<StudentSimpleResponseDTO> listartoSelect();
    StudentFullResponseDTO obtenerStudent(Integer id);
}
