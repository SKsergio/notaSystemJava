package com.sistema.notas.service.impl.catalogue;

import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.respository.catalogues.DegreeRespository;
import com.sistema.notas.service.catalogue.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    //inyectamos al repository
    private final DegreeRespository degreeRespository;

    @Override
    public Degree save(Degree degree) {
        if(degreeRespository.existsByCode(degree.getCode())){
            throw new BadRequestException("Ya existe un Grado con ese codigo");
        }
        return degreeRespository.save(degree);
    }

    @Override
    public PaginateResponse<CatalogueResponseDTO> obtenerGradosPaginados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Degree> result = degreeRespository.findAll(pageable);

        List<CatalogueResponseDTO> content = result.getContent()
                .stream()
                .map(dg -> new CatalogueResponseDTO(
                        dg.getId(),
                        dg.getName(),
                        dg.getCode(),
                        dg.getCreatedAt()
                ))
                .toList();

        return new PaginateResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public Degree update(Integer id, Degree degree) {
        Degree oldDegree = degreeRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("El grado con el id: " + id + " no existe")
        );
        oldDegree.setName(degree.getName());
        oldDegree.setName(degree.getCode());

        return degreeRespository.save(oldDegree);
    }

    @Override
    public void delete(Integer id) {
        Degree oldDegree = degreeRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("El grado con el id: " + id + " no existe")
        );
        degreeRespository.delete(oldDegree);
    }

    @Override
    public List<Degree> findAll() {
        return degreeRespository.findAll();
    }

    @Override
    public Optional<Degree> findById(Integer id) {
        return  degreeRespository.findById(id);
    }

    @Override
    public Optional<Degree> findByName(String name) {
        Optional<Degree> degree = degreeRespository.findByName(name);
        return degree;
    }

    @Override
    public Optional<Degree> findByCode(String code) {
        Optional<Degree> degree = degreeRespository.findByCode(code);
        return degree;
    }
}
