package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailFullResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailRequestDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.entity.catalogues.Section;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Teacher;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GradeDetailMapper {

    // 1. DE ENTIDAD A RESPONSE (Salida de datos - Aplanamiento)
    @Mapping(source = "section.name", target = "sectionName")
    @Mapping(source = "degree.name", target = "degreeName")
    @Mapping(source = "tutor.firstName", target = "tutorName")
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

    GradeDetailFullResponseDTO toFullResponseDTO(GradeDetail entity);

    default CatalogueSimpleResponseDTO mapDegreeToSimpleDTO(Degree degree) {
        if (degree == null)
            return null;
        return new CatalogueSimpleResponseDTO(degree.getId(), degree.getName());
    }

    default CatalogueSimpleResponseDTO mapSectionToSimpleDTO(Section section) {
        if (section == null)
            return null;
        return new CatalogueSimpleResponseDTO(section.getId(), section.getName());
    }

    default TeacherSimpleResponseDTO mapTeacherToSimpleDTO(Teacher tutor) {
        if (tutor == null)
            return null;
        return new TeacherSimpleResponseDTO(tutor.getId(), tutor.getFirstName(), tutor.getFirstLastName(),
                tutor.getEmail(), tutor.getRoutePhoto(), tutor.getAge(), tutor.getDui());
    }
}