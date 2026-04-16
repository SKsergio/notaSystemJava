package com.sistema.notas.mapper.catalogues;

import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.entity.AbstractCatalogo;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class CatalogueMapper {
    public <T extends AbstractCatalogo> T toEntity(
            CatalogueRequestDto request,
            Supplier<T> supplier
    ){
        T entity = supplier.get();
        entity.setName(request.name());
        entity.setCode(request.code());
        return entity;
    }

    public CatalogueResponseDTO toResponse(AbstractCatalogo entity) {
        return new CatalogueResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getCreatedAt()
        );
    }
}
