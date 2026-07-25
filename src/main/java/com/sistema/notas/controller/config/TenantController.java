package com.sistema.notas.controller.config;

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

import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.dto.security.tenant.TenantRequestDTO;
import com.sistema.notas.dto.security.tenant.TenantResponseDTO;
import com.sistema.notas.dto.security.tenant.TenantSimpleResponseDTO;
import com.sistema.notas.service.config.TenantService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/core/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantResponseDTO> createDegree(@Validated @RequestBody TenantRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.save(dto));
    }

    @GetMapping("all")
    public ResponseEntity<List<TenantSimpleResponseDTO>> getAllTenants() {
        return ResponseEntity.status(HttpStatus.OK).body(tenantService.listarToSelects());
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<TenantResponseDTO>> getDegrees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                tenantService.getTenantPaginate(page, size, search, fromDate, toDate));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> UpdateDegree(
            @Validated @RequestBody TenantRequestDTO dto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(tenantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDegree(@PathVariable Integer id) {
        tenantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDTO> getTenant(@PathVariable Integer id) {
        TenantResponseDTO responseDTO = tenantService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}
