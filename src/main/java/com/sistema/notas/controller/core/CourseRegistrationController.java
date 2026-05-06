package com.sistema.notas.controller.core;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationRequestDTO;
import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.service.core.CourseRegistrationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/course-registrations")
public class CourseRegistrationController {
    
    private final CourseRegistrationService courseRegistrationService;

    @PostMapping()
    public ResponseEntity<CourseRegistrationResponseDTO> createRegistration(@Valid @RequestBody CourseRegistrationRequestDTO registrationDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(courseRegistrationService.save(registrationDTO));
    }

    @GetMapping("all")
    public ResponseEntity<List<CourseRegistrationResponseDTO>> getAllRegistrations() {
        return ResponseEntity.status(HttpStatus.OK).body(courseRegistrationService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<CourseRegistrationResponseDTO>> getRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                courseRegistrationService.obtenerRegistrosPaginados(page, size, search, fromDate, toDate ));
    }

    //CAMBIAR ESTADOS
    @PatchMapping("/{id}/status")
    public ResponseEntity<CourseRegistrationResponseDTO> openEvaluation(@PathVariable Integer id, @RequestBody EnrollmentStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(courseRegistrationService.changeStatusRegistration(id, status));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<PaginateResponse<CourseRegistrationResponseDTO>> getEnrollsByGradeDetail(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Integer courseId
        ) {
        return ResponseEntity.ok(
                courseRegistrationService.getRegistrationByCourse(page, size, courseId));
    }
}
