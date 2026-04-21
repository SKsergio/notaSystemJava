package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.gradeDetail.GradeDetailRequestDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailResponseDTO;
import com.sistema.notas.entity.core.GradeDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GradeDetailMapper {

    // 1. DE ENTIDAD A RESPONSE (Salida de datos - Aplanamiento)
    @Mapping(source = "section.name", target = "sectionName")
    @Mapping(source = "degree.name", target = "degreeName")
    @Mapping(source = "tutor.name", target = "tutorName")
    GradeDetailResponseDTO toResponse(GradeDetail entity);

    // 2. DE REQUEST A ENTIDAD (Entrada de datos)
    @Mapping(target = "degree", ignore = true)
    @Mapping(target = "section", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    GradeDetail toEntity(GradeDetailRequestDTO requestDTO);

    // 3. ACTUALIZACIÓN DE ENTIDAD EXISTENTE
    @Mapping(target = "degree", ignore = true)
    @Mapping(target = "section", ignore = true)
    @Mapping(target = "tutor", ignore = true)
    void updateEntityFromDTO(GradeDetailRequestDTO requestDTO, @MappingTarget GradeDetail gradeDetail);
}