package com.sistema.notas.mapper.catalogues;

import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.entity.catalogues.Degree;
import org.springframework.stereotype.Component;

@Component
public class DegreeMapper {
    public CatalogueResponseDTO toDTO(Degree degree){

        return new CatalogueResponseDTO(
                degree.getId(),
                degree.getName(),
                degree.getCode(),
                degree.getCreatedAt()
        );
    }
}
