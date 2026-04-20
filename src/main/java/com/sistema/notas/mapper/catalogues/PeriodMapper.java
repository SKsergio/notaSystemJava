package com.sistema.notas.mapper.catalogues;

import com.sistema.notas.dto.catalogues.PeriodRequestDTO;
import com.sistema.notas.dto.catalogues.PeriodResponseDTO;
import com.sistema.notas.entity.catalogues.Period;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PeriodMapper {
    PeriodResponseDTO toResponseDTO(Period period);

    Period toEntity(PeriodRequestDTO requestDTO);

    Period updateEntityFromDTO(PeriodRequestDTO requestDTO, @MappingTarget Period period);
}
