package com.sistema.notas.specifications;

import com.sistema.notas.entity.catalogues.Degree;
import org.springframework.data.jpa.domain.Specification;

public class DegreeSpecification {
    public static Specification<Degree> search(String search){
        return (root, query, cb) ->{
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String value = "%" + search.toUpperCase() + "%";

            return cb.or(
                cb.like(cb.upper(root.get("name")), value),
                cb.like(cb.upper(root.get("code")), value)
            );
        };
    }
}
