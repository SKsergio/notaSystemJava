package com.sistema.notas.entity.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.FetchType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.enums.EnrollmentStatus;

@Entity
// @Table(name = "course_registration", uniqueConstraints = {
//         @UniqueConstraint(columnNames = {"student_id", "course_id"})
// })
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE course_registration SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class CourseRegistration extends AuditableEntity{
    
    //muchos cursos a una matricula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "final_average", nullable = true)
    private Double finalAverage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;
}
