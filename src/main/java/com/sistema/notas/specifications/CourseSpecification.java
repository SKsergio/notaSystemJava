package com.sistema.notas.specifications;

import com.sistema.notas.entity.core.Course;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CourseSpecification {

    public static Specification<Course> search(String keyword) {
        return (Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            // Buscamos en las tablas relacionadas ya que Course no tiene campos de texto propios
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("subject").get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("teacher").get("firstName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("teacher").get("lastName")), searchPattern)
            );
        };
    }
}