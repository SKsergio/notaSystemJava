package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.EvaluationDetailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/evaluation-details")
public class EvaluationDetailController {
    private final EvaluationDetailService evaluationDetailService;

    @PostMapping
    public ResponseEntity<EvaluationDetailResponseDTO> create(
            @Valid @RequestBody EvaluationDetailRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationDetailService.save(requestDTO));
    }
    @PostMapping("batch")
    public ResponseEntity<List<EvaluationDetailResponseDTO>> createinBatch(
            @Valid @RequestBody BatchEvaluationDetailDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationDetailService.calificateinBatch(requestDTO));
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<EvaluationDetailResponseDTO>> getPaginate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                evaluationDetailService.getDetailsPaginated(page, size, search, fromDate, toDate));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<EvaluationDetailResponseDTO> updateRecord(
            @Validated @RequestBody EvaluationDetailEditRequestDTO evaluationDetailDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationDetailService.update(id, evaluationDetailDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Integer id) {
        evaluationDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDetailFullResponseDTO> getOneDetail(@PathVariable Integer id) {
        evaluationDetailService.getOneDetail(id);
        return ResponseEntity.ok(evaluationDetailService.getOneDetail(id));
    }

    // editar
    @GetMapping("/edit/{id}")
    public ResponseEntity<EvaluationDetailEditResponseDTO> getOneEdit(@PathVariable Integer id) {
        evaluationDetailService.getOneDetail(id);
        return ResponseEntity.ok(evaluationDetailService.getOneDetailEdit(id));
    }

    // devolver notas por evaluacion
    @GetMapping("/by_evaluation/{evaluationId}")
    public ResponseEntity<List<EvaluationDetailSimpleResponseDTO>> gradesByEvaluation(
            @PathVariable Integer evaluationId) {
        return ResponseEntity.ok(evaluationDetailService.getGradesByEvaluation(evaluationId));
    }

    // devolver notas de un alumno por curso
    @GetMapping("/by_course/{studentId}/{courseId}")
    public ResponseEntity<List<EvaluationDetailResponseDTO>> gradesByCourseStudent(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        return ResponseEntity.ok(
                evaluationDetailService.getGradesByStudentAndCourse(studentId, courseId));
    }

    // devolver notas de un alumno por curso
    @GetMapping("/average/{studentId}/{courseId}")
    public ResponseEntity<Double> currentAverage(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        return ResponseEntity.ok(
                evaluationDetailService.getCurrentStudentAverage(studentId, courseId));
    }
    @GetMapping("/evaluation/{evaluationId}")
    public ResponseEntity<PaginateResponse<EvaluationGradebookDTO>> getGradebook(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Integer evaluationId
    ) {
        return ResponseEntity.ok(
                evaluationDetailService.getEvaluationGradebook(page, size, evaluationId));
    }
}
