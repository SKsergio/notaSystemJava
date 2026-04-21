package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.gradeDetail.GradeDetailFullResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailRequestDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

public interface GradeDetailService {
    
    //funcion para devolver historial de notas por grado
    //funcion para devolver el total de alumnos asignados a este grado
    //funcion calcular promedio global del aula

    GradeDetailResponseDTO save(GradeDetailRequestDTO gradeDetail);
    GradeDetailResponseDTO update(Integer id, GradeDetailRequestDTO gradeDetail);
    void delete(Integer id);
    PaginateResponse<GradeDetailResponseDTO> obtenerGradaDetailPaginados(int page, int size, String search ,LocalDate fromDate, LocalDate  toDate);
    List<GradeDetailSimpleResponseDTO> listarToSelects();
    GradeDetailFullResponseDTO obtenerOneGradeDetail(Integer id);
}
