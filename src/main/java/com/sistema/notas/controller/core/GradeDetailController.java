package com.sistema.notas.controller.core;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.core.gradeDetail.GradeDetailFullResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailRequestDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.GradeDetailService;

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
@RequestMapping("api/core/grade-detail")
public class GradeDetailController {
    private final GradeDetailService gradeDetailService;

    @PostMapping()
    public ResponseEntity<GradeDetailResponseDTO> createGradesDetail(@Valid @RequestBody GradeDetailRequestDTO entity) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeDetailService.save(entity));
    }

    @GetMapping("all")
    public ResponseEntity<List<GradeDetailSimpleResponseDTO>> getAllGradeDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(gradeDetailService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<GradeDetailResponseDTO>> getGradesDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                gradeDetailService.obtenerGradaDetailPaginados(page, size, search, fromDate, toDate ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GradeDetailResponseDTO> UpdateDetailGrade(
            @Validated @RequestBody GradeDetailRequestDTO gradeDetailDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeDetailService.update(id, gradeDetailDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDegree(@PathVariable Integer id) {
        gradeDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<GradeDetailFullResponseDTO> getGradeDetail(@PathVariable Integer id) {
        GradeDetailFullResponseDTO responseDTO = gradeDetailService.obtenerOneGradeDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
