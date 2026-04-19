package com.sistema.notas.mapper.catalogues;

import com.sistema.notas.dto.catalogues.SubjectRequestDTO;
import com.sistema.notas.dto.catalogues.SubjectResponseDTO;
import com.sistema.notas.entity.catalogues.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubjectMapper {

    SubjectResponseDTO toResponse(Subject subject);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subject toEntity(SubjectRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subject updateEntityFromDto(SubjectRequestDTO requestDTO, @MappingTarget Subject subject);
}
