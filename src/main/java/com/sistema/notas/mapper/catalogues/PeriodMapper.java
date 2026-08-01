package com.sistema.notas.mapper.catalogues;

import com.sistema.notas.dto.catalogues.PeriodRequestDTO;
import com.sistema.notas.dto.catalogues.PeriodResponseDTO;
import com.sistema.notas.dto.catalogues.PeriodResponseEditDto;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailEditResponseDTO;
import com.sistema.notas.entity.catalogues.Period;
import com.sistema.notas.entity.core.GradeDetail;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PeriodMapper {
    @Mapping(source = "gradeDetail.fullName", target = "gradeDetailName")
    PeriodResponseDTO toResponseDTO(Period period);

    @Mapping(target = "gradeDetail", ignore = true)
    Period toEntity(PeriodRequestDTO requestDTO);

    @Mapping(source = "gradeDetail.id", target = "gradeDetailId")
    PeriodResponseEditDto toEditResponseDTO(Period entity);

    Period updateEntityFromDTO(PeriodRequestDTO requestDTO, @MappingTarget Period period);
}
