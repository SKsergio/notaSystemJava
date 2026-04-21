package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

public interface TeacherService {
    //Acciones de cambios
    void ChangeDegree(Integer newDegreeId);
    void RegisterInCourse(Integer courseId);
    void EvaluateStudent(Integer studentId);

    //evaluaciones
    //pendientes

    //crud basico
    TeacherResponseDTO save(TeacherRequestDTO teacher);
    TeacherResponseDTO update(Integer id, TeacherRequestDTO teaccher);
    void delete(Integer id);
    PaginateResponse<TeacherResponseDTO> obtenerTeacherPaginados(int page, int size, String search ,LocalDate fromDate, LocalDate  toDate);
    List<TeacherSimpleResponseDTO> listartoSelect();
    TeacherResponseDTO obtenerTeachear(Integer id);
}
