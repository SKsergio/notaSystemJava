package com.sistema.notas.mapper.catalogues;

import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.entity.catalogues.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {
    public CatalogueResponseDTO toDTO(Section section){
        return new CatalogueResponseDTO(
                section.getId(),
                section.getName(),
                section.getCode(),
                section.getCreatedAt()
        );
    }
}
