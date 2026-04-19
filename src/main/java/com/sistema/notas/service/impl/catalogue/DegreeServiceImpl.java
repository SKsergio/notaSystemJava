package com.sistema.notas.service.impl.catalogue;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.catalogues.CatalogueMapper;
import com.sistema.notas.mapper.catalogues.DegreeMapper;
import com.sistema.notas.respository.catalogues.DegreeRespository;
import com.sistema.notas.service.catalogue.DegreeService;
import lombok.RequiredArgsConstructor;
import com.sistema.notas.specifications.CatalogoSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    //inyectamos al repository
    private final DegreeRespository degreeRespository;
    private final PageMapper pageMapper;
    private final DegreeMapper degreeMapper;
    private final CatalogueMapper catalogueMapper;

    @Override
    public CatalogueResponseDTO save(CatalogueRequestDto request) {
        //validamos
        if(degreeRespository.existsByCode(request.code())){
            throw new BadRequestException("Ya existe un Grado con ese codigo");
        }
        //convertit a entity
        Degree entity = catalogueMapper.toEntity(
                request,
                Degree::new
        );

        Degree saved = degreeRespository.save(entity);
        return catalogueMapper.toResponse(saved);
    }

    @Override
    public PaginateResponse<CatalogueResponseDTO> obtenerGradosPaginados(int page, int size, String search, LocalDate  fromDate, LocalDate  toDate) {
        Pageable pageable = PageRequest.of(page, size); //paginacin

        //filtros
        Specification<Degree> filtros = Specification
                .where(CatalogoSpecification.<Degree>searchContains(search))
                .and(CatalogoSpecification.<Degree>createdBetween(fromDate, toDate));   

        Page<Degree> result = degreeRespository.findAll(filtros, pageable);

        return pageMapper.toPaginateResponse(
            result,
            degreeMapper::toDTO
        );
    }


    @Override
    public CatalogueResponseDTO update(Integer id, CatalogueRequestDto degree) {
        Degree oldDegree = degreeRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("El grado con el id: " + id + " no existe")
        );

        //convertir a entity
        Degree entity = catalogueMapper.toEntity(
                degree,
                Degree::new
        );

        oldDegree.setName(entity.getName());
        oldDegree.setCode(entity.getCode());

        Degree updated = degreeRespository.save(oldDegree);
        return catalogueMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        Degree oldDegree = degreeRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("El grado con el id: " + id + " no existe")
        );
        CatalogueResponseDTO res =  catalogueMapper.toResponse(oldDegree);

        degreeRespository.delete(oldDegree);
    }

    @Override
    public List<CatalogSimpleResponseDTO> obtenerGradosParaSelect() {
        List<Degree> degrees = degreeRespository.findAll();

        // 2. Los mapeamos al DTO ligero
        return degrees.stream()
                .map(dg -> new CatalogSimpleResponseDTO(dg.getId(), dg.getName()))
                .toList();
    }

    @Override
    public CatalogueResponseDTO findById(Integer id) {

        Degree degreeFind = degreeRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("El grado con el id: " + id + " no existe")
        );

        return catalogueMapper.toResponse(degreeFind);
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
