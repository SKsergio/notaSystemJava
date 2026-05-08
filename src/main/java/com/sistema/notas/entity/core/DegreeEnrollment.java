package com.sistema.notas.entity.core;

import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
// @Table(name = "degree_enrollment", uniqueConstraints = {
//         @UniqueConstraint(columnNames = {"student_id", "grade_detail_id"})
// })
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE degree_enrollment SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class DegreeEnrollment extends AuditableEntity {

    //muchos detalles de grado pertenecen a una matricula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_detail_id", nullable = false)
    private GradeDetail gradeDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;
}
