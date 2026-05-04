package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.evaluations.*;
import com.sistema.notas.entity.enums.StatusEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.EvaluationService;

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
@RequestMapping("/api/core/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping()
    public ResponseEntity<EvaluationsResponseDTO> create(@Valid @RequestBody EvaluationRequestDTO entity) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.save(entity));
    }

    @GetMapping("all")
    public ResponseEntity<List<EvaluationSimpleResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<EvaluationsResponseDTO>> getPaginate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                evaluationService.obtenerEvaluations(page, size, search, fromDate, toDate));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EvaluationsResponseDTO> updateRecord(
            @Validated @RequestBody EvaluationRequestDTO evaluationDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.update(id, evaluationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Integer id) {
        evaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationFullResponseDTO> getOne(@PathVariable Integer id) {
        EvaluationFullResponseDTO responseDTO = evaluationService.obtenerOneEvaluation(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<EvaluationEditResponse> getOneEdit(@PathVariable Integer id) {
        EvaluationEditResponse responseDTO = evaluationService.obtenerEditResponse(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    //CAMBIAR ESTADOS
    @PatchMapping("/{id}/status")
    public ResponseEntity<EvaluationsResponseDTO> openEvaluation(@PathVariable Integer id, @RequestParam StatusEnum status) {
        return ResponseEntity.status(HttpStatus.OK).body(evaluationService.changeEvaluationStatus(id, status));
    }

}
