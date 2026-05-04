package com.sistema.notas.entity.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.sistema.notas.entity.AuditableEntity;

@Entity
@Table(name = "evaluation_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"evaluation_id", "student_id"})
})
@Getter @Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE evaluation_details SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class EvaluationDetail extends AuditableEntity{
    @Column(name = "grade", nullable = false)
    private Double grade;

    @Column(name = "feedback", length = 255, nullable = true)
    private String feedback;

    // Relación con la Evaluación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    // Relación con el Alumno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
