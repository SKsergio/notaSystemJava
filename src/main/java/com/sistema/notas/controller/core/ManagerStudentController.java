package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.managerStudents.AssignedStudentDetailDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestEditDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
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
    public ResponseEntity<PaginateResponse<ManagerStudentResponseDTO>> getManagerStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                managerStudentService.obtenerManagerStudents(page, size, search, fromDate, toDate ));
    }

    @GetMapping("assignedStudents/{managerId}")
    public ResponseEntity<PaginateResponse<AssignedStudentDetailDTO>> getAssigned(
            @PathVariable Integer managerId,

            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search){
        return ResponseEntity.ok(
                managerStudentService.getAsignedStudents(page, size, search,managerId ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Integer id) {
        managerStudentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
