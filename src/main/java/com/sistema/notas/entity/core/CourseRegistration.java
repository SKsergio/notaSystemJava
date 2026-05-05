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
@Table(name = "course_registration", uniqueConstraints = {

        @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE course_registration SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class CourseRegistration extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;
}