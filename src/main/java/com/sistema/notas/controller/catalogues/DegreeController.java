package com.sistema.notas.controller.catalogues;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.service.catalogue.DegreeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/catalogue/degrees")
@RequiredArgsConstructor
public class DegreeController {
    // inyectamos el servicio
    private final DegreeService degreeService;

    @PostMapping
    public ResponseEntity<CatalogueResponseDTO> createDegree(@Validated @RequestBody CatalogueRequestDto degreeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(degreeService.save(degreeDto));
    }

    // or definir el futuro de esta fuuncion
    @GetMapping("all")
    public ResponseEntity<List<CatalogSimpleResponseDTO>> getAllDegrees() {
        return ResponseEntity.status(HttpStatus.OK).body(degreeService.obtenerGradosParaSelect());
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
            @Validated @RequestBody CatalogueRequestDto degreeDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(degreeService.update(id, degreeDto));
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
