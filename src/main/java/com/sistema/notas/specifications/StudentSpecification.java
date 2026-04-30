package com.sistema.notas.specifications;

import com.sistema.notas.entity.core.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<Student> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("secondName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstLastName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("secondLastName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("carnet")), searchPattern)
            );
        };
    }
}