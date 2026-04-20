package com.sistema.notas.controller.core;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResposeDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.TeacherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/core/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Validated @ModelAttribute TeacherRequestDTO teacherDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.save(teacherDto));
    }

    // or definir el futuro de esta fuuncion
    @GetMapping("all")
    public ResponseEntity<List<TeacherSimpleResposeDTO>> getAllTeacher() {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.obtenerTeachear());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<CatalogueResponseDTO>> getDegrees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                degreeService.obtenerGradosPaginados(page, size, search, fromDate, toDate ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CatalogueResponseDTO> UpdateDegree(
            @Validated @RequestBody CatalogueRequestDto teacherDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(degreeService.update(id, teacherDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDegree(@PathVariable Integer id) {
        degreeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<CatalogueResponseDTO> getDegree(@PathVariable Integer id) {
        CatalogueResponseDTO responseDTO = degreeService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
