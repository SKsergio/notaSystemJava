package com.sistema.notas.controller.catalogues;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.SubjectRequestDTO;
import com.sistema.notas.dto.catalogues.SubjectResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.catalogue.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/catalogue/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectResponseDTO> createSubject(@Validated @RequestBody SubjectRequestDTO subjectDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.save(subjectDTO));
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<SubjectResponseDTO>> getSubjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate){
        return ResponseEntity.ok(
                subjectService.obtenerSubjectsPaginadas(page, size, search, fromDate, toDate ));
    }

    @GetMapping("all")
    public ResponseEntity<List<CatalogSimpleResponseDTO>> getAllSubjects() {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.obtenerSubjectSelect());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable Integer id,
            @Validated @RequestBody SubjectRequestDTO subjectDTO){
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.update(id, subjectDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> getSubject(@PathVariable Integer id) {
        SubjectResponseDTO responseDTO = subjectService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
