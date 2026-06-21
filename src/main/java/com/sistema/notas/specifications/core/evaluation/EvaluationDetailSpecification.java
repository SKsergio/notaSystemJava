package com.sistema.notas.specifications.core.evaluation;

import com.sistema.notas.entity.core.EvaluationDetail;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EvaluationDetailSpecification {
    public static Specification<EvaluationDetail> search(String keyword) {
        return (Root<EvaluationDetail> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
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
