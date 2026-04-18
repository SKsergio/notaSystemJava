package com.sistema.notas.specifications;


import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

public class CatalogoSpecification {

    //filtro para search
    public static <T> Specification<T> searchContains(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String value = "%" + search.toUpperCase() + "%";

            return cb.or(
                    cb.like(cb.upper(root.get("name")), value),
                    cb.like(cb.upper(root.get("code")), value));
        };
    }

    // filtro para rango de fechas
    public static<T> Specification<T> createdBetween(LocalDate fromDate, LocalDate toDate){
        return (root, query, cb) ->{
            if (fromDate == null && toDate == null) {
                return cb.conjunction();
            }

            if (fromDate != null && toDate ==null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate.atStartOfDay());
            }

            if (fromDate == null && toDate !=null) {
                return cb.lessThanOrEqualTo(root.get("createdAt"), toDate.atTime(23, 59, 59));
            }

            return cb.between(root.get("createdAt"), fromDate.atStartOfDay(), toDate.atTime(23,59,59));

        };
    }
}
