package com.sistema.notas.specifications;

import com.sistema.notas.entity.core.GradeDetail;
import org.springframework.data.jpa.domain.Specification;

public class GradeDetailSpecification {

    public static Specification<GradeDetail> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    // degree y section heredan 'name' de AbstractCatalogo
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("degree").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("section").get("name")), searchPattern),

                    // Asegúrate de que en Teacher.java los campos sean firstName y firstLastName
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tutor").get("firstName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tutor").get("firstLastName")), searchPattern)
            );
        };
    }
}