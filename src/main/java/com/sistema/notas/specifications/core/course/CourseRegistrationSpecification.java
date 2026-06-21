package com.sistema.notas.specifications.core.course;

import org.springframework.data.jpa.domain.Specification;

import com.sistema.notas.entity.core.CourseRegistration;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CourseRegistrationSpecification {
    public static Specification<CourseRegistration> search(String keyword) {
        return (Root<CourseRegistration> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("fullName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("firstName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("course").get("name")), searchPattern)
            );
        };
    }
}
