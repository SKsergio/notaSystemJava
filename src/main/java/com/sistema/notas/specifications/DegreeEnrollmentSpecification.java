package com.sistema.notas.specifications;

import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

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

    public static Specification<DegreeEnrollment> hasStatus(EnrollmentStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<DegreeEnrollment> byGradeDetail(Long gradeDetailId) {
        return (root, query, criteriaBuilder) -> {
            if (gradeDetailId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("gradeDetail").get("id"), gradeDetailId);
        };
    }

    public static Specification<DegreeEnrollment> betweenDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            }

            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), fromDate, toDate);
            } else if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate);
            }
        };
    }
}