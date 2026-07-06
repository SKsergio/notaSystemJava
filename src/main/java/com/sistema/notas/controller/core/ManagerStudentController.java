package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.degreeEnrollment.BatchEnrollmentRequestDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentRequestDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentResponseDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentSimpleResponseDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationRequestDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationsResponseDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestEditDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentResponseDTO;
import com.sistema.notas.dto.core.managers.ManagerRequestUpdateDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.service.core.ManagerStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/manager-student")
public class ManagerStudentController {
    private final ManagerStudentService managerStudentService;

    @PostMapping()
    public ResponseEntity<ManagerStudentResponseDTO> create(@Valid @RequestBody ManagerStudentRequestDTO recordDTO){
        return ResponseEntity.status(HttpStatus.OK).body(managerStudentService.save(recordDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ManagerStudentResponseDTO> update(
            @Validated @RequestBody ManagerStudentRequestEditDTO recordDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(managerStudentService.update(recordDto, id));
    }


    @GetMapping("all")
    public ResponseEntity<List<ManagerStudentResponseDTO>> getAllRecords() {
        return ResponseEntity.status(HttpStatus.OK).body(managerStudentService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<ManagerStudentResponseDTO>> getEnrolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                managerStudentService.obtenerManagerStudents(page, size, search, fromDate, toDate ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Integer id) {
        managerStudentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
