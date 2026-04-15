package com.sistema.notas.controller.catalogues;

import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.service.catalogue.DegreeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/catalogue/degrees")
@RequiredArgsConstructor
public class DegreeController {
    //inyectamos el servicio
    private final DegreeService degreeService;

    @PostMapping
    public ResponseEntity<CatalogueResponseDTO> createDegree(@Validated @RequestBody CatalogueRequestDto degreeDto) {
        //Convertir a entity
        Degree degree = new Degree();
        degree.setName(degreeDto.name());
        degree.setCode(degreeDto.code());

        //guardar
        Degree degreeSave = degreeService.save(degree);

        CatalogueResponseDTO responseDTO = new CatalogueResponseDTO(
                degreeSave.getId(),
                degreeSave.getName(),
                degreeSave.getCode(),
                degreeSave.getCreatedAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("all")
    public ResponseEntity<List<CatalogueResponseDTO>> getAllDegrees() {

        List<Degree> degrees = degreeService.findAll();
        List<CatalogueResponseDTO> responseDTO = degrees.stream().
                map(dg-> new CatalogueResponseDTO(
                        dg.getId(),
                        dg.getName(),
                        dg.getCode(),
                        dg.getCreatedAt()
                )).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PaginateResponse<CatalogueResponseDTO>> getDegrees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                degreeService.obtenerGradosPaginados(page, size)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CatalogueResponseDTO>UpdateDegree(
            @Validated @RequestBody CatalogueRequestDto degreeDto,
            @PathVariable Integer id
            ) {
        Degree degree = new Degree();
        degree.setId(id);
        degree.setName(degreeDto.name());
        degree.setCode(degreeDto.code());

        Degree degreeSave = degreeService.update(id,degree);
        CatalogueResponseDTO responseDTO = new CatalogueResponseDTO(
                degree.getId(),
                degree.getName(),
                degree.getCode(),
                degreeSave.getCreatedAt()
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDegree(@PathVariable Integer id) {
        degreeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogueResponseDTO> getDegree(@PathVariable Integer id) {
        Optional<Degree> degree = degreeService.findById(id);
        if (!degree.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Degree degreeGet = degree.get();
        CatalogueResponseDTO degreeResponse = new CatalogueResponseDTO(
                degreeGet.getId(),
                degreeGet.getName(),
                degreeGet.getCode(),
                degreeGet.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.OK).body(degreeResponse);
    }
}
