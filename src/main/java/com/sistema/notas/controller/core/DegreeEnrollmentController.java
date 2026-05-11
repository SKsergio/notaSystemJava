package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentRequestDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentResponseDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.service.core.DegreeEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/enrollment-degrees")
public class DegreeEnrollmentController {
    private final DegreeEnrollmentService degreeEnrollmentService;

    @PostMapping()
    public ResponseEntity<DegreeEnrollmentResponseDTO> createDegreeEnrollment(@Valid @RequestBody DegreeEnrollmentRequestDTO enrollDTO){
        return ResponseEntity.status(HttpStatus.OK).body(degreeEnrollmentService.save(enrollDTO));
    }

    @GetMapping("all")
    public ResponseEntity<List<DegreeEnrollmentSimpleResponseDTO>> getAllEnrolls() {
        return ResponseEntity.status(HttpStatus.OK).body(degreeEnrollmentService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<DegreeEnrollmentResponseDTO>> getEnrolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                degreeEnrollmentService.obtenerEnrtollmentPaginados(page, size, search, fromDate, toDate ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrolls(@PathVariable Integer id) {
        degreeEnrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }


    //CAMBIAR ESTADOS
    @PatchMapping("/{id}/status")
    public ResponseEntity<DegreeEnrollmentResponseDTO> openEvaluation(@PathVariable Integer id, @RequestBody EnrollmentStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(degreeEnrollmentService.changeStatusEnrollment(id, status));
    }

    //obtener por id del grado
    @GetMapping("/grade-detail/{gradeDetailId}")
    public ResponseEntity<PaginateResponse<DegreeEnrollmentResponseDTO>> getEnrollsByGradeDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Integer gradeDetailId
        ) {
        return ResponseEntity.ok(
                degreeEnrollmentService.getEnrollmentsByGradeDetail(page, size, gradeDetailId));
    }

}
