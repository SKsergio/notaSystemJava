package com.sistema.notas.controller.catalogues;


import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.service.catalogue.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("api/catalogue/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<CatalogueResponseDTO> creteSection(@Validated @RequestBody CatalogueRequestDto sectionDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sectionService.save(sectionDto));
    }

    //para los selects
    @GetMapping("all")
    public ResponseEntity<List<CatalogSimpleResponseDTO>> getAllDegrees() {
        return ResponseEntity.status(HttpStatus.OK).body(sectionService.obtenerSectionSelect());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<CatalogueResponseDTO>> getSection(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso =DateTimeFormat.ISO.DATE)LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso =DateTimeFormat.ISO.DATE)LocalDate toDate
            ){

        return ResponseEntity.ok(
            sectionService.obtenerSectionPaginadas(page, size, search, fromDate, toDate)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CatalogueResponseDTO> updateSection(
            @Validated @RequestBody CatalogueRequestDto sectionDto,
            @PathVariable Integer id
            ){
        return ResponseEntity.status(HttpStatus.OK).body(sectionService.update(id, sectionDto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable Integer id) {
        sectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogueResponseDTO> getSection(@PathVariable Integer id) {
        CatalogueResponseDTO responseDTO = sectionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}
