package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.evaluations.EvaluationFullResponseDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationRequestDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationSimpleResponse;
import com.sistema.notas.dto.core.evaluations.EvaluationsResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

public interface EvaluationService {
    EvaluationsResponseDTO save(EvaluationRequestDTO evaluationDTO);
    EvaluationsResponseDTO update(Integer id, EvaluationRequestDTO evaluationDTO);
    void delete(Integer id);
    PaginateResponse<EvaluationsResponseDTO> obtenerEvaluations(int page, int size, String search ,LocalDate fromDate, LocalDate  toDate);
    List<EvaluationSimpleResponse> listarToSelects();
    EvaluationFullResponseDTO obtenerOneEvaluation(Integer id);

    //abrir y cerrar evaluaciones
    EvaluationsResponseDTO openEvaluation(Integer id);
    EvaluationsResponseDTO closeEvaluation(Integer id);
}
