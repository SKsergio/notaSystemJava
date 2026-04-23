package com.sistema.notas.controller.catalogues;

import com.sistema.notas.dto.catalogues.*;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.catalogue.PeriodService;
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
@RequestMapping("/api/catalogue/periods")
public class PeriodController {

    private final PeriodService periodService;

    @PostMapping
    public ResponseEntity<PeriodResponseDTO> createPerid(@Validated @RequestBody PeriodRequestDTO period){
        return ResponseEntity.status(HttpStatus.CREATED).body(periodService.save(period));
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<PeriodResponseDTO>> getPeriods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate){
        return ResponseEntity.ok(
                periodService.obtenerPeriodsPaginados(page, size, fromDate, toDate ));
    }

    @GetMapping("all")
    public ResponseEntity<List<PeriodSimpleResponseDto>> getAllPeriods() {
        return ResponseEntity.status(HttpStatus.OK).body(periodService.listartoSelects());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> updatePeriods(
            @PathVariable Integer id,
            @Validated @RequestBody PeriodRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(periodService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePeriods(@PathVariable Integer id) {
        periodService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> getPeriods(@PathVariable Integer id) {
        PeriodResponseDTO responseDTO = periodService.obtenerPeriod(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
