package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditRequestDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailFullResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailRequestDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

public interface EvaluationDetailService {
    //crud basico
    EvaluationDetailResponseDTO save(EvaluationDetailRequestDTO requestDTO);
    EvaluationDetailResponseDTO update(Integer id, EvaluationDetailEditRequestDTO requestDTO);
    void delete(Integer id);
    PaginateResponse<EvaluationDetailResponseDTO> getDetailsPaginated(int page, int size, String search ,LocalDate fromDate, LocalDate  toDate);
    EvaluationDetailFullResponseDTO getOneDetail(Integer id);
    EvaluationDetailEditResponseDTO getOneDetailEdit(Integer id);

    // VISTA DEL PROFESOR: Cuando el docente hace clic en una "Evaluación" para calificar a toda la clase.
    List<EvaluationDetailSimpleResponseDTO> getGradesByEvaluation(Integer evaluationId);
    // 2. VISTA DEL ALUMNO / DETALLE: Cuando el alumno entra a un curso para ver qué tareas le han calificado.
    List<EvaluationDetailResponseDTO> getGradesByStudentAndCourse(Integer studentId, Integer courseId);
    // 3. CÁLCULO EN TIEMPO REAL: Para mostrar el "Promedio Actual" acumulado en la pantalla del curso.
    Double getCurrentStudentAverage(Integer studentId, Integer courseId);
}
