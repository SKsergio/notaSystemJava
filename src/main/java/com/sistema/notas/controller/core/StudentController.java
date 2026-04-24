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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.core.student.StudentFullResponseDTO;
import com.sistema.notas.dto.core.student.StudentRequestDTO;
import com.sistema.notas.dto.core.student.StudentResponseDTO;
import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherFullResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Validated @ModelAttribute StudentRequestDTO studentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.save(studentDto));
    }

    @GetMapping("all")
    public ResponseEntity<List<StudentSimpleResponseDTO>> getAllStudent() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.listartoSelect());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<StudentResponseDTO>> getStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                studentService.obtenerStudentPaginados(page, size, search, fromDate, toDate ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @Validated @ModelAttribute StudentRequestDTO studentDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.update(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<StudentFullResponseDTO> getStudent(@PathVariable Integer id) {
        StudentFullResponseDTO responseDTO = studentService.obtenerStudent(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
