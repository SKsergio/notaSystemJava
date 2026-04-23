package com.sistema.notas.entity.core;

import com.sistema.notas.entity.AuditableEntity;
import jakarta.persistence.FetchType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Getter @Setter
@Table(name = "evaluations")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE evaluations SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Evaluation extends AuditableEntity{
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "percentage", nullable = false)
    private double percentage;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Transient
    public Long getDaysRemaning(){
        if (this.endDate == null) {
            return null;
        }
        LocalDate today = LocalDate.now();

        Long days = ChronoUnit.DAYS.between(today, this.endDate);
        return days < 0 ? 0 : days;
    }
}
