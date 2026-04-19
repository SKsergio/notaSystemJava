package com.sistema.notas.dto.generics;

import java.util.List;

public record PaginateResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
