package com.sistema.notas.controller.core;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.notas.dto.core.course.CourseFullResponseDTO;
import com.sistema.notas.dto.core.course.CourseRequestDTO;
import com.sistema.notas.dto.core.course.CourseResponseDTO;
import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.CourseService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/core/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping()
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseDto));
    }

    @GetMapping("all")
    public ResponseEntity<List<CourseSimpleResponseDTO>> getAllCourses() {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<CourseResponseDTO>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                courseService.obtenerCoursePaginados(page, size, search, fromDate, toDate ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> UpdateCourses(
            @Validated @RequestBody CourseRequestDTO courseDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.update(id, courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDegree(@PathVariable Integer id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<CourseFullResponseDTO> getOneCourse(@PathVariable Integer id) {
        CourseFullResponseDTO responseDTO = courseService.obtenerOneCourse(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
