package com.sistema.notas.entity.core;

import com.sistema.notas.entity.AuditableEntity;

import com.sistema.notas.entity.enums.RelationTypeEnum;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "manager_students")
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE manager_students SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class ManagerStudents extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @Column(name = "relationship_type", nullable = false, length = 50)
    private RelationTypeEnum relationType;

    @Column(name = "is_emergency_contact", nullable = false)
    private Boolean emergencyContact;
}