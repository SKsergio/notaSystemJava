package com.sistema.notas.controller.core;

import com.sistema.notas.dto.core.managers.*;
import com.sistema.notas.dto.core.student.*;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.service.core.ManagerService;
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
@RequestMapping("/api/core/managers")
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ManagerResponseDTO> createManager(@Validated @ModelAttribute ManagerRequestDTO managerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(managerService.save(managerRequest));
    }

    @GetMapping("all")
    public ResponseEntity<List<ManagerSimpleResponseDTO>> getAllManager(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService.listartoSelect(search));
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<ManagerResponseDTO>> getMAnager(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return ResponseEntity.ok(
                managerService
.obtenerManagerPaginados(page, size, search, fromDate, toDate ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> updateManager(
            @Validated @ModelAttribute ManagerRequestUpdateDTO managerDto,
            @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(managerService
.update(id, managerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Integer id) {
        managerService
.delete(id);
        return ResponseEntity.noContent().build();
    }

    // cambiar para usar el mapping
    @GetMapping("/{id}")
    public ResponseEntity<ManagerFullResponseDTO> getManager(@PathVariable Integer id) {
        ManagerFullResponseDTO responseDTO = managerService.obtenerManager(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("edit/{id}")
    public ResponseEntity<ManagerResponseEditDTO> getManagerEdit(@PathVariable Integer id) {
        ManagerResponseEditDTO responseDTO = managerService.obtenerManagertoEdit(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

}
