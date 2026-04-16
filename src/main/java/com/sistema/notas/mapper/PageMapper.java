package com.sistema.notas.mapper;

import com.sistema.notas.dto.catalogues.PaginateResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Function;

@Component
public class PageMapper {
    public <T, R> PaginateResponse<R> toPaginateResponse(
            Page<T> page,
            Function<T, R> mapperFunction
    ){
        List<R> content = page.getContent()
                .stream()
                .map(mapperFunction)
                .toList();
        return new PaginateResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
