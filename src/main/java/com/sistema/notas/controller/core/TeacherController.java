package com.sistema.notas.controller.core;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sistema.notas.dto.core.teacher.TeacherFullResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
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

    @GetMapping("all")
    public ResponseEntity<List<TeacherSimpleResponseDTO>> getAllTeacher() {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.listartoSelect());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<TeacherResponseDTO>> getTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                teacherService.obtenerTeacherPaginados(page, size, search, fromDate, toDate ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> UpdateTeacher(
            @Validated @ModelAttribute TeacherRequestDTO teacherDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.update(id, teacherDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Integer id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<TeacherFullResponseDTO> getTeacher(@PathVariable Integer id) {
        TeacherFullResponseDTO responseDTO = teacherService.obtenerTeachear(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
