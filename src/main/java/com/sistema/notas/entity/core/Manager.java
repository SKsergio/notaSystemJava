package com.sistema.notas.entity.core;

import com.sistema.notas.entity.InstitutionalPerson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "managers")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends InstitutionalPerson {
    @Column(name ="dui", nullable = false, length = 10, unique = true)
    private String dui;
}
