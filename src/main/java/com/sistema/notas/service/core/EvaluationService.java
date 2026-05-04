package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.evaluations.*;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.enums.StatusEnum;

public interface EvaluationService {
    EvaluationsResponseDTO save(EvaluationRequestDTO evaluationDTO);
    EvaluationsResponseDTO update(Integer id, EvaluationRequestDTO evaluationDTO);
    void delete(Integer id);
    PaginateResponse<EvaluationsResponseDTO> obtenerEvaluations(int page, int size, String search ,LocalDate fromDate, LocalDate  toDate);
    List<EvaluationSimpleResponse> listarToSelects();
    EvaluationFullResponseDTO obtenerOneEvaluation(Integer id);
    //abrir y cerrar evaluaciones
    EvaluationsResponseDTO changeEvaluationStatus(Integer id, StatusEnum newState);
    //obtener para edicion
    EvaluationEditResponse obtenerEditResponse(Integer id);
}
