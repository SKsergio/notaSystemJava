package com.sistema.notas.service.catalogue.impl;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.catalogues.Section;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.catalogues.CatalogueMapper;
import com.sistema.notas.mapper.catalogues.SectionMapper;
import com.sistema.notas.respository.catalogues.SectionRespository;
import com.sistema.notas.service.catalogue.SectionService;
import com.sistema.notas.specifications.CatalogoSpecification;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    //inyeccion de dependencias
    private final SectionRespository sectionRespository;
    private final CatalogueMapper catalogueMapper;
    private final PageMapper pageMapper;
    private final SectionMapper sectionMapper;


    @Override
    public CatalogueResponseDTO save(CatalogueRequestDto section) {
        if (sectionRespository.existsByCode(section.code())){
            throw new BadRequestException("Ya existe una section con el codigo.");
        }

        Section entity = catalogueMapper.toEntity(
                section,
                Section::new
        );

        Section saved = sectionRespository.save(entity);
        return catalogueMapper.toResponse(saved);
    }

    @Override
    public CatalogueResponseDTO update(Integer id, CatalogueRequestDto section) {
        Section oldSection = sectionRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("La secion con el id: " + id + " no existe")
        );

        Section entity = catalogueMapper.toEntity(
                section,
                Section::new
        );

        oldSection.setName(entity.getName());
        oldSection.setCode(entity.getCode());

        Section updated = sectionRespository.save(oldSection);
        return catalogueMapper.toResponse(updated);
    }

    @Override
    public void delete(Integer id) {
        Section oldSection = sectionRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("La secion con el id: " + id + " no existe")
        );

        sectionRespository.delete(oldSection);
    }

    @Override
    public PaginateResponse<CatalogueResponseDTO> obtenerSectionPaginadas(int page, int size, String search, LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<Section> filtros = Specification
                .where(CatalogoSpecification.<Section>searchContains(search))
                .and(CatalogoSpecification.<Section>createdBetween(fromDate, toDate));

        Page<Section> result = sectionRespository.findAll(filtros, pagable);

        return  pageMapper.toPaginateResponse(
                result,
                sectionMapper::toDTO
        );
    }

    @Override
    public List<CatalogueSimpleResponseDTO> obtenerSectionSelect() {
        List<Section> sections = sectionRespository.findAll();

        return sections.stream()
                .map(sc-> new CatalogueSimpleResponseDTO(sc.getId(), sc.getCode()))
                .toList();
    }

    @Override
    public CatalogueResponseDTO findById(Integer id) {
        Section sectionFind = sectionRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("La secion con el id: " + id + " no existe")
        );

        return catalogueMapper.toResponse(sectionFind);
    }
}
