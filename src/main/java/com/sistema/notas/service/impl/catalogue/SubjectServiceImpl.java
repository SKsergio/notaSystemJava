package com.sistema.notas.service.impl.catalogue;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.SubjectRequestDTO;
import com.sistema.notas.dto.catalogues.SubjectResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.catalogues.Section;
import com.sistema.notas.entity.catalogues.Subject;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.catalogues.SubjectMapper;
import com.sistema.notas.respository.catalogues.SubjectRepository;
import com.sistema.notas.service.catalogue.SubjectService;
import com.sistema.notas.specifications.CatalogoSpecification;
import jakarta.transaction.Transactional;
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
public class SubjectServiceImpl implements SubjectService {

    //inyeccion de dependencias
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final PageMapper pageMapper;


    @Override
    public SubjectResponseDTO save(SubjectRequestDTO subject) {
        if (subjectRepository.existsByCode(subject.code())){
            throw new BadRequestException("Materia con este codigo " + subject.code() + " ya existe");
        }

        Subject entity = subjectMapper.toEntity(subject);
        Subject saved = subjectRepository.save(entity);
        return subjectMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SubjectResponseDTO update(Integer id, SubjectRequestDTO subject) {
        Subject subjectoFind = subjectRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Materia con el id :" + id + " no encontrado")
        );

        //maperalo a entity para poderlo guardar
        subjectoFind = subjectMapper.updateEntityFromDto(subject, subjectoFind);

        return subjectMapper.toResponse(subjectoFind);
    }

    @Override
    public void delete(Integer id) {
        Subject subjectoFind = subjectRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Materia con el id :" + id + " no encontrado")
        );

        SubjectResponseDTO res = subjectMapper.toResponse(subjectoFind);
        subjectRepository.delete(subjectoFind);
    }

    @Override
    public PaginateResponse<SubjectResponseDTO> obtenerSubjectsPaginadas(int page, int size, String search, LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<Subject> filtros = Specification
                .where(CatalogoSpecification.<Subject>searchContains(search))
                .and(CatalogoSpecification.<Subject>createdBetween(fromDate, toDate));

        Page<Subject> subjects = subjectRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                subjects,
                subjectMapper::toResponse
        );
    }

    @Override
    public List<CatalogSimpleResponseDTO> obtenerSubjectSelect() {
        List<Subject> subjects = subjectRepository.findAll();

        return subjects.stream()
                .map(sb -> new CatalogSimpleResponseDTO(sb.getId(), sb.getName()))
                .toList();
    }

    @Override
    public SubjectResponseDTO findById(Integer id) {
        Subject subjectFind = subjectRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("La materia con el id: " + id + " no existe")
        );

        return subjectMapper.toResponse(subjectFind);
    }
}
