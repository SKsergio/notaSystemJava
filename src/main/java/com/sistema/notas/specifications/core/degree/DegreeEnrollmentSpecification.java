package com.sistema.notas.specifications.core.degree;

import com.sistema.notas.entity.core.DegreeEnrollment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DegreeEnrollmentSpecification {
    public static Specification<DegreeEnrollment> search(String keyword) {
        return (Root<DegreeEnrollment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("fullName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("firstName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("gradeDetail").get("fullName")), searchPattern)
            );
        };
    }
}
