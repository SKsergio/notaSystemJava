package com.sistema.notas.service.catalogue.impl;

import com.sistema.notas.dto.catalogues.PeriodRequestDTO;
import com.sistema.notas.dto.catalogues.PeriodResponseDTO;
import com.sistema.notas.dto.catalogues.PeriodSimpleResponseDto;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.catalogues.Period;
import com.sistema.notas.entity.enums.StatusEnum;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.catalogues.PeriodMapper;
import com.sistema.notas.respository.catalogues.PeriodRespository;
import com.sistema.notas.respository.core.CoursesRespository;
import com.sistema.notas.respository.core.EvaluationsRepository;
import com.sistema.notas.service.catalogue.PeriodService;
import com.sistema.notas.specifications.CatalogoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodServiceImpl implements PeriodService {

    private final PeriodRespository periodRespository;
    private final PeriodMapper periodMapper;
    private final PageMapper pageMapper;

    //ralaciones
    private final CoursesRespository coursesRespository;
    private final EvaluationsRepository evaluationsRepository;

    @Override
    public PeriodResponseDTO save(PeriodRequestDTO period) {

        if (period.startDate().isAfter(period.endDate())){
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        if (periodRespository.existsOverlappingPeriod(period.startDate(), period.endDate())){
            throw new BadRequestException("Las fechas indicadas se cruzan con un periodo escolar ya existente.");
        }

        Period entity = periodMapper.toEntity(period);
        Period saved = periodRespository.save(entity);
        return periodMapper.toResponseDTO(saved);

    }

    @Override
    public PeriodResponseDTO update(Integer id, PeriodRequestDTO period) {

        Period periodFind = periodRespository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Periodo con el id: " + id + " no existe.")
        );

        if (period.startDate().isAfter(period.endDate())){
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        if (periodRespository.existsOverlappingPeriodForUpdate(period.startDate(), period.endDate(), id)){
            throw new BadRequestException("Ya existe un periodo establecido para estas fechas");
        }

        periodFind = periodMapper.updateEntityFromDTO(period, periodFind);
        return periodMapper.toResponseDTO(periodRespository.save(periodFind));

    }

    @Override
    public void delete(Integer id) {
       Period periodFind = periodRespository.findById(id).orElseThrow(
               ()->new ResourceNotFoundException("Periodo con el id: " + id + " no existe.")
       );

       if (coursesRespository.existsByPeriodId(id)) {
            throw new BadRequestException("No se puede eliminar el Periodo porque tiene Cursos asignados y activos.");
       }

       periodRespository.delete(periodFind);
    }

    @Override
    public PaginateResponse<PeriodResponseDTO> obtenerPeriodsPaginados(int page, int size, LocalDate fromDate, LocalDate toDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Period> filtros = Specification.
                where(CatalogoSpecification.<Period>createdBetween(fromDate, toDate));

        Page<Period> periods = periodRespository.findAll(filtros, pageable);

        return pageMapper.toPaginateResponse(
                periods,
                periodMapper::toResponseDTO
        );
    }

    @Override
    public List<PeriodSimpleResponseDto> listartoSelects() {
        List<Period> periods = periodRespository.findAll();

        return periods.stream()
                .map(per -> new PeriodSimpleResponseDto(per.getId(), per.getStartDate(), per.getEndDate()))
                .toList();
    }

    @Override
    public PeriodResponseDTO obtenerPeriod(Integer id) {
        Period periodFind = periodRespository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Periodo con el id: " + id + " no existe.")
        );

        return periodMapper.toResponseDTO(periodFind);
    }

    @Transactional
    @Override
    public PeriodResponseDTO changePeriodStatus(Integer id, StatusEnum newState) {
        Period periodFind = periodRespository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun periodo con el id: " + id));

        if (periodFind.getStatus().equals(newState)){
            return periodMapper.toResponseDTO(periodFind);
        }

        //cascada de nivel 1
        evaluationsRepository.updateEvaluationStatusByPeriodId(id, newState);
        //cascade de nivel 2
        coursesRespository.updateCourseStatusByPeriodId(id, newState);

        periodFind.setStatus(newState);
        periodRespository.save(periodFind);
        return periodMapper.toResponseDTO(periodFind);
    }
}
